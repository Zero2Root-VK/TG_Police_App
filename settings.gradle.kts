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
    }
}

rootProject.name = "TGP"

include(":app")
include(":core:model")
include(":core:designsystem")
include(":core:common")
include(":core:database")
include(":core:network")
include(":core:datastore")
include(":core:domain")
include(":core:data")
include(":feature:exam")
include(":feature:catalog")
include(":feature:home")
include(":feature:auth")
include(":feature:currentaffairs")
include(":feature:admin")
