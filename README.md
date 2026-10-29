# Feed Baby Villagers

Feed baby villagers to speed up their growth. Available for NeoForge, Forge and
Fabric, with no new items, blocks or network payloads.

## How to use

Right-click an awake baby villager with a food item in your **main hand**. The
default foods are bread, carrots, potatoes and beetroot. Each has the same effect:
one item removes about 10% of the remaining growth time and produces green happy
villager particles and the vanilla villager approval sound. Creative players
keep their item. There is no additional feeding cooldown.

The food is consumed directly for growth; it is not put in the villager's
inventory. Offhand feeding is not supported. Adults and other interactions keep
their normal behavior, including sleeping villagers, name tags and spawn eggs.
Breeding, healing, AI and professions are unchanged.

Growth uses vanilla age handling and animal-feeding rounding. Remaining ticks
are divided by 20 to get whole seconds, then the configured percentage is applied
and rounded down to whole seconds. At the default 10%, a baby with 20 minutes
remaining gains 2 minutes. Near adulthood a food item can remove **zero seconds**
and is still consumed. Even 100% leaves any remainder shorter than one second
to finish naturally. There is no minimum speedup.

## Requirements

| Loader | Minecraft | Minimum loader | Additional requirement |
| --- | --- | --- | --- |
| NeoForge | 1.21.1 | 21.1.1 | Java 21 |
| Forge | 1.21.1 | 52.0.0 | Java 21 |
| Fabric | 1.21–1.21.1 | Fabric Loader 0.15.11 | Fabric API 0.102.0+ for your Minecraft version, Java 21 |

NeoForge and Forge use separate jars. Fabric bundles NightConfig core and toml
3.8.0; NeoForge and Forge supply NightConfig themselves.

These boundaries are checked by source inspection and compilation during
production. In-game behavior and mod compatibility still require manual testing;
no game or runtime test has been run for this initial release.

## Installation

- **Multiplayer:** put the jar for your server's loader in its `mods` folder.
  Fabric servers also need Fabric API. Players can join with a vanilla client
  without this mod; particles and sound use vanilla packets. Other installed
  mods may have their own client requirements.
- **Singleplayer:** install the appropriate loader and put its jar in your
  game's `mods` folder. Install Fabric API too when using Fabric.

Use the jar matching the loader:

- `feedbabyvillagers-neoforge-1.21.1-1.0.0.jar`
- `feedbabyvillagers-forge-1.21.1-1.0.0.jar`
- `feedbabyvillagers-fabric-1.21-1.21.1-1.0.0.jar`

## Configuration

The file is `config/feedbabyvillagers-server.toml`. It is created with English
comments when a server or singleplayer world first starts. Restart the server
or close and reopen the world to apply edits. It is shared by worlds using that
game installation's config directory; there is no per-world override or reload
command.

```toml
enabled = true
foods = ["minecraft:bread", "minecraft:carrot", "minecraft:potato", "minecraft:beetroot"]
growthPercent = 10
```

- `enabled`: enable or disable direct feeding.
- `foods`: registered item IDs, including items from installed mods. Every item
  has the same growth effect. Invalid entries are skipped with warnings.
  `foods = []` is valid, and a list with no valid entries stays empty.
- `growthPercent`: a whole number from 1 to 100, applied to the remaining growth
  time with vanilla rounding.

A missing key uses its default. A wrong type for `enabled`, a non-list `foods`,
or an invalid `growthPercent` uses only that setting's default and logs a warning.
A broken or unreadable file uses all defaults and logs a warning. Existing files
are left unchanged, including broken files.

## Interaction details and compatibility

The mod preserves each loader's normal villager interaction gates. Ordinary
right-click feeds on all three loaders. Sneak-right-click bypasses villager
interaction on NeoForge and Forge, so it does not feed; on Fabric the vanilla
baby interaction is reached and feeding works.

Client prediction remains vanilla, even with the mod installed on the client.
A baby may briefly shake its head while the server accepts the food and sends
the happy particles and sound. The server does not play the refusal sound for a
successful feed. The client does not receive a custom config sync.

Villager Trade Enchant's Minecraft 1.21.1 interaction mixin skips babies in its
source, so its interaction is separate from feeding. Combined runtime testing
with it and other villager mods has not been performed. Mods that replace baby
villager interaction can interfere.

Removing this mod leaves ordinary vanilla age data; it adds no custom age
storage or world data.

## Building

Use JDK 21. The wrapper uses Gradle 9.3.1. The build follows the `common`,
`neoforge`, `forge`, `fabric` and `buildSrc` multiloader layout.

```sh
./gradlew build
```

Distributable jars are in `neoforge/build/libs`, `forge/build/libs` and
`fabric/build/libs`. The `common` jar is not a standalone mod.

Minimum compilation commands are documented in `gradle.properties`. Production
verification adds `-Xlint:all` to every Java compile using an external Gradle init
script. Build and compile checks do not start Minecraft. Development launcher
profiles are deferred until runtime review to avoid preparing game directories
during an IDE import.

## License

MIT, copyright 2026 emberbocor. See [LICENSE](LICENSE).

The Fabric jar bundles NightConfig core and toml 3.8.0, licensed under
[LGPL-3.0](https://github.com/TheElectronWill/night-config/tree/v3.8.0).
