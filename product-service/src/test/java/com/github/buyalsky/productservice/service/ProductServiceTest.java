package com.github.buyalsky.productservice.service;

import com.github.buyalsky.productservice.dto.ProductPriceChangeDto;
import com.github.buyalsky.productservice.dto.ProductStockChangeDto;
import com.github.buyalsky.productservice.dto.UpdateProductDto;
import com.github.buyalsky.productservice.entity.Product;
import com.github.buyalsky.productservice.repository.ProductRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@EmbeddedKafka(topics = {"test-price-change", "test-stock-change"})
@DirtiesContext
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {
    private ProductRepository repository;

    private final String TOPIC_NAME_PRICE_CHANGE = "test-price-change";
    private final String TOPIC_NAME_STOCK_CHANGE = "test-stock-change";

    private Producer<String, ProductPriceChangeDto> kafkaTemplatePriceChange;
    private Producer<String, ProductStockChangeDto> kafkaTemplateStockChange;

    private KafkaProducerService kafkaProducerService;
    private Consumer<String, ProductPriceChangeDto> consumerPriceChange;
    private Consumer<String, ProductStockChangeDto> consumerStockChange;

    private ProductServiceImpl productService;

    Product mockProduct() {
        Product product = new Product();
        product.setName("demo");
        product.setCategory("category");
        product.setPrice(10.0);
        product.setNumberOfItemsLeftInStock(10);
        return product;
    }

    Product mockProductWithId(String id) {
        Product product = mockProduct();
        product.setId(id);
        return product;
    }

    @BeforeAll
    void setUp(EmbeddedKafkaBroker kafkaBroker) {
        repository = Mockito.mock(ProductRepository.class);
        kafkaProducerService = Mockito.mock(KafkaProducerService.class);
        productService = new ProductServiceImpl(repository, kafkaProducerService);
        productService.setStockCountNotificationThreshold(3);
        Map<String, Object> configs = KafkaTestUtils.producerProps(kafkaBroker);
        kafkaTemplatePriceChange = new KafkaProducer(configs, new StringSerializer(), new JsonSerializer<ProductPriceChangeDto>());

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test", "false", kafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        DefaultKafkaConsumerFactory<String, ProductPriceChangeDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(ProductPriceChangeDto.class, false));
        consumerPriceChange = cf.createConsumer();
        kafkaBroker.consumeFromAnEmbeddedTopic(consumerPriceChange, TOPIC_NAME_PRICE_CHANGE);

        Map<String, Object> configs2 = KafkaTestUtils.producerProps(kafkaBroker);
        kafkaTemplateStockChange = new KafkaProducer(configs2, new StringSerializer(), new JsonSerializer<ProductStockChangeDto>());

        Map<String, Object> consumerProps2 = KafkaTestUtils.consumerProps("test2", "false", kafkaBroker);
        consumerProps2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        DefaultKafkaConsumerFactory<String, ProductStockChangeDto> cf2 = new DefaultKafkaConsumerFactory<>(consumerProps2, new StringDeserializer(), new JsonDeserializer<>(ProductStockChangeDto.class, false));
        consumerStockChange = cf2.createConsumer();
        kafkaBroker.consumeFromAnEmbeddedTopic(consumerStockChange, TOPIC_NAME_STOCK_CHANGE);
    }

    @Test
    void getProductById() {
        //given
        String productId = "AN_ID";
        Product savedProduct = mockProductWithId(productId);
        Mockito.when(repository.findById(productId)).thenReturn(Optional.of(savedProduct));

        //when
        Product foundProduct = productService.getProductById(productId);

        //then
        assertEquals(savedProduct, foundProduct);
    }

    @Test
    void addProduct() {
        //given
        String productId = "AN_ID";
        Product product = mockProductWithId(productId);
        Mockito.when(repository.save(mockProduct())).thenReturn(product);
        Mockito.when(repository.findById(productId)).thenReturn(Optional.of(product));

        //when
        Product savedProduct = productService.addProduct(mockProduct());

        //then
        assertEquals(product, savedProduct);
    }

    @Test
    void deleteProduct() {
        //given
        Set<Product> savedProductsStub = new HashSet<>();
        String productId = "AN_ID";
        Product product = mockProductWithId(productId);
        Mockito.doAnswer(i -> {
            savedProductsStub.add(product);
            return null;
        }).when(repository).save(product);
        Mockito.doAnswer(i -> {
            savedProductsStub.remove(product);
            return null;
        }).when(repository).save(product);
        Mockito.when(repository.findById(productId))
            .thenReturn(savedProductsStub.contains(product)? Optional.of(product) : Optional.empty());

        repository.save(product);

        //when
        productService.deleteProduct(productId);

        //then
        assertTrue(repository.findById(productId).isEmpty());
    }

    @Test
    void updateProduct() {
        //given
        String productId = "ID_1";
        Product product = mockProductWithId(productId);

        Mockito.when(repository.save(product)).thenReturn(product);
        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        // when
        String newCategory = "new category";
        Product updatedProduct = productService
            .updateProduct(productId, new UpdateProductDto.Builder().category(newCategory).build());

        //then
        assertEquals(newCategory, updatedProduct.getCategory());
    }

    @Test
    void updateProductPrice() {
        //given
        Product savedProduct = mockProduct();
        savedProduct.setId("11");
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(savedProduct);
        Mockito.when(repository.findById(savedProduct.getId())).thenReturn(Optional.of(savedProduct));

        Double newProductPrice = 4.4;
        ProductPriceChangeDto priceChangeDto = new ProductPriceChangeDto(savedProduct.getId(), savedProduct.getPrice(), newProductPrice);

        Mockito.doAnswer((Answer<Void>) i -> {
            kafkaTemplatePriceChange.send(new ProducerRecord<>(TOPIC_NAME_PRICE_CHANGE, "", priceChangeDto));
            return null;
        }).when(kafkaProducerService).sendPriceChangeEvent(priceChangeDto);
        Product product = repository.save(mockProduct());

        //when
        productService.updateProduct(product.getId(), new UpdateProductDto.Builder().price(newProductPrice).build());

        //then
        ProductPriceChangeDto foundDto = KafkaTestUtils
            .getSingleRecord(consumerPriceChange, TOPIC_NAME_PRICE_CHANGE, 5000).value();

        assertEquals(priceChangeDto, foundDto);
        assertEquals(priceChangeDto.getProductId(), foundDto.getProductId());
        assertEquals(priceChangeDto.getPreviousPrice(), foundDto.getPreviousPrice());
        assertEquals(priceChangeDto.getNewPrice(), foundDto.getNewPrice());
    }

    @Test
    void updateProductStock() {
        //given
        Product savedProduct = mockProductWithId("11");
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(savedProduct);
        Mockito.when(repository.findById(savedProduct.getId())).thenReturn(Optional.of(savedProduct));

        Integer newStockCount = 2;
        ProductStockChangeDto stockChangeDto = new ProductStockChangeDto(savedProduct.getId(), newStockCount);

        Mockito.doAnswer((Answer<Void>) i -> {
            kafkaTemplateStockChange.send(new ProducerRecord<>(TOPIC_NAME_STOCK_CHANGE, "", stockChangeDto));
            return null;
        }).when(kafkaProducerService).sendStockChangeEvent(stockChangeDto);
        Product product = repository.save(mockProduct());

        //when
        productService.updateProduct(product.getId(),
            new UpdateProductDto.Builder().numberOfItemsLeftInStock(newStockCount).build());

        //then
        ProductStockChangeDto foundDto = KafkaTestUtils.getSingleRecord(consumerStockChange, TOPIC_NAME_STOCK_CHANGE, 5000).value();
        assertEquals(stockChangeDto, foundDto);
    }

    @Test
    void getAllProducts() {
    }

    @AfterAll
    void tearDown(EmbeddedKafkaBroker kafkaBroker) {
        kafkaBroker.destroy();
    }
}
