package id.co.vantablack.order

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.math.RoundingMode

data class OrderRequestItem(
    @param:JsonProperty("menuId") val menuId: String,
    @param:JsonProperty("count") val count: Int,
    @param:JsonProperty("price") val price: BigDecimal
) {
    @JsonIgnore
    val normalizedPrice: BigDecimal = price.setScale(2, RoundingMode.CEILING)
}

data class OrderRequest(
    @param:JsonProperty("orders") val orders: Collection<OrderRequestItem>
) {
    @JsonIgnore
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
