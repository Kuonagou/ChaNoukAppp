plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
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

// Définition de la version JavaFX à utiliser
val javafxVersion = "21.0.1"

dependencies {
    // JavaFX dependencies - ajoutées explicitement sans le plugin
    implementation("org.openjfx:javafx-base:$javafxVersion")
    implementation("org.openjfx:javafx-controls:$javafxVersion")
    implementation("org.openjfx:javafx-fxml:$javafxVersion")
    implementation("org.openjfx:javafx-graphics:$javafxVersion")

    // Core Java libraries
    implementation("com.google.guava:guava:32.1.2-jre")

    // Swing UI dependencies
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
}

// Configuration des ressources
sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
            srcDir("src/main/java")  // Pour inclure les fichiers de ressources dans les packages Java
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

// Configuration des tests
tasks.test {
    useJUnitPlatform()
}

// Configuration de la compilation Java
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Configuration du Shadow JAR pour créer un JAR avec toutes les dépendances, y compris JavaFX
tasks.shadowJar {
    archiveClassifier.set("") // Remplace le JAR standard
    archiveVersion.set("") // Pas de version dans le nom du fichier
    manifest {
        attributes(
            "Main-Class" to "com.ubo.tp.message.MessageAppLauncher"
        )
    }
    // S'assurer que tous les modules JavaFX sont inclus
    mergeServiceFiles()
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

// Tâche pour exécuter l'application avec JavaFX
tasks.register<JavaExec>("runWithJavaFX") {
    group = "application"
    description = "Runs the application with JavaFX"
    mainClass.set(application.mainClass)
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "--module-path", configurations.runtimeClasspath.get().asPath,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics"
    )
}