package id.co.vantablack.order

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class Controller(
    private val orderRepository: OrderRepository
) {

    @PostMapping
    fun submitOrder(
        @RequestBody orderRequest: OrderRequest,
    ): ResponseEntity<OrderResponse> {
        val orderData = Order.fromRequest(orderRequest)

        val response = orderRepository.save(orderData)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(OrderResponse.fromOrder(response))
    }

}