# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.3.0-1.21.3] – 2026-05-24
Initial port targeting Minecraft 1.21.3.

### Added
- `ToolItem` - new item class representing tools within the API.
- Built-in attribute handling inside `ToolSet`.

### Removed
- `AttributeHelper` and `GenericAttributeHelper`.
- External attribute helpers are no longer required, as attribute support is now integrated directly into `ToolSet`.

### Changed
- Reworked item factory and initialization patterns across the API to align with Minecraft 1.21.3’s updated registry system.
- Many APIs previously using `Supplier<Item>` now use `Function<Item.Properties, Item>` instead.