package org.zmolnar.rest.entry

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApplicationEntry

fun  main(args: Array<String>) {
    SpringApplication.run(ApplicationEntry::class.java, *args)
}


