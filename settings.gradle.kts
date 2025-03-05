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
include(":features:collection")
include(":features:droplist")
include(":features:alarm")
include(":features:core")
include(":design-compose")
include(":design-ui")
include(":features:symbol")
include(":features:simulator")
include(":features:home")
include(":features:auth")
include(":features:info")
include(":data:datasource")
include(":data:repository")
