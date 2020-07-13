# Changelog
All notable changes to this package will be documented in this file.

## [0.6.2-preview] - 2019-07-29

### Fixed
* Fixed property bag resolution for boxed and interface types.

## [0.6.1-preview] - 2019-07-25

### Fixed
* Fixed the reflection property generator to correclty handle `IList<T>`, `List<T>` and `T[]` collection types.
* Fixed `ArgumentNullException` when visiting a null container.

## [0.6.0-preview] - 2019-07-19

### Added
* Added `[Property]` attribute which can be used on fields or C# properties. The attribute will force the reflection generator to include the member.

### Changed
* `PropertyBagResolver.RegisterProvider` has been removed and replaced with access to a static `ReflectedPropertyBagProvider`.
* `Unity.Properties.Reflection` assembly has been removed and merged with `Unity.Properties`.

### Fixed
* `TypeConverter` no longer warns if the source and destination types are the same.
* TypeConversion of enum types will now convert based on the value and not the index.
* PropertyContainer.Transfer now ensures destination type is a reference type when not passed by ref.
* Fix generated properties for `List<string>` incorrectly treating strings as container types.
* `UnmanagedProperty` can now be generated for `char` types during reflection.

## [0.5.0-preview] - 2019-04-29

### Changed
* Complete refactor of the Properties package.

<!-- Template for version sections
## [0.0.0-preview.0]

### New Features


### Upgrade guide


### Changes


### Fixes
-->