plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
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
    implementation("org.openjfx:javafx-controls:21.0.1")
    implementation("org.openjfx:javafx-fxml:21.0.1")
    implementation("org.openjfx:javafx-swing:21.0.1")

    // Autres dépendances de votre projet
    implementation("com.google.guava:guava:32.1.2-jre")
}

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.swing")
}

application {
    mainClass.set("com.ubo.tp.message.MessageAppLauncher")
}

// Configuration pour activer/désactiver le mode mock
val isMockEnabled = project.findProperty("mockMode")?.toString()?.toBoolean() ?: false

tasks.named<JavaExec>("run") {
    // Passer le mode mock comme propriété système
    systemProperty("mock.mode", isMockEnabled)
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Main-Class" to "com.ubo.tp.message.MessageAppLauncher",
            "Class-Path" to configurations.runtimeClasspath.get().files.map { it.name }.joinToString(" ")
        ))
    }

    // Inclure toutes les dépendances dans le jar
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })

    // Inclure explicitement les sources et ressources
    from(sourceSets.main.get().output)

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Tâche pour créer un fat JAR avec toutes les dépendances
tasks.register<Jar>("fatJar") {
    archiveBaseName.set(project.name + "-all")

    manifest {
        attributes["Main-Class"] = "com.ubo.tp.message.MessageAppLauncher"
    }

    // Inclure toutes les dépendances
    from(sourceSets.main.get().output)
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Ajouter le fatJar à la construction
tasks.build {
    dependsOn(tasks.named("fatJar"))
}

// Exclure les fichiers de signature pour éviter les conflits
tasks.withType<Jar> {
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

// Configuration du wrapper Gradle
tasks.wrapper {
    gradleVersion = "8.5"
}