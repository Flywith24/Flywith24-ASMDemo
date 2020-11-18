plugins {
    `kotlin-dsl`
    java
    maven
    `maven-publish`
}
dependencies {

    implementation(gradleApi())
    implementation(localGroovy())
    implementation("com.android.tools.build:gradle:4.1.1")

    implementation("org.ow2.asm:asm:7.1")
    implementation("org.ow2.asm:asm-commons:7.1")
}

group = "cn.flywith24.plugin"
version = "1.0.0"

publishing {
    publications {
        repositories { maven { url = uri("../asm_lifecycle_repo") } }
    }
}
