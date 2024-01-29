plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
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
            proguardFiles("proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("com.android.billingclient:billing-ktx:6.1.0")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.QuantumGrove-Tech"
            artifactId = "Quantum-IAP"
            version = "1.0.2"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}