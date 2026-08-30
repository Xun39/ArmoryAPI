# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0-1.21.1] - 2026-08-30

### Added

* Added the `PieceType` interface for defining extensible item piece types. A piece type provides its registry-name suffix and can generate its registry name from an item set name.
* Added support for extensible tool piece types through `ToolPieceType`, allowing tools beyond the standard vanilla tool types to be represented by Armory.
* Added per-piece tool factories through `ToolItemFactory`, allowing each tool piece to define how its concrete `Item` instance is created.
* Added per-piece tool configuration, including default `ToolStats`, item property modifiers, and additional attribute modifiers.
* Added `ToolContext` to provide shared tool-set configuration to customizers and item factories.
* Added `ToolMetaData` and `ToolMetaDataLookup` for associating runtime metadata with concrete Armory-created tool items.
* Added support for selecting individual pieces when constructing a `ToolSet`, allowing partial sets such as a set containing only a sword and hoe.

### Changed

#### `ItemSet`

* Refactored `ItemSet` to use the extensible `PieceType` abstraction instead of requiring piece types to be Java enums:

    * Previously: `ItemSet<P extends Enum<P>, T extends Item>`
    * Now: `ItemSet<P extends PieceType, T extends Item>`
* Reworked item factories within `ItemSet` so each piece can provide its own `Function<Item.Properties, T>` factory.
* Updated the registration API to generate registration entries from the factories associated with each concrete piece type.

#### `ToolSet`

* Refactored `ToolSet` to support arbitrary `ToolPieceType` implementations rather than assuming that every set consists of the five standard vanilla tools.
* Replaced the previous `ToolFactory` concept with `ToolItemFactory`.

    * `ToolItemFactory` is now responsible for creating the final concrete `Item` for a tool piece.
    * `ToolCustomizer` remains responsible for the higher-level customization and creation process and delegates final item construction to the piece's factory.
* Removed `DefaultToolCustomizer` in favor of the `ToolCustomizer.DEFAULT` implementation.
* Replaced the previous global attribute configuration mechanism based on `AttributeHelper` with `additionalAttributes` on `ToolSet`.

    * This modifier is applied in addition to the tool's existing attack damage and attack speed attributes rather than replacing them.
* Removed `ToolSet.Builder.withToolStats(float[] damages, float[] speeds)`.

    * The previous array-based approach assumed a fixed collection and ordering of vanilla tools, which is unsuitable now that tool sets can contain arbitrary pieces and support modded tool types.
    * Tool statistics are now defined by `ToolPieceType` and can be overridden individually for specific pieces.
* Removed `ToolSet.Builder.withVanillaBalance()`.

    * Default statistics are now defined directly by each `ToolPieceType`.
* Removed `ToolSet.Builder.withItemProperties(Item.Properties)` and `withItemPropertiesSupplier(Supplier<Item.Properties)`.

    * These have been replaced by `UnaryOperator<Item.Properties>`, allowing callers to modify the existing item properties without having to construct a complete replacement.
* Updated tool creation so piece-specific factories can create different concrete item classes, enabling compatibility with modded tools such as Farmer's Delight's knife.

#### `ArmorSet`

* Applied the `ItemSet` and customization architecture changes to `ArmorSet`.
* Removed `DefaultArmorCustomizer` in favor of the `ArmorCustomizer.DEFAULT` implementation.
* Updated armor-set configuration to use the same item-property modifier and additional-attribute architecture as `ToolSet`.

#### Customizers and combat integration

* Renamed `AbstractToolEffectCustomizer` to `AbstractHitEffectCustomizer`.
* Changed hit-effect handling to use platform combat events rather than relying on hardcoded `HurtEnemy` overrides in individual item implementations.
* This allows hit effects to work with modded tool item classes instead of requiring Armory-specific item implementations.

### Breaking Changes

* `ItemSet` no longer requires piece types to be enums.
* `ToolFactory` has been replaced by `ToolItemFactory`.
* `DefaultToolCustomizer` and `DefaultArmorCustomizer` have been removed.
* `ToolSet.Builder.withToolStats(...)` has been removed.
* `ToolSet.Builder.withVanillaBalance()` has been removed.
* `ToolSet.Builder.withItemProperties(...)` and `withItemPropertiesSupplier(...)` have been removed.
* `AbstractToolEffectCustomizer` has been renamed to `AbstractHitEffectCustomizer`.
* The previous `AttributeHelper`-based tool-set attribute configuration has been replaced by `ToolSet`'s `additionalAttributes` configuration.

## [2.3.0-1.21.1] - 2026-05-24

### Changed
- Version bump only to maintain version consistency with the Minecraft 1.21.3 port of the library.
- No functional changes, API additions, or breaking changes in this release.

## [2.1.0-1.21.1] - 2026-05-17

### Added
- `AbstractEffectToolCustomizer` - abstract base class for tool customizers that apply status effects (such as poison, fire, or wither) on attack or use.

### Changed
- Reorganized package structure.

## [2.0.0-1.21.1] - 2026-05-16

### Added
- `ItemSet` - new base class for `ArmorSet` and `ToolSet`, unifying shared item-set behavior.
- `DefaultToolCustomizer` and `DefaultArmorCustomizer` - default implementations of the customizer interfaces.

### Changed
- Renamed `ArmorConfigurator` → `ArmorCustomizer`.
- Renamed `ToolConfigurator` → `ToolCustomizer`.
- Moved `GenericAttributeHelper` from `net.xun.armory.api.item.tools` to `net.xun.armory.impl.item.tools`.