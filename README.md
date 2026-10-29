# Feed Baby Villagers

Feed baby villagers to speed up their growth. Available for NeoForge, Forge and
Fabric, with no new items, blocks or network payloads.

## How to use

There are two ways to feed an awake baby villager:

- Right-click with food in your **main hand**. One item is consumed, except in
  Creative. There is no additional right-click cooldown.
- Drop food nearby for the baby to pick up. It stores the stack in its vanilla
  inventory, then eats one item every 3 seconds by default until it becomes an
  adult or runs out of accepted food. The first meal also waits for that interval.

The default foods are bread, carrots, potatoes and beetroot. Each item has the
same effect: it removes about 10% of the remaining growth time and produces green
happy villager particles and the vanilla villager approval sound.

Right-click food is consumed directly and does not enter the villager's inventory.
Dropped food uses normal pickup rules, including pickup delay, inventory capacity,
and `mobGriefing` or the loader's corresponding permission hook. Configured custom
foods can also be picked up by babies. Adults keep their normal pickup behavior.
Automatic feeding pauses while sleeping and stops at adulthood; leftover food
stays in the vanilla inventory. It also works with accepted food already stored
there, including after a world reload. Its meal interval restarts after reopening
the world, waking up, or running out of accepted food.

Offhand feeding is not supported. Sleeping villagers, name tags and spawn eggs
keep their normal right-click interactions. Adult breeding, healing, pathfinding
and professions are unchanged.

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

The current jars passed source, compilation and dedicated-server runtime checks
on the normal loaders and all listed minimum boundaries. Client checks for the
original right-click implementation covered singleplayer on all three loaders
and vanilla-client connections. The new inventory-feeding path has server checks;
its real-client visuals and audio have not been checked. This coverage does not
establish compatibility with every setup.

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
feedingIntervalSeconds = 3
```

- `enabled`: enable or disable both feeding methods.
- `foods`: registered item IDs, including items from installed mods. Every item
  has the same growth effect. Invalid entries are skipped with warnings.
  `foods = []` is valid, and a list with no valid entries stays empty.
- `growthPercent`: a whole number from 1 to 100, applied to the remaining growth
  time with vanilla rounding.
- `feedingIntervalSeconds`: a positive whole number of seconds between inventory
  meals (maximum 2147483647). Uses 20 server ticks per second; server lag can make
  it take longer in real time. It does not delay right-click feeding. Existing
  configs without this key use 3 seconds and are not rewritten.

A missing key uses its default. A wrong type for `enabled`, a non-list `foods`,
or an invalid `growthPercent` or `feedingIntervalSeconds` uses only that setting's
default and logs a warning.
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

The original right-click implementation's Minecraft 1.21.1 runtime checks with
Villager Trade Enchant 1.0.0, Peek 1.1.0,
Swap 1.1.0 and Labels 1.0.0 covered baby feeding and Enchant transactions.
NeoForge included the real client screen; Forge and Fabric used server assertions
for those transactions. Other interactions and combinations remain unverified.
Inventory feeding was added after those companion checks. Mods that replace baby
villager interaction, pickup or inventory handling can interfere.

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
profiles are omitted from the production project to avoid preparing game
directories during an IDE import. Runtime testing uses temporary profiles in an
isolated scratch copy.

## License

MIT, copyright 2026 emberbocor. See [LICENSE](LICENSE).

The Fabric jar bundles NightConfig core and toml 3.8.0, licensed under
[LGPL-3.0](https://github.com/TheElectronWill/night-config/tree/v3.8.0).
