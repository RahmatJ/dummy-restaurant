package id.co.vantablack.order

import id.co.vantablack.order.utility.next
import id.co.vantablack.order.utility.random
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.kafka.core.KafkaTemplate

class KafkaProducerTest {

    private val kafkaTemplate = mockk<KafkaTemplate<String, Any>>(relaxed = true)
    private val kafkaProducer = KafkaProducer(kafkaTemplate)

    @Test
    fun `should publish message`() {
        val order = random.next<OrderRequest>()
        val topic = random.next<String>()

        kafkaProducer.produce(topic = topic,order)

        verify(exactly = 1) { kafkaTemplate.send(topic, order) }
    }

}