import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "ru.steklopod"
description = " API for money transfers between accounts "

repositories { mavenCentral(); jcenter() }

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")

    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("com.sparkjava:spark-core:2.8.0")
    implementation("com.google.code.gson:gson:2.8.5")

    implementation("com.h2database:h2:1.4.198")
    implementation("org.jetbrains.exposed:exposed:0.13.2")
    implementation("com.zaxxer:HikariCP:3.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")
}


tasks {
    withType<KotlinCompile> { kotlinOptions { jvmTarget = "1.8" } }

    withType<ShadowJar> { manifest { attributes(mapOf("Main-Class" to "ru.steklopod.Main")) } }
    
    build { dependsOn(shadowJar) }
}
