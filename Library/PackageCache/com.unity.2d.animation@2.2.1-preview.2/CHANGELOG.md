# Changelog

## [2.2.1-preview.2] - 2019-09-27
### Fixed
- Fix case 1181183 where visual glitches appeared when SpriteSkin's GameObject is enabled after being disabled.

## [2.2.1-preview.1] - 2019-09-04
### Changed
- Bump version number due to new dependencies and validation check.
- Sprite and Group in Sprite Visibility Window appear in same order as original art source file

### Added
- Optional performance boost by installing Burst package.

## [2.2.0-preview.5] - 2019-08-07
###Fixed
- Fix Sprite visual might get corrupted when swapping Sprite with Sprite Resolver.

## [2.2.0-preview.4] - 2019-07-16
### Changed
- Update documentation
- Backport changes from Animation 3.0.2
- Make Size property field in Weight Brush draggable for changing brush size
- Change string hash for Category and Label name. This might break existing animation usage with SpriteResolver.
- Rename SpriteLibraryAsset::GetCategorylabelNames to SpriteLibraryAsset::GetCategoryLabelNames

### Fixed
- Fixed Amount slider not working in Weight Slider Panel
- Fixed exception when changing size to less than 0 in SpriteLibraryAssetInspector

## [2.2.0-preview.3] - 2019-06-06
### Changed
- BoneGizmos can be toggled in the SceneView Gizmos dropdown
- Scrollbar does not show for toolbar when required to scroll
- Change Sprite Library implementation.
 - APIs redesigned. This will cause compilation errors if previous APIs are used
 - Data serialization change. Previous Asset and Component data will not be compatible

### Fixed
- Fixed Amount slider not working in Weight Slider Panel
- Fixed exception when changing size to less than 0 in SpriteLibraryAssetInspector

## [2.2.0-preview.2] - 2019-05-10
### Added
- BoneGizmos will only show in selected hierarchies.
- Associate nearest Bone to Sprite intead of root Bone when no Bones are overlapping the Sprite
- Fixed Sprite not showing after it is being culled due to bone animation
- Add icons for Sprite Library Asset, Sprite Library Component and Sprite Resolver Component
- Fixed Sprite Library Asset Inspector Property Field text clipping
- SpriteResolver will assign Sprite to SpriteRenderer even it resolves to a missing Sprite
- Add visual feedback in SpriteResolver Inspector for missing Sprite

## [2.2.0-preview.1] - 2019-05-09
### Added
- Upgrade for 2019.2
- Copy and Paste rework
- Visibility Window remains open when switching between tools
- Reparent Bone tool removed and functionality moved into Bone Visibility Panel
- Added Sprite Library feature
- Add Layer Grouping support in Sprite Visibility Panel

## [2.1.0-preview.4] - 2019-04-29
### Added
- Fix skinning not in sync with the rendering.

## [2.1.0-preview.3] - 2019-04-24
### Added
- Set Burst compilation off for internal build

## [2.1.0-preview.2] - 2019-02-25
### Added
- Fix enable skinning on add SpriteSkin component
- Upgrade dependency package version for Unity 2019.1 support
- Fix case 1118093: SpriteSkin.onDrawGizmos() increases memory usage.

## [2.1.0-preview.1] - 2019-01-25
### Added
- Update package to work with 2019.1
- Improve animation runtime performance
- Fix bone reparenting sibling order
- Fix Sprite Visibility Tool in disabled state in certain cases
- Update documents

## [2.0.0-preview.1] - 2018-11-20
### Added
- Overhauled 2D Animation workflow.
  - Refer to updated documentation for workflow changes.
- Single Sprite Editor Window module for 2D Sprite Rigging workflow
  - Unified Bone, Geometry and Weight tools in a single window
- Supports Multiple Sprite Single Character rigging workflow through 2D PSD Importer Package.
- SpriteSkin now uses user define bounds for renderer culling

## [1.0.16-preview.2] - 2018-11-14
### Added
- Fix 2 Issues:
  1. Prefabs with SpriteSkin loses references to bone hierarchy when Library folder is rebuilt/different.
  2. The scene viewport shows the character without any bones applied, needing an re-import.

## [1.0.16-preview.1] - 2018-7-18
### Added
- Fix error log about VertexAttribute

## [1.0.16-preview] - 2018-6-20
### Added
- Fix Documentation warnings
- Fix error log complaining about DidReloadScripts signature.
- Fix issues with generate outline

## [1.0.15-preview] - 2018-4-12
### Added
- New Version suffix (preview)
- Improved Scene View gizmos for better manipulation of bone rotation and position
- Added notification when Sprites are imported with incorrect weights
- Fixed bug where textures with max texture size could not generate geometry
