plugins {
    java
    application
}

group = "com.ubo.tp.message"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Core Java libraries
    implementation("com.google.guava:guava:32.1.2-jre")

    // Swing UI dependencies (if needed)
    implementation("com.formdev:flatlaf:3.2.1")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Testing dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

application {
    mainClass.set("com.ubo.tp.message.MessageAppLauncher")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.ubo.tp.message.MessageAppLauncher"
        )
    }

    // Create a fat jar with all dependencies
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}