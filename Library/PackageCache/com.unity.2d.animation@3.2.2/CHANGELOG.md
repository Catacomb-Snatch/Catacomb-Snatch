# Changelog

## [3.2.2] - 2020-04-06
### Fixed
- Fixed bone name field in weight slider does not display bone name (case 1226249)
- Fixed SpriteResolver's Inspector not updated when GameObject is disabled

### Changed
- Improved deformation performance when Collection and Burst package is installed

### Added
- Allow reordering of bone order in Bone Influence window. This is to allow fine tuning of bone order when shown in SpriteSkin's Inspector

## [3.2.1] - 2020-03-20
### Fixed
- Fixed inconsistent line ending

## [3.2.0] - 2020-02-20
### Added
- Add alwaysUpdate option to SpriteSkin to determine if SpriteSkin execution should occur even when the associated SpriteRenderer is culled
- Added message to inform user on dependent packages when viewing certain sample Scenes
- Added API to access deformed vertices from SpriteSkin

### Changed
- Improved SpriteSkinEditor UI
- Adjust length of popup and value fields for Weight Slider Window

### Removed
- Remove Bounds Gizmo from SpriteSkin
- Remove Reset Bounds button from SpriteSkinEditor

### Fixed
- Fixed Sprite asset used by SpriteSkin in Scene is being deleted
- Fixed broken documentation links in inspectors
- Fixed Sprite deformation not updated when GameObject is being enabled
- Fixed exception after reverting from creating new vertices and edges
- Fixed visual defect after undoing changes to Bone Transform properties in SpriteSkin's Inspector

## [3.1.1] - 2020-01-20
###Fixed
- Fix 2D Animation not working when reloading scene in runtime (case 1211100)

###Added
- Bone visibility persist after apply
- Sprite visibility persist after apply

###Changed
- Deformed Sprite's bounds are now calculated and bounds property is removed from SpriteSkin's inspector (case 1208712)
- Changed default shortcut key for "Animation/Create Vertex" from "Shift-D" to "Shift-J"
- Changed default shortcut key for "Animation/Weight Brush" from "Shift-C" to "Shift-N"

## [3.1.0] - 2019-12-10
### Changed
- Changed how Samples are imported into the user's project
- Updated Third Party Notices file

### Fixed
- Fix Animation Samples crashes when installing on certain machines (case 1185787)
- Fix visual glitch when using SpriteSwap with Multi-threaded rendering (case 1203380)
- Fix bone name misaligned under Weight Slider Inspector when a name contains more than 26 letters (case 1200873)
- Fix bones not chained correctly when splitting bone in certain cases
- Fix 'Label' and 'Sprite' name overlaps with its input field when preset of "Sprite Library Asset" is created (case 1201061)
- Fix bone names can be empty (case 1200861)
- Fix bone gets created even though clicked on Visibility Panel (case 1200857)
- Fix NullReferenceException when using shortcut 'Shift+1' in certain cases (case 1200849)

### Added
- Expose SpriteSkin component to be accessible from scripts.

## [3.0.8] - 2019-11-06
### Changed
- Improve optional performance boost by installing Burst and Collections package. Currently tested with 
    - com.unity.collections 0.1.1-preview
    - com.unity.burst 1.1.2

### Added
- Skinning Module now persists the following state after Apply or Revert is clicked in Sprite Editor Window
    - Current view mode i.e. Character or Spritesheet Mode
    - Sprite Selection
    - Bone Selection
    - Preview Pose
    - Vertex Selection
    - Visibililty Tool Active State
    - Weight Brush Settings

## [3.0.7] - 2019-10-18
### Fixed
- Fix Search reset button is visible in Visibility tool when nothing is entered into the search field (case 1182627)
- Fix Sprite outline disappears when the Selected Outline Color alpha value is less than 255 (case 1186776)
- Fix button's label spelling error in 'Generate For All Visible' (case 1188621)

## [3.0.6] - 2019-09-18
### Changed
- Remove usage of Resource folder for assets needed by package.

### Fixed
- Fix GC allocation when using Sprite Resolver.

## [3.0.5] - 2019-09-06
### Added
- Optional performance boost by installing Burst package.
- Samples showing different how to produce different outcomes

### Changed
- Sprite and Group in Sprite Visibility Window appear in same order as original art source file

### Fixed
- Fix missing bone data in Sprite when importing with AssetDatabase V2

## [3.0.4] - 2019-08-09
### Added
- Add related test packages
- Added tangent deform for lighting support

### Fixed
- Fixed Amount slider not working in Weight Slider Panel
- Fixed exception when changing size to less than 0 in SpriteLibraryAssetInspector
- Fixed Sprite visual corruption when swapping Sprite using SpriteResolver

###Changed
- Make Size property field in Weight Brush draggable for changing brush size
- Rename SpriteLibraryAsset::GetCategorylabelNames to SpriteLibraryAsset::GetCategoryLabelNames
- Change string hash for Category and Label name. This might break existing animation usage with SpriteResolver.
- Add Experimental tag on Sprite Swap related features


## [3.0.3] - 2019-07-17
### Changed
- Update documentation
- Update to latest Mathematics package version

## [3.0.2] - 2019-07-13
### Changed
- Mark package to support Unity 2019.3.0a10 onwards.

## [3.0.1] - 2019-07-12
### Changed
- Fix path length due to validation failure.

## [3.0.0] - 2019-06-17
### Changed
- Remove preview tag.
- Remove experimental namespace

## [2.2.0-preview.3] - 2019-06-06
### Added
- BoneGizmos can be toggled in the SceneView Gizmos dropdown
- Scrollbar does not show for toolbar when required to scroll
- Change Sprite Library implementation.
 - APIs redesigned. This will cause compilation errors if previous APIs are used
 - Data serialization change. Previous Asset and Component data will not be compatible

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

## [1.0.16-preview.1] - 2018-07-18
### Added
- Fix error log about VertexAttribute

## [1.0.16-preview] - 2018-06-20
### Added
- Fix Documentation warnings
- Fix error log complaining about DidReloadScripts signature.
- Fix issues with generate outline

## [1.0.15-preview] - 2018-04-12
### Added
- New Version suffix (preview)
- Improved Scene View gizmos for better manipulation of bone rotation and position
- Added notification when Sprites are imported with incorrect weights
- Fixed bug where textures with max texture size could not generate geometry
