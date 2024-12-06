package org.xpathqs.api.delaycontroller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@RestController
class DemoController {
    @GetMapping("/test")
    fun hellow() = "hellow1"

    @GetMapping("/test2")
    fun hellow2() = "hellow2"
}

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
