plugins {
    kotlin("jvm") version "1.4.0-rc"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "com.github.gitantoinee.deobfuscator.radon"
version = "1.0.0"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.ow2.asm:asm:9.0-beta")
}

kotlin {
    explicitApi()

    target.compilations.forEach { compilation ->
        compilation.kotlinOptions.jvmTarget = "1.8"
        compilation.kotlinOptions.allWarningsAsErrors = true
    }
}

tasks {
    shadowJar {
        manifest.attributes["Main-Class"] = "com.github.gitantoinee.deobfuscator.radon.MainKt"
    }
}
