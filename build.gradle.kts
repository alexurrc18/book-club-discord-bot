plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.alexandruc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:6.5.0")
    implementation("org.jsoup:jsoup:1.23.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.alexandruc.bookclub.Main"
    }
}

tasks.shadowJar {
    archiveBaseName.set("bookclub-bot")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.test {
    useJUnitPlatform()
}