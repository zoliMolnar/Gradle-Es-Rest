package org.zmolnar.rest.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.zmolnar.rest.model.DescriptionIndexDocument
import org.zmolnar.rest.requests.DescriptionSearchRequestBuilder
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@RestController
class DescriptionRestController {

    val counter = AtomicLong()

    @GetMapping("/descriptions")
    fun getDescriptions(
            @RequestParam(value = "typeId", defaultValue = "typeId") typeId: String,
            @RequestParam(value = "term", defaultValue = "term") term: String) : List<DescriptionIndexDocument> {

        return DescriptionSearchRequestBuilder()
                .filterByTerm(term)
                .build()
                .execute(60, TimeUnit.SECONDS)
    }
}