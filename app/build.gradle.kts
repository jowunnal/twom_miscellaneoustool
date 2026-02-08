import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("jinProject.android.application")
}

android {
    namespace = "com.jinproject.twomillustratedbook"

    defaultConfig {
        applicationId = "com.jinproject.twomillustratedbook"
        targetSdk = 36
        versionCode = 89
        versionName = "2.5.2"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["ADMOB_APP_ID"] = getLocalKey("adMob.test.appId")
            buildConfigField("String","ADMOB_REWARD_ID",getLocalKey("adMob.test.rewardId"))
            buildConfigField("String", "ADMOB_UNIT_ID", getLocalKey("adMob.test.unitId"))
            buildConfigField("Boolean", "IS_DEBUG_MODE", "true")
            extra.set("alwaysUpdateBuildId", true)
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["ADMOB_APP_ID"] = getLocalKey("adMob.real.appId")
            buildConfigField("String","ADMOB_REWARD_ID",getLocalKey("adMob.real.rewardId"))
            buildConfigField("String", "ADMOB_UNIT_ID", getLocalKey("adMob.real.unitId"))
            buildConfigField("Boolean", "IS_DEBUG_MODE", "false")
        }
    }
}

fun getLocalKey(propertyKey:String):String{
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation(project(":data:datasource"))
    implementation(project(":features:collection"))
    implementation(project(":features:droplist"))
    implementation(project(":features:alarm"))
    implementation(project(":features:core"))
    implementation(project(":features:symbol"))
    implementation(project(":features:simulator"))
    implementation(project(":design-compose"))
    implementation(project(":features:home"))
    implementation(project(":features:auth"))
    implementation(project(":features:info"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.compose)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.bundles.lifecycle)

    implementation(libs.bundles.billing)
    implementation(libs.bundles.playInAppUpdate)
    implementation(libs.bundles.square)
}