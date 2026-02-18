package id.co.vantablack.order

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Any>) {

    fun <T>produce(topic: String,messages: T) {
        kafkaTemplate.send(topic, messages)
    }

}