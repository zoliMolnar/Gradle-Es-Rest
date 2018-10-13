package org.zmolnar.rest.entry

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.zmolnar.rest.controllers.DescriptionRestController

@EnableAutoConfiguration
@Configuration
@ComponentScan("org.zmolnar.rest.config.ElasticSearchConfig")
@SpringBootApplication
class ApplicationEntry {

    @Bean
    fun descriptionController() = DescriptionRestController()
}

fun  main(args: Array<String>) {
    SpringApplication.run(ApplicationEntry::class.java, *args)
}


