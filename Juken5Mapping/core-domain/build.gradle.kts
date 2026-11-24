plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":core-protocol"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
