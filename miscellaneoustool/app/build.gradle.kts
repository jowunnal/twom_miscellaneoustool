import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("jinProject.android.application")
    id("jinProject.android.safeArgs")
}

android {
    namespace = "com.jinproject.twomillustratedbook"

    defaultConfig {
        applicationId = "com.jinproject.twomillustratedbook"
        targetSdk = 34
        versionCode = 61
        versionName = "2.3.3"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["ADMOB_APP_ID"] = getLocalKey("adMob.test.appId")
            buildConfigField("String","ADMOB_REWARD_ID",getLocalKey("adMob.test.rewardId"))
            buildConfigField("String", "ADMOB_UNIT_ID", getLocalKey("adMob.test.unitId"))
        }
        release {
            isMinifyEnabled = false
            manifestPlaceholders["ADMOB_APP_ID"] = getLocalKey("adMob.real.appId")
            buildConfigField("String","ADMOB_REWARD_ID",getLocalKey("adMob.real.rewardId"))
            buildConfigField("String", "ADMOB_UNIT_ID", getLocalKey("adMob.real.unitId"))
        }
    }
}

fun getLocalKey(propertyKey:String):String{
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":features:collection"))
    implementation(project(":features:droplist"))
    implementation(project(":features:alarm"))
    implementation(project(":features:core"))
    implementation(project(":features:symbol"))
    implementation(project(":features:simulator"))
    implementation(project(":design-compose"))
    implementation(project(":design-ui"))
    implementation(project(":features:home"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.compose)

    implementation(libs.bundles.navigation)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.bundles.lifecycle)

    implementation(libs.bundles.billing)
    implementation(libs.bundles.playInAppUpdate)
}