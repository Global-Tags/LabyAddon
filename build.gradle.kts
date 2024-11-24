plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")
group = "org.example"
version = providers.environmentVariable("VERSION").getOrElse("1.3.4")

labyMod {
    defaultPackageName = "com.rappytv.globaltags" //change this to your main package name (used by all modules)
    addonInfo {
        namespace = "globaltags"
        displayName = "GlobalTags"
        author = "RappyTV"
        description = "Get yourself a custom GlobalTag that's publicly visible to anyone using this addon."
        minecraftVersion = "*"
        version = rootProject.version.toString()
    }

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    devLogin = true
                }
            }
        }
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version

    repositories {
        maven("https://repo.rappytv.com/public/")
    }
}
