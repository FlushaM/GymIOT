buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://api.mapbox.com/downloads/v2/releases/maven") {
            credentials {
                username = "mapbox"
                password = "pk.eyJ1IjoibWF0aWFzaGRnIiwiYSI6ImNtMmNncWdoMjE0bzQya24xa3J3OWs5a3QifQ.Y9F-Fg4-jFDfBoe6Bj67Lw"
            }
        }
    }
}

