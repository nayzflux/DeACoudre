plugins {
    id("java")
}

group = "fr.nayz.deacoudre"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation(files("C:/Users/ninob/Documents/Minecraft/de-a-coudre/plugins/GameAPI-1.0-SNAPSHOT.jar"))
}

task("copy") {
    copy {
        from("./build/libs/DeACoudre-1.0-SNAPSHOT.jar")
        into("C:/Users/ninob/Documents/Minecraft/de-a-coudre/plugins")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}