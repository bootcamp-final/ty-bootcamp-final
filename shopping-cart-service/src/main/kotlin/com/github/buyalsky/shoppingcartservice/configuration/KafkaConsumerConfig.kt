package com.github.buyalsky.shoppingcartservice.configuration

import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeDto
import com.github.buyalsky.shoppingcartservice.dto.ProductStockChangeDto
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig {
    @Value(value = "\${kafka.bootstrapAddress}")
    private lateinit var bootstrapAddress: String

    fun consumerFactoryPriceChange(): ConsumerFactory<String, ProductPriceChangeDto> {
        val jsonDeserializer: JsonDeserializer<ProductPriceChangeDto> = JsonDeserializer<ProductPriceChangeDto>(
            ProductPriceChangeDto::class.java
        )
        jsonDeserializer.setRemoveTypeHeaders(false)
        jsonDeserializer.addTrustedPackages("*")
        jsonDeserializer.setUseTypeMapperForKey(true)
        val configs: Map<String, Any> = hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to "groupId",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )
        return DefaultKafkaConsumerFactory(configs, StringDeserializer(), jsonDeserializer);
    }

    fun consumerFactoryStockChange(): ConsumerFactory<String, ProductStockChangeDto> {
        val jsonDeserializer: JsonDeserializer<ProductStockChangeDto> = JsonDeserializer<ProductStockChangeDto>(
            ProductStockChangeDto::class.java
        )
        jsonDeserializer.setRemoveTypeHeaders(false)
        jsonDeserializer.addTrustedPackages("*")
        jsonDeserializer.setUseTypeMapperForKey(true)
        val configs: Map<String, Any> = hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to "groupId",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )
        return DefaultKafkaConsumerFactory(configs, StringDeserializer(), jsonDeserializer);
    }

    @Bean
    fun listenerContainerForPriceChange():
            ConcurrentKafkaListenerContainerFactory<String, ProductPriceChangeDto> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, ProductPriceChangeDto> = ConcurrentKafkaListenerContainerFactory()
        factory.consumerFactory = consumerFactoryPriceChange()
        return factory;
    }

    @Bean
    fun listenerContainerForStockChange():
            ConcurrentKafkaListenerContainerFactory<String, ProductStockChangeDto> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, ProductStockChangeDto> = ConcurrentKafkaListenerContainerFactory()
        factory.consumerFactory = consumerFactoryStockChange()
        return factory;
    }
}
