plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val plugin = "Velasus"
val author = "Diamondy01"
val desc = "Proxy Plugin"
val main = "me.diamondy.velasus.Velasus"
val version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://mvnrepository.com/artifact/io.netty/netty-all") }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.24.Final")
    compileOnly("org.projectlombok:lombok:1.18.20")
}

tasks {
    named<ProcessResources>("processResources") {
        expand(
            "id" to plugin.lowercase(),
            "name" to plugin,
            "author" to author,
            "main" to main,
            "description" to desc,
            "version" to version
        )
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