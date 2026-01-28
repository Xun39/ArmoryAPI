# Armory API

<a href="https://www.curseforge.com/minecraft/mc-mods/armory-api">
  <img alt="Curseforge" src="https://img.shields.io/badge/-CurseForge-black?style=flat-square&logo=curseforge&labelColor=black">
</a>
<a href="https://www.modrinth.com/mod/armory-api">
  <img alt="Modrinth" src="https://img.shields.io/badge/-Modrinth-gray?style=flat-square&logo=modrinth&logoColor=lightgreencyan&labelColor=black">
</a>

Armory API is a lightweight library for defining and managing **tool sets and armor sets** in Minecraft mods.

It was extracted from **XunLib** to allow **Forge support**, as XunLib’s multi-loader registration system is not compatible with Forge. Since the tool and armor set system does not depend on that system, separating it makes the codebase loader-agnostic, easier to maintain, and more lightweight.

## Supported Loaders

- Forge
- NeoForge
- Fabric

## Features

- APIs for defining customizable **ToolSet** and **ArmorSet**
- Reduced boilerplate when adding tools and armor
- Loader-independent implementation
- Small scope with minimal dependencies

Planned additions include:
- Built-in attribute handling
- Compatibility hooks for modded tool types
- Additional armory-related APIs where appropriate

## Usage Example

```java
public static final ToolSet DIAMOND_TOOLS = new ToolSet.builder("diamond", Tiers.DIAMOND, new GenericAttributeHelper())
        .withVanillaBalance()
        .withToolStats(ToolType.AXE, 5.0F, 1.0F) // diamond tools have slightly different attributes
        .withToolStats(ToolType.HOE, -3.0F, 4.0F) // diamond tools have slightly different attributes
        .build();

public static final ArmorSet DIAMOND_ARMORS = new ArmorSet.builder("diamond", ArmorMaterials.DIAMOND)
        .withDurabilityFactor(33)
        .build();
````