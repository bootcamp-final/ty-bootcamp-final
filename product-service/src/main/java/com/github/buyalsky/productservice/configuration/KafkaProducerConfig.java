package com.github.buyalsky.productservice.configuration;

import com.github.buyalsky.productservice.dto.ProductPriceChangeDto;
import com.github.buyalsky.productservice.dto.ProductStockChangeDto;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    @Value(value = "${topic.price-change.name}")
    private String priceChangeTopicName;

    @Value(value = "${topic.stock-change.name}")
    private String stockChangeTopicName;

    private final Map<String, Object> configProps;

    public KafkaProducerConfig() {
        this.configProps = new HashMap<>() {{
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        }};
    }

    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    public ProducerFactory<String, ProductPriceChangeDto> producerFactoryPriceChange() {
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, ProductStockChangeDto> producerFactoryStockChange() {
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ProductPriceChangeDto> kafkaTemplatePriceChange() {
        KafkaTemplate<String, ProductPriceChangeDto> template = new KafkaTemplate<>(producerFactoryPriceChange());
        template.setDefaultTopic(priceChangeTopicName);
        return template;
    }

    @Bean
    public KafkaTemplate<String, ProductStockChangeDto> kafkaTemplateStockChange() {
        KafkaTemplate<String, ProductStockChangeDto> template = new KafkaTemplate<>(producerFactoryStockChange());
        template.setDefaultTopic(stockChangeTopicName);
        return template;
    }
}
