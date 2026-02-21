package id.co.vantablack.order

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

data class MessageBody (
    @param:JsonProperty("eventType") val eventType: String,
    @param:JsonProperty("data") val data: OrderRequest
)

@Service
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Any>) {

    fun produce(topic: String, message: MessageBody) {
        kafkaTemplate.send(topic, message)
    }

}