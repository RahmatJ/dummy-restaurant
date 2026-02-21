package id.co.vantablack.order

import id.co.vantablack.order.utility.next
import id.co.vantablack.order.utility.random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.Duration
import kotlin.jvm.optionals.getOrNull
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureRestTestClient
@ImportAutoConfiguration(KafkaAutoConfiguration::class)
class ControllerTest {

    @LocalServerPort
    var port: Int = 0

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mongo = MongoDBContainer(DockerImageName.parse("mongo:8.0"))

        @Container
        @JvmStatic
        val kafka = KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"))

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)

            registry.add("spring.kafka.producer.key-serializer") { "org.apache.kafka.common.serialization.StringSerializer" }
            registry.add("spring.kafka.producer.value-serializer") { "org.springframework.kafka.support.serializer.JsonSerializer" }
        }
    }

    @Autowired
    lateinit var client: RestTestClient

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, out Any>

    @Autowired
    lateinit var consumerFactory: ConsumerFactory<String, Any>

    @Test
    fun `should return 200`() {
        val request = OrderRequest(
            orders = listOf(
                OrderRequestItem(
                    menuId = "1",
                    count = 2,
                    price = BigDecimal(10_000)
                )
            )
        )
        val expectedResponse = random.next<OrderResponse>().copy(
            orders = request.orders
        )
        val topic = "order"
        val eventType = "order_created"
        val consumer = consumerFactory.createConsumer(topic)
        consumer.subscribe(listOf(topic))

        var id = ""
        client.post().uri("/order")
            .body(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                id = body!!.id
                assertEquals(expectedResponse.orders, body!!.orders)
            }

        val data = orderRepository.findById(id).getOrNull()
        val record = KafkaTestUtils.getSingleRecord(consumer, topic, Duration.ofSeconds(10))

        assertNotNull(data)
        assertEquals(expectedResponse.orders, data.orders)
        assertNotNull(record)
//        assertEquals(eventType, record.value())
    }

}