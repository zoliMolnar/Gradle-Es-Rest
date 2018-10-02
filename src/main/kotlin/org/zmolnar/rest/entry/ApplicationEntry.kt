package org.zmolnar.rest.entry

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zmolnar.rest.controllers.DescriptionRestController

@EnableAutoConfiguration
@Configuration
class ApplicationEntry {

    @Bean
    fun descriptionController() = DescriptionRestController()

}

fun  main(args: Array<String>) {

    SpringApplication.run(ApplicationEntry::class.java, *args)
}


