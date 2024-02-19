plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "0.1.0.34"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") } // Spigot
    maven { url = uri("https://mvn-repo.arim.space/lesser-gpl3/") } // MorePaperLib
    maven { url = uri("https://repo.opencollab.dev/main/") } // Floodgate
    maven { url = uri("https://repo.smrt-1.com/releases") } // ChatColorHandler
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }
    implementation("me.dave:ChatColorHandler:v2.5.3")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("space.arim.morepaperlib:morepaperlib:0.4.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("space.arim", "me.dave.platyutils.libraries.paperlib")
        relocate("me.dave.chatcolorhandler", "me.dave.platyutils.libraries.chatcolor")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "smrt1Releases"
            url = uri("https://repo.smrt-1.com/releases")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "smrt1Snapshots"
            url = uri("https://repo.smrt-1.com/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}