plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "com.grupo8.appfinanza"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.grupo8.appfinanza"
        minSdk = 29
        targetSdk = 36
=======
    namespace = "com.grupo8.finanzas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.grupo8.finanzas"
        minSdk = 29
        targetSdk = 34
>>>>>>> 79f4f529f84157666331907e0a426462460ab30b
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
<<<<<<< HEAD
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
=======
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
>>>>>>> 79f4f529f84157666331907e0a426462460ab30b
    }
}

dependencies {
<<<<<<< HEAD
=======

>>>>>>> 79f4f529f84157666331907e0a426462460ab30b
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}