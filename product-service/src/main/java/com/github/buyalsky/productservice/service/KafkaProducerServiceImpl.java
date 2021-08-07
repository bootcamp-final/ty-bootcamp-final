package com.github.buyalsky.productservice.service;

import com.github.buyalsky.productservice.dto.ProductPriceChangeDto;
import com.github.buyalsky.productservice.dto.ProductStockChangeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, ProductPriceChangeDto> kafkaTemplate;
    private final KafkaTemplate<String, ProductStockChangeDto> kafkaTemplateProductStockChange;


    public KafkaProducerServiceImpl(KafkaTemplate<String, ProductPriceChangeDto> kafkaTemplate, KafkaTemplate<String, ProductStockChangeDto> kafkaTemplateProductStockChange) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateProductStockChange = kafkaTemplateProductStockChange;
    }

    public void sendPriceChangeEvent(ProductPriceChangeDto priceChangeDto) {
        kafkaTemplate.sendDefault(priceChangeDto);
    }

    public void sendStockChangeEvent(ProductStockChangeDto stockChangeDto) {
        kafkaTemplateProductStockChange.sendDefault(stockChangeDto);
    }
}
