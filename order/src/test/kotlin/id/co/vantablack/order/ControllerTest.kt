package id.co.vantablack.order

import id.co.vantablack.order.utility.next
import id.co.vantablack.order.utility.random
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureRestTestClient
class ControllerTest {

    @Autowired
    lateinit var client: RestTestClient

    @Test
    fun `should return 200`() {
        val request = random.next<OrderRequest>()
        val expectedResponse = OrderResponse(request.orders)

        client.post().uri("/order")
            .body(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assertEquals(expectedResponse, body)
            }
    }

}