import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.21"
  application
}

group = "me.aunthtooaung"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test-junit"))

  implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.8.0")
  implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
  useJUnit()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClassName = "MainKt"
}