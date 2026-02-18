package id.co.vantablack.order

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.math.BigDecimal
import java.time.Instant

@Document
data class Order(
    @Id
    val id: String? = null,
    val orders: Collection<OrderRequestItem>,
    val totalPayment: BigDecimal,

    @CreatedDate
    val createdAt: Instant? = null,
    @LastModifiedDate
    val updatedAt: Instant? = null
) {
    companion object {
        fun fromRequest(request: OrderRequest): Order {
            return Order(
                orders = request.orders,
                totalPayment = request.totalPayment
            )
        }
    }
}


interface OrderRepository: MongoRepository<Order, String>
