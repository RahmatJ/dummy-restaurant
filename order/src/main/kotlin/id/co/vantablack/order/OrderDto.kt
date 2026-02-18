package id.co.vantablack.order

import java.math.BigDecimal

data class OrderRequestItem(
    val menuId: String,
    val count: Int,
    val price: BigDecimal
)

data class OrderRequest(
    val orders: Collection<OrderRequestItem>
) {
    val totalPayment = orders.sumOf { it.price }
}

data class OrderResponse(
    val orders: Collection<OrderRequestItem>
)
