package id.co.vantablack.order

import java.math.BigDecimal
import java.math.RoundingMode

data class OrderRequestItem(
    val menuId: String,
    val count: Int,
    val price: BigDecimal
) {
    val normalizedPrice: BigDecimal = price.setScale(2, RoundingMode.CEILING)
}

data class OrderRequest(
    val orders: Collection<OrderRequestItem>
) {
    val totalPayment = orders.sumOf { it.normalizedPrice }
}

data class OrderResponse(
    val id: String,
    val orders: Collection<OrderRequestItem>
) {
    companion object {
        fun fromOrder(order: Order): OrderResponse {
            return OrderResponse(
                id = order.id!!,
                orders = order.orders
            )
        }
    }
}
