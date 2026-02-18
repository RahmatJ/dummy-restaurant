package id.co.vantablack.order

import id.co.vantablack.order.utility.next
import id.co.vantablack.order.utility.random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mongodb.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import kotlin.jvm.optionals.getOrNull
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureRestTestClient
class ControllerTest {

    @LocalServerPort
    var port: Int = 0

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mongo = MongoDBContainer(DockerImageName.parse("mongo:8.0"))
    }

    @Autowired
    lateinit var client: RestTestClient

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should return 200`() {
        val request = random.next<OrderRequest>()
        val expectedResponse = random.next<OrderResponse>().copy(
            orders = request.orders
        )

        client.post().uri("/order")
            .body(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assertEquals(expectedResponse.orders, body!!.orders)
            }

        val data = orderRepository.findById(expectedResponse.id).getOrNull()

//        TODO(Rahmat): Has error [Conversion to Decimal128 would require inexact rounding of 0] fix this
        assertNotNull(data)
//        assertEquals(expectedResponse.orders, data.getOrNull()!!.orders)
    }

}