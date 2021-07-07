/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.time.Year
import java.time.format.DateTimeFormatter

// Minecraft, Mappings, Loader Versions
val minecraftVersion       = project.property("minecraft.version").toString()
val yarnBuild              = project.property("yarn.build").toString()
val loaderVersion          = project.property("loader.version").toString()

// Mod Info
val modVersion             = project.property("mod.version").toString()
val modName                = project.property("mod.name").toString()
val modGroup               = project.property("mod.group").toString()

// Dependency Version
val fabricVersion          = project.property("fabric.version").toString()
val trEnergyVersion        = project.property("tr_energy.version").toString()
val lbaVersion             = project.property("lba.version").toString()
val wthitVersion           = project.property("wthit.version").toString()

plugins {
    java
    `maven-publish`
    id("fabric-loom") version("0.8-SNAPSHOT")
    id("org.cadixdev.licenser") version("0.6.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

group = modGroup
version = modVersion + getVersionDecoration()

base {
    archivesBaseName = modName
}

loom {
    refmapName = "galacticraftenergy.refmap.json"
}

repositories {
    mavenLocal()
    maven("https://alexiil.uk/maven/") {
        content {
            includeGroup("alexiil.mc.lib")
        }
    }
    maven("https://bai.jfrog.io/artifactory/maven/") {
        content {
            includeGroup("mcp.mobius.waila")
        }
    }
}

/**
 * From:
 * @see net.fabricmc.loom.util.FabricApiExtension.getDependencyNotation
 */
fun getFabricApiModule(moduleName: String, fabricApiVersion: String): String {
    return String.format("net.fabricmc.fabric-api:%s:%s", moduleName,
        fabricApi.moduleVersion(moduleName, fabricApiVersion))
}

fun optionalImplementation(dependencyNotation: String, dependencyConfiguration: Action<ExternalModuleDependency>) {
    project.dependencies.modCompileOnly(dependencyNotation, dependencyConfiguration)
    project.dependencies.modRuntime(dependencyNotation, dependencyConfiguration)
}

dependencies {
    // Minecraft, Mappings, Loader
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.$yarnBuild:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    // Fabric Api Modules
    listOf(
        "fabric-api-base",
        "fabric-resource-loader-v0"
    ).forEach {
        modImplementation(getFabricApiModule(it, fabricVersion)) { isTransitive = false }
    }

    // Mandatory Dependencies (Included with Jar-In-Jar)
    include(modApi("alexiil.mc.lib:libblockattributes-core:$lbaVersion") { isTransitive = false })
    include(modApi("alexiil.mc.lib:libblockattributes-items:$lbaVersion") { isTransitive = false })
    include(modApi("alexiil.mc.lib:libblockattributes-fluids:$lbaVersion") { isTransitive = false })

    // Optional Dependencies
    optionalImplementation("teamreborn:energy:$trEnergyVersion") { isTransitive = false }
    optionalImplementation("mcp.mobius.waila:wthit:fabric-$wthitVersion") { isTransitive = false }

    // Other Dependencies
    modRuntime("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mutableMapOf("version" to project.version))
    }

    // Minify json resources
    // https://stackoverflow.com/questions/41028030/gradle-minimize-json-resources-in-processresources#41029113
    doLast {
        fileTree(mapOf("dir" to outputs.files.asPath, "includes" to listOf("**/*.json", "**/*.mcmeta"))).forEach {
                file: File -> file.writeText(groovy.json.JsonOutput.toJson(groovy.json.JsonSlurper().parse(file)))
        }
    }
}

java {
    withSourcesJar()
}

tasks.create<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

tasks.jar {
    from("LICENSE")
    manifest {
        attributes(mapOf(
            "Implementation-Title"     to modName,
            "Implementation-Version"   to project.version,
            "Implementation-Vendor"    to "Team Galacticraft",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME,
            "Maven-Artifact"           to "$modGroup:$modName:$project.version"
        ))
    }
}

tasks.withType(JavaCompile::class) {
    dependsOn(tasks.checkLicenses)
    options.encoding = "UTF-8"
    options.release.set(16)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "dev.galacticraft"
            artifactId = "GalacticraftEnergy"

            artifact(tasks.remapJar) { builtBy(tasks.remapJar) }
            artifact(tasks.getByName("sourcesJar", Jar::class)) { builtBy(tasks.remapSourcesJar) }
            artifact(tasks.getByName("javadocJar", Jar::class))
        }
    }
    repositories {
        maven {
            setUrl("s3://maven.galacticraft.dev")
            authentication {
                register("awsIm", AwsImAuthentication::class)
            }
        }
    }
}

license {
    setHeader(project.file("LICENSE_HEADER.txt"))
    include("**/dev/galacticraft/**/*.java")
    include("build.gradle.kts")
    ext {
        set("year", Year.now().value)
        set("company", "Team Galacticraft")
    }
}

// inspired by https://github.com/TerraformersMC/GradleScripts/blob/2.0/ferry.gradle
fun getVersionDecoration(): String {
    if ((System.getenv("DISABLE_VERSION_DECORATION") ?: "false") == "true") return ""
    if (project.hasProperty("release")) return ""

    var version = "+build"
    if ("git".exitValue() != 0) {
        version += ".unknown"
    } else {
        val branch = "git branch --show-current".execute()
        if (branch.isNotEmpty() && branch != "main") {
            version += ".${branch}"
        }
        val commitHashLines = "git rev-parse --short HEAD".execute()
        if (commitHashLines.isNotEmpty()) {
            version += ".${commitHashLines}"
        }
        val dirty = "git diff-index --quiet HEAD".exitValue()
        if (dirty != 0) {
            version += "-modified"
        }
    }
    return version
}

// from https://discuss.gradle.org/t/how-to-run-execute-string-as-a-shell-command-in-kotlin-dsl/32235/9
fun String.execute(): String {
    print(this)
    val output = ByteArrayOutputStream()
    rootProject.exec {
        commandLine(split("\\s".toRegex()))
        workingDir = rootProject.projectDir
        isIgnoreExitValue = true
        standardOutput = output
        errorOutput = OutputStream.nullOutputStream()
    }

    return String(output.toByteArray()).trim()
}

fun String.exitValue(): Int {
    return rootProject.exec {
        commandLine(split("\\s".toRegex()))
        workingDir = rootProject.projectDir
        isIgnoreExitValue = true
        standardOutput = OutputStream.nullOutputStream()
        errorOutput = OutputStream.nullOutputStream()
    }.exitValue
}