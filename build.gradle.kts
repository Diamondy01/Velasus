plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val plugin = "Velasus"
val author = "Diamondy01"
val desc = "Proxy Plugin"
val main = "me.diamondy.velasus.Velasus"
val version = "1.0"
val id = "velasus" // Ensure this matches the expected plugin ID format

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://mvnrepository.com/artifact/org.yaml/snakeyaml") }
    maven { url = uri("https://mvnrepository.com/artifact/com.google.guava/guava") }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.108.Final")
    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly("net.kyori:adventure-api:4.17.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.google.guava:guava:33.2.1-jre")
}

tasks {
    named<ProcessResources>("processResources") {
        filesMatching("**/*.properties") {
            expand(
                "id" to id,
                "name" to plugin,
                "author" to author,
                "main" to main,
                "description" to desc,
                "version" to version
            )
        }
    }

    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
    }

    register<Copy>("copy") {
        from(named("shadowJar"))
        rename("(.*)-all.jar", "$plugin-$version.jar")
        into(file("jars"))
    }

    register("delete") {
        doLast { file("jars").deleteRecursively() }
    }
}