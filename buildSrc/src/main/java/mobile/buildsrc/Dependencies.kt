package mobile.buildsrc

object Versions {
    val ktlint = "0.29.0"
}

object GradlePlugins {
    val androidGradlePlugin = "com.android.tools.build:gradle:3.5.3"
}

object Libs {

//    val cameraIntegrator = "com.github.Him-khati:camera_intergrator:0.3.3"
    val cameraIntegrator = "com.github.vinnersafterwork:camera_intergrator:0.3.3"
    val timber = "com.jakewharton.timber:timber:4.7.1"
    val junit = "junit:junit:4.12"

    val progressButton = "com.github.razir.progressbutton:progressbutton:2.1.0"

    val mockitoCore = "org.mockito:mockito-inline:3.0.0"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
    val mockitoInline = "org.mockito:mockito-inline:3.0.0"

    val mpChart = "com.github.PhilJay:MPAndroidChart:v3.1.0"
    val circularImageView = "de.hdodenhof:circleimageview:3.1.0"
    val appIntro = "com.github.AppIntro:AppIntro:5.1.0"
    val zoomableImageView = "com.jsibbold:zoomage:1.3.1"
    val leakCanary2 = "com.squareup.leakcanary:leakcanary-android:2.3"
    val coil = "io.coil-kt:coil:0.11.0"

    object Vinners {
        val logger = "com.github.vinnersafterwork:core:1.0"
    }

    object Firebase {

        val firebaseBom = "com.google.firebase:firebase-bom:27.0.0"
        val firebaseCore = "com.google.firebase:firebase-core:17.4.1"
        val analyticsKtx = "com.google.firebase:firebase-analytics-ktx"
        val crashlyticsKtx = "com.google.firebase:firebase-crashlytics-ktx"
        val firebaseMessagingKtx = "com.google.firebase:firebase-messaging-ktx"
        val dynamicLinksKtx =   "com.google.firebase:firebase-dynamic-links-ktx"
        val inAppMessagingKtx = "com.google.firebase:firebase-inappmessaging-display-ktx"

        val crashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:2.4.1"
    }

    object Google {

        val material = "com.google.android.material:material:1.1.0"
        val gmsGoogleServicesGradlePlugin = "com.google.gms:google-services:4.3.3"
        val placesLibrary = "com.google.android.libraries.places:places:1.0.0"
        val maps = "com.google.android.gms:play-services-maps:16.1.0"

        val playCore = "com.google.android.play:core:1.7.2"
        val playLocation = "com.google.android.gms:play-services-location:16.0.0"
        val playRefer = "com.android.installreferrer:installreferrer:1.1.2"
        val playServicesBase = "com.google.android.gms:play-services-base:17.0.0"
        val authApiPhone = "com.google.android.gms:play-services-auth-api-phone:17.0.0"
        val auth = "com.google.android.gms:play-services-auth:18.0.0"
        val smsRetriver = "com.google.android.gms:play-services-gcm:17.0.0"
    }

    object Kotlin {
        private const val version = "1.4.0"
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.3.6"
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        val appcompat = "androidx.appcompat:appcompat:1.1.0"
        val browser = "androidx.browser:browser:1.0.0"
        val palette = "androidx.palette:palette:1.0.0"
        val recyclerview = "androidx.recyclerview:recyclerview:1.0.0"
        val emoji = "androidx.emoji:emoji:1.0.0"
        val fragment = "androidx.fragment:fragment:1.0.0"
        val multiDex = "androidx.multidex:multidex:2.0.1"
        val vectorDrawables = "androidx.vectordrawable:vectordrawable:1.1.0"
        val preference = "androidx.preference:preference:1.1.0-alpha02"
        val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        val coreKtx = "androidx.core:core-ktx:1.2.0"
        val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"

        object Test {
            val core = "androidx.test:core:1.1.0"
            val runner = "androidx.test:runner:1.1.1"
            val rules = "androidx.test:rules:1.1.1"
            val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }

        object Navigation {
            private const val version = "2.2.0"
            val navigationFragment = "androidx.navigation:navigation-fragment:$version"
            val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            val navigationUi = "androidx.navigation:navigation-ui:$version"
            val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Paging {
            private const val version = "2.1.0"
            val common = "androidx.paging:paging-common:$version"
            val runtime = "androidx.paging:paging-runtime-ktx:$version"
            val rxjava2 = "androidx.paging:paging-rxjava2-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
            val lifeCyleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            val lifeCyleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            val lifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.2.5"
            val roomKtx = "androidx.room:room-ktx:$version"
            val common = "androidx.room:room-common:$version"
            val runtime = "androidx.room:room-runtime:$version"
            val rxjava2 = "androidx.room:room-rxjava2:$version"
            val compiler = "androidx.room:room-compiler:$version"
        }

        object Work {
            private const val version = "2.2.0"
            val workManager = "androidx.work:work-runtime-ktx:$version"
            val workManageRxJavaSupport = "androidx.work:work-rxjava2:$version"
        }

        object Security{
            val securityCrypto =  "androidx.security:security-crypto:1.0.0-rc02"
        }
    }

    object AirBnb {
        val lottie = "com.airbnb.android:lottie:3.0.7"
    }
    object Facebook {
        val stetho = "com.facebook.stetho:stetho:1.5.1"
    }

    object RxJava {
        val rxJava = "io.reactivex.rxjava2:rxjava:2.2.12"
        val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
        val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        private const val version = "2.27"
        val dagger = "com.google.dagger:dagger:$version"
        val androidSupport = "com.google.dagger:dagger-android-support:$version"
        val compiler = "com.google.dagger:dagger-compiler:$version"
        val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Glide {
        private const val version = "4.12.0"
        val glide = "com.github.bumptech.glide:glide:$version"
        val compiler = "com.github.bumptech.glide:compiler:$version"
    }


    object Square {

        object OkHttp {
            val okhttpBom = "com.squareup.okhttp3:okhttp-bom:4.9.1"

            val okHttp = "com.squareup.okhttp3:okhttp"
            val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor"
        }

        object Okio {
            val okIo = "com.squareup.okio:okio:2.2.2"
        }

        object Retrofit {
            private const val version = "2.9.0"
            val retrofit = "com.squareup.retrofit2:retrofit:$version"
            val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
            val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
            val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
        }
    }

    object Gson {
        val gson = "com.google.code.gson:gson:2.8.5"
    }

    object AssistedInject {
        private const val version = "0.4.0"
        val annotationDagger2 = "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
        val processorDagger2 = "com.squareup.inject:assisted-inject-processor-dagger2:$version"
    }
}
