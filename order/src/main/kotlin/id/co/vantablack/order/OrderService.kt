package id.co.vantablack.order

import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val kafkaProducer: KafkaProducer
) {

    fun submitOrder(request: OrderRequest): Order {
        val orderData = Order.fromRequest(request)

        val result = orderRepository.save(orderData)

        val message = MessageBody(
            eventType = "order.created",
            data = request
        )
        kafkaProducer.produce("order",message)

        return result
    }

}