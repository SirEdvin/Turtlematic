# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.6] - 2024-02-03

### Changed

- Update code to new CC:T

### Fixed

- Crash when turtle mimic is beacon [#19]

## [1.2.5] - 2023-11-03

### Fixed

- Mercantile automata trading issue [#16]

## [1.2.4] - 2023-09-14

### Added

- `pose` argument for use and swing

### Fixed

- Use function set damage nbt tag when there is no damage

## [1.2.3] - 2023-08-24

### Fixed

- Piston turtle sound

## [1.2.2] - 2023-08-20

### Removed

- Extra logging for shooted item

## [1.2.1] - 2023-08-16

### Fixed

- Permission checks for smithing and mason turtles
- Propagate fix for inventory logic from peripheralium

## [1.2.0] - 2023-07-25

### Changed

- Boring internal toolkit migration

## [1.1.1] - 2023-07-21

### Fixed

- Automata core tooltip crash #14

## [1.1.0] - 2023-07-20

### Added

- Mimic gadget
- Nice tooltips for automata cores

### Fixed

- Various scanner problems (see UnlimitedPeripheralWorks#22)
- Potential crash when provide incorrect item to automata upgrades
- Mason recipe is not missed anymore (#12)
- Tooltip for soul vial (#13)

## [1.0.0] - 2023-07-09

### Changed

- Adapt to CC:T 1.106 and introduce a lot of persistence into turtles

## [0.7.4] - 2023-07-03

### Changed

- Use java `time` instead of kotlin one, fix #10

## [0.7.3] - 2023-06-24

### Added

- `1.20.1` support

## [0.7.2] - 2023-06-13

### Added

- Turtle `suck` method for most of automata cores

## [0.7.1] - 2023-06-13

### Fixed

- Dependencies definition

## [0.7.0] - 2023-06-13

### Added

- `1.20` support

## [0.6.4] - 2023-06-12

### Added

- Peripheralium 0.5.7

## [0.6.3] - 2023-06-04

### Added

- Protective automata, that can interact with hostile mobs

### Fixed

- Block breaking progress storing

## [0.6.2] - 2023-05-28

### Added

- Husbandry automata, starting from netherite tier, can disable animal AI

### Fixed

- Mixins for forge and fabric

## [0.6.1] - 2023-05-24

### Changed

- Required peripheralium version to 0.5.4, to fix forge issue with shooting turtle

### Fixed

- Brewing automata `throwPotion` method now correctly accepts arguments

## [0.6.0] - 2023-05-23

### Added

- Forge support
- 1.19.4 support
- Bow turtle

### Changed

- Brewing automata throw item API

## [0.5.4] - 2023-05-18

### Added

- Chunk loading turtle! You can use chunk vial to make sure that turtle will be always loaded

### Changed

- Mod recipes now supports fabric tags

## [0.5.3] - 2023-04-25

### Added
- Mercantile automata for trading with villagers and traders
- Starbound Mercantile automata, that can restock villagers!

### Changed

- Brewing automata effects IDs will now me "minecraft:something"
- Enchanting automata enchantment data now enriched with correct cost information

## [0.5.2] - 2022-12-28
### Fixed
- Blocklist check for capture end automata logic #3

## [0.5.1] - 2022-12-26
### Fixed
- Peripheralium compatibility
- Problems with Fuel (and other ability-centered API) not available
- Teleporting to non-existing points now returning operation failure instead of teleporting to 0, 0

## [0.5.0] - 25.10.22
### Added
- 1.19 support

## [0.4.4] - 17.10.22
### Fixed
- Chipped integration for 2.0.0 version

## [0.4.3] - 12.05.22
### Fixed
- Rail shape changing doesn't break rail placing (again, yep)

## [0.4.2] - 12.05.22
### Fixed
- Rail shape changing doesn't break rail placing, and now it is possible to change shape to ascending ones

## [0.4.1] - 11.05.22
### Changed
- Mason automata now can change rail shape to any possible shape

## [0.4.0] - 09.05.22

**BREAKING CHANGE**

Turtlematic now requires [Peripheralium](https://www.curseforge.com/minecraft/mc-mods/peripheralium/) as basic library
### Added
- Treasure enchantments for starbound enchanting automata

## [0.3.1] - 06.05.22
### Added
- Support for `up` and `down` direction for piston methods
- `isFuelConsumptionDisable` as configuration part for Fuel API

## [0.3.0] - 05.05.22
### Added
- Ability to harvest crops for husbandry automata
- Extra information about crops for husbandry automata
- Extra information about beehives for husbandry automata
- Ticking effect to husbandry automata cores (netherite and above)
- Chipped support for mason automata
- Piston and sticky piston turtles
- Traits description on automata cores
- Base automata levels: netherite, starbound and creative
- Villager automata levels: starbound and creative
- Trashing turtle

### Changed
- Default cooldown for `use` now 1 second

## [0.2.0] - 01.05.22
### Added
- Brewing automata
- Smithing automata
- Enchanting automata
- Mason automata

### Fixed
- Single entity hit login (again)
- `use` now will also perform general item use if use on block lead to nothing

## [0.1.3] - 28.04.22
### Changed
- `soulHarvest()` now have more human-readable results

## [0.1.2] - 28.04.22
### Fixed
- Single entity interaction logic