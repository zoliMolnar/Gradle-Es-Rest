package org.zmolnar.rest.controllers

import org.zmolnar.rest.model.Description
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class DescriptionRestController {

    val counter = AtomicLong()

    @GetMapping("/descriptions")
    fun getDescriptions(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Description(counter.incrementAndGet(), "Hello, $name")
}