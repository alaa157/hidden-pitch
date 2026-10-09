import java.io.File
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.isFile) localPropertiesFile.inputStream().use { localProperties.load(it) }

fun buildConfigString(value: String): String {
    val slash = 92.toChar().toString()
    val escaped = value.replace(slash, slash + slash).replace("\"", slash + "\"")
    return "\"" + escaped + "\""
}

android {
    namespace = "hiddenpitch"
    compileSdk = 37
    defaultConfig {
        applicationId = "hiddenpitch"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "SUPABASE_URL", buildConfigString(localProperties.getProperty("SUPABASE_URL", "")))
        buildConfigField("String", "SUPABASE_ANON_KEY", buildConfigString(localProperties.getProperty("SUPABASE_ANON_KEY", "")))
    }
    androidResources { localeFilters += listOf("en", "ar") }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        debug { isMinifyEnabled = false }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    lint {
        abortOnError = true
        checkReleaseBuilds = true
        error += listOf("HardcodedText", "RtlHardcoded", "RtlSymmetry", "RtlEnabled", "MissingTranslation", "ExtraTranslation")
    }
}

kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)
    implementation(libs.activity.compose)
    implementation(libs.appcompat)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.coroutines.android)
    testImplementation(libs.junit)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}

val verifyStringResourceParity = tasks.register("verifyStringResourceParity") {
    val englishFile = layout.projectDirectory.file("src/main/res/values/strings.xml")
    val arabicFile = layout.projectDirectory.file("src/main/res/values-ar/strings.xml")
    inputs.files(englishFile, arabicFile)
    doLast {
        fun resourceKeys(file: File): Set<String> {
            val factory = DocumentBuilderFactory.newInstance()
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            val root = factory.newDocumentBuilder().parse(file).documentElement
            val keys = linkedSetOf<String>()
            val children = root.childNodes
            for (index in 0 until children.length) {
                val node = children.item(index)
                if (node.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                    val name = node.attributes?.getNamedItem("name")?.nodeValue
                    if (name != null) keys.add(node.nodeName + ":" + name)
                }
            }
            return keys
        }
        val englishKeys = resourceKeys(englishFile.asFile)
        val arabicKeys = resourceKeys(arabicFile.asFile)
        check(englishKeys == arabicKeys) {
            "English/Arabic resource keys differ. English only: " + (englishKeys - arabicKeys) +
                "; Arabic only: " + (arabicKeys - englishKeys)
        }
    }
}

tasks.named("preBuild").configure { dependsOn(verifyStringResourceParity) }
tasks.named("check").configure { dependsOn(verifyStringResourceParity) }
