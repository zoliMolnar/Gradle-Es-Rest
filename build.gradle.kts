import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sun.misc.ExtensionDependency


buildscript {
    val kotlin_version = "1.2.71"
    val spring_version = "1.5.4.RELEASE"

    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$spring_version")
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
    compile("org.springframework.boot:spring-boot-starter-web")
    testCompile("org.springframework.boot:spring-boot-starter-test")
}

group = "org.zmolnar.rest"
version  = "0.1.0.SNAPSHOT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
