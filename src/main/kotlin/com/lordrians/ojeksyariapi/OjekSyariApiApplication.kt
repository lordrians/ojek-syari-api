package com.lordrians.ojeksyariapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OjekSyariApiApplication

fun main(args: Array<String>) {
//    mongodb+srv://lordrians:<password>@cluster0.4iodyi8.mongodb.net/?retryWrites=true&w=majority
    runApplication<OjekSyariApiApplication>(*args)
}
