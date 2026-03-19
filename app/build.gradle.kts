plugins {
    application
}

group = "me.exeos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":assembler"))
    implementation(project(":vm"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "me.exeos.jjvm.app.Main"
}