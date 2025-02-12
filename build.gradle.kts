plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    java
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven("https://maven.noxcrew.com/public")
    maven("https://maven.enginehub.org/repo/")
    maven("https://maven.terraformersmc.com/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")!!
    include(implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")!!)
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    modCompileOnly("com.noxcrew.noxesium:fabric:${property("noxesium_version")}")
    modImplementation(include("net.kyori:adventure-platform-fabric:${property("adventure_version")}")!!)
    modImplementation(include("org.incendo:cloud-fabric:${property("cloud_version")}")!!)
    modImplementation(include("org.incendo:cloud-annotations:2.0.0")!!)
    modImplementation(include("org.incendo:cloud-kotlin-coroutines-annotations:2.0.0")!!)
    modImplementation(include("org.incendo:cloud-kotlin-extensions:2.0.0")!!)
}

tasks {

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

// configure the maven publication
