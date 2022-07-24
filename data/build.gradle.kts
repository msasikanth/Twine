import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.devtools.ksp")
}

android {
  namespace = "dev.sasikanth.twine.data"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testInstrumentationRunner = "dev.sasikanth.twine.common.testing.di.TwineTestRunner"
    consumerProguardFiles("consumer-rules.pro")

    val testTwineBearerToken = System.getenv("TWINE_DEV_BEARER_TOKEN")
    buildConfigField("String", "TEST_TWINE_BEARER_TOKEN", "\"$testTwineBearerToken\"")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}

kapt {
  correctErrorTypes = true
}

dependencies {
  implementation(projects.common)

  implementation(libs.androidx.core)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.bundles.retrofit)
  implementation(libs.moshi)
  ksp(libs.moshi.codegen)

  androidTestImplementation(projects.commonTesting)
  kaptAndroidTest(libs.hilt.compiler)
  androidTestImplementation(libs.truth)

  coreLibraryDesugaring(libs.desugar.jdk.libs)
}