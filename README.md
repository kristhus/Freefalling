# Freefalling
Oppsett av prosjektet og Gradle. 

Hvis det ikke funker å bare klone prosjektet, prøv følgende; 

1. Slett prosjektet.
2. Klone bare Develop branchen: git clone -b Develop https://github.com/kristhus/Freefalling.git
3. Hvis det blir nemnt noe om Gradle etter prosjektet er åpnet i Android Studio, velg "add root" i stedet for configure eller ignore. 
4. Legg til følgende filer under Freefalling i explorer: build.gradle, settings.gradle
5. Legg til build.gradle under /app
6. Gå til file --> Invalidate Caches / restart

Dette burde funke. 

NB: Det er to ulike build.gradle filer, men begge må hete build.gradle for at Gradle skal skjønne at de skal kjøres. 








Filer under Freefalling: 

build.gradle: 


// Top-level build file where you can add configuration options common to all sub-projects/modules.



buildscript {

    repositories {

        jcenter()

    }

    dependencies {



        classpath 'com.android.tools.build:gradle:2.2.3'



        // NOTE: Do not place your application dependencies here; they belong

        // in the individual module build.gradle files

    }

}



allprojects {

    repositories {

        jcenter()

    }

}


settings.gradle:

include ':app'













Filer under /app: 

apply plugin: 'com.android.application'



android {

    compileSdkVersion 23

    buildToolsVersion '25.0.0'



    defaultConfig {

        applicationId "com.pa_gruppe11.freefalling"

        minSdkVersion 15

        targetSdkVersion 23

        versionCode 1

        versionName "1.0"

    }

    buildTypes {

        release {

            minifyEnabled false

            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

        }

    }

}



dependencies {

    compile fileTree(dir: 'libs', include: ['*.jar'])

    compile 'com.android.support:appcompat-v7:23.0.1'

}
