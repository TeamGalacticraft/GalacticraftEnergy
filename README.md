![Maven metadata URL](https://img.shields.io/maven-metadata/v?logo=Apache%20Maven&metadataUrl=https%3A%2F%2Fmaven.galacticraft.dev%2Fdev%2Fgalacticraft%2FGalacticraftEnergy%2Fmaven-metadata.xml&style=flat-square&logoColor=white)
[![Team Galacticraft Discord](https://img.shields.io/discord/775251052517523467.svg?colorB=5865F2&label=discord&style=flat-square&logo=discord&logoColor=white)](https://discord.gg/n3QqhMYyFK)
[![Issues](https://img.shields.io/github/issues/StellarHorizons/Galacticraft-Energy?style=flat-square&logo=github&logoColor=white)](https://github.com/StellarHorizons/Galacticraft-Energy/issues)
[![GalacticraftDev Twitch](https://img.shields.io/twitch/status/galacticraftdev.svg?style=flat-square&logo=twitch&logoColor=white)](https://twitch.tv/GalacticraftDev)

# Galacticraft Energy
An energy API that utilizes LibBlockAttributes.

## Installing
```groovy
repositories {
    maven {
        url "https://maven.galacticraft.dev"
        content { includeGroup("dev.galacticraft") }
    }
}

dependencies {
    include modImplementation("dev.galacticraft:GalacticraftEnergy:${VERSION}")
}
```