buildscript {
    val sqlDelightVersion: String by project
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.2.0-alpha16")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}
group = "com.kobasato.kmmbrainfuck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
