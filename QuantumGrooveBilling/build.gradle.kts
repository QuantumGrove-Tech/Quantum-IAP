plugins {
    id("maven-publish")
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "quantum.apps.quantumgroovebilling"
    compileSdk = 34

    defaultConfig {
        minSdk = 16
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        jvmToolchain(17)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    publishing {
//        singleVariant("release") {
//            withSourcesJar()
//            withJavadocJar()
//        }
//    }
}

dependencies {
    implementation("com.android.billingclient:billing-ktx:6.1.0")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.QuantumGrove-Tech"
            artifactId = "Quantum-IAP"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}