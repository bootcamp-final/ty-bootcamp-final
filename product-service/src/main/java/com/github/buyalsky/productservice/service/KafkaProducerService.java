package com.github.buyalsky.productservice.service;

import com.github.buyalsky.productservice.dto.ProductPriceChangeDto;
import com.github.buyalsky.productservice.dto.ProductStockChangeDto;

public interface KafkaProducerService {
    public void sendPriceChangeEvent(ProductPriceChangeDto priceChangeDto);

    public void sendStockChangeEvent(ProductStockChangeDto stockChangeDto);
}
