plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jackson.jsonDatabind)
    implementation(libs.spel)
    implementation(libs.spring.security)
}
