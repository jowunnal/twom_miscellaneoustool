pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "miscellaneoustool"

include(":app")
include(":domain")
include(":core")
include(":data")
include(":features")
include(":features:collection:api")
include(":features:collection:impl")
include(":features:droplist:api")
include(":features:droplist:impl")
include(":features:alarm:api")
include(":features:alarm:impl")
include(":features:core")
include(":design")
include(":features:symbol:api")
include(":features:symbol:impl")
include(":features:simulator")
include(":features:home")
include(":features:auth:api")
include(":features:auth:impl")
include(":features:info")
include(":data:datasource")
include(":data:repository")
