# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.3.0] - 2026-05-24

### Changed
- Version bump only to maintain version consistency with the Minecraft 1.21.3 port of the library.
- No functional changes, API additions, or breaking changes in this release.

## [2.1.0] - 2026-05-17

### Added
- `AbstractEffectToolCustomizer` - abstract base class for tool customizers that apply status effects (such as poison, fire, or wither) on attack or use.

### Changed
- Reorganized package structure.

## [2.0.0] - 2026-05-16

### Added
- `ItemSet` - new base class for `ArmorSet` and `ToolSet`, unifying shared item-set behavior.
- `DefaultToolCustomizer` and `DefaultArmorCustomizer` - default implementations of the customizer interfaces.

### Changed
- Renamed `ArmorConfigurator` → `ArmorCustomizer`.
- Renamed `ToolConfigurator` → `ToolCustomizer`.
- Moved `GenericAttributeHelper` from `net.xun.armory.api.item.tools` to `net.xun.armory.impl.item.tools`.

### Migration Notes
- Update imports and references if you were using the old configurator classes.