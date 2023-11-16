import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
}

group = "net.botwithus.example"
version = "1.0-SNAPSHOT"

configurations {
    create("includeInJar") {
        this.isTransitive = false
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from("build/libs/")
    into("${System.getProperty("user.home")}\\BotWithUs\\scripts\\local\\")
    include("*.jar")
}

tasks.named<Jar>("jar") {
    from({
        configurations["includeInJar"].map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations["compileClasspath"].filter { it.name.startsWith("kotlin-stdlib") }.map { zipTree(it) })
    finalizedBy(copyJar)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        setUrl("https://nexus.botwithus.net/repository/maven-snapshots/")
    }
}

dependencies {
    implementation ("net.botwithus.rs3:botwithus-api:1.0.0-20231112.012705-1")
    implementation("net.botwithus.xapi.public:botwithusx-api:1.0.0-20231113.032104-1")
    "includeInJar"("net.botwithus.xapi.public:botwithusx-api:1.0.0-20231113.032104-1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "20"
}