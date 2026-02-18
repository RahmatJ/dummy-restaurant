package id.co.vantablack.order

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class Controller {

    @PostMapping
    fun submitOrder(
        @RequestBody orderRequest: OrderRequest,
    ): ResponseEntity<OrderResponse> {
        val response = OrderResponse(orderRequest.orders)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response)
    }

}