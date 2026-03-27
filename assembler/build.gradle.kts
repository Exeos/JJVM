plugins {
    id("java")
}

dependencies {
    implementation(project(":shared"))
    implementation("org.apache.commons:commons-lang3:3.20.0")
}

repositories {
    mavenCentral()
}