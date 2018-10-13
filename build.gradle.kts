import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sun.misc.ExtensionDependency


buildscript {
    val kotlin_version = "1.2.71"
    val spring_version = "1.5.4.RELEASE"
    val es_version = "6.4.1"

    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$spring_version")
        classpath("org.elasticsearch:elasticsearch:$es_version")
        classpath("org.elasticsearch.client:elasticsearch-rest-client:6.4.1")
        classpath("org.elasticsearch.client:elasticsearch-rest-high-level-client:$es_version")
        classpath("com.google.code.gson:gson:2.8.5")

    }
}

plugins {
    application
    kotlin("jvm") version "1.2.71"
    id("org.jetbrains.kotlin.plugin.spring") version ("1.2.71")
    id("org.springframework.boot") version ("1.5.4.RELEASE")
}

application {
    mainClassName = "org.zmolnar.rest.entry.ApplicationEntryKt"
}

repositories {
    jcenter()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib:1.2.71")
    compile("org.jetbrains.kotlin:kotlin-reflect:1.2.71")
    compile("org.elasticsearch:elasticsearch:6.4.1")
    compile("org.elasticsearch.client:elasticsearch-rest-client:6.4.1")
    compile("org.elasticsearch.client:elasticsearch-rest-high-level-client:6.4.1")
    compile("org.elasticsearch.gradle:build-tools:6.4.1")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    compile("org.springframework.boot:spring-boot-starter-web") {
        exclude("spring-boot-starter-tomcat")
    }

    compile("org.springframework.boot:spring-boot-starter-jetty")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("com.google.code.gson:gson:2.8.5")

}

group = "org.zmolnar.rest"
version  = "0.1.0.SNAPSHOT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
