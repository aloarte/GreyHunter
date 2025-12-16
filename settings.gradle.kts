pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenCentral()
    }
}

rootProject.name = "GreyHunter"
include(":app")
include(":common:data")
include(":common:domain")
include(":common:framework")
include(":feature:home")
include(":feature:projectdetail")
include(":feature:minidetail")
include(":feature:createproject")
include(":feature:createminiature")
