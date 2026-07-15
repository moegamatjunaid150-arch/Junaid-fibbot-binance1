plugins {
    id("com.android.application") version "8.1.0" apply false
    kotlin("android") version "1.9.0" apply false
    kotlin("kapt") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}