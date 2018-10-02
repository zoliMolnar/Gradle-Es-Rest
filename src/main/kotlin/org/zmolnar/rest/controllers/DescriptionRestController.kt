package org.zmolnar.rest.controllers

import org.zmolnar.rest.model.DescriptionIndexDocument
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class DescriptionRestController {

    val counter = AtomicLong()

    @GetMapping("/descriptions")
    fun getDescriptions(@RequestParam(value = "typeId", defaultValue = "typeId") typeId: String, @RequestParam(value = "term", defaultValue = "term") term: String) : DescriptionIndexDocument {
        return DescriptionIndexDocument("id", 10, true, "moduleId", "conceptId", "languageCode", typeId, term, "caseSignificanceId")
    }
}