package server.web.recolte

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class RecolteApplication

fun main(args: Array<String>) {
    runApplication<RecolteApplication>(*args)
}

@RestController
class HomeController {
    @GetMapping("/")
    fun home(): Map<String, String> = mapOf(
        "name" to "hd-recolte",
        "status" to "up",
    )
}
