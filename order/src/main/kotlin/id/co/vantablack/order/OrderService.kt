package id.co.vantablack.order

import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun submitOrder(request: OrderRequest): Order {
        val orderData = Order.fromRequest(request)

        return orderRepository.save(orderData)
    }

}