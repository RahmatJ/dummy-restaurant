package id.co.vantablack.order.utility

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import kotlin.random.Random

val random = EasyRandom(
    EasyRandomParameters()
        .stringLengthRange(10, 20)
        .seed(Random.nextLong()),
)

inline fun <reified T : Any> EasyRandom.next(): T = this.nextObject(T::class.java)
