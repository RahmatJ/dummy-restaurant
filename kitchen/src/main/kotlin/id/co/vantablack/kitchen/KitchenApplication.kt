package id.co.vantablack.kitchen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KitchenApplication

fun main(args: Array<String>) {
    runApplication<KitchenApplication>(*args)
}