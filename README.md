# Feed Baby Villagers

Help baby villagers grow up faster.

Feed Baby Villagers lets you feed baby villagers the same way you feed baby animals. Right-click a baby with bread, carrots, potatoes or beetroot and it grows up a bit faster, with green particles and a happy villager sound. Babies also eat food they pick up from the ground, so you can drop a stack and let them grow on their own.

The mod runs on the server only. Players join with a plain vanilla client and still see the particles and hear the sound.

Available for NeoForge, Forge and Fabric.

## Features

- **Hand feeding.** Right-click a baby villager with food in your main hand. One item is used (none in Creative) and the baby's remaining growth time drops by 10%. There is no cooldown: feed as often as you like.
- **Self-feeding.** Drop food near a baby villager. It picks the food up like any villager does and eats one item every 3 seconds until it is grown up or runs out of food. Leftover food stays in its inventory.
- **Vanilla feedback.** Every meal shows the green happy villager particles and plays the villager's approval sound.
- **Configurable.** The foods, the growth per item, the time between meals and the whole mod can be changed or turned off in the server config. Food from other mods can be added.

## How growing works

Growth works like feeding a baby animal in vanilla. The remaining growth time is taken in whole seconds, and each item removes the configured percentage of it, rounded down. A baby with 20 minutes left gains 2 minutes from its first meal at the default 10%; each further meal gains a little less.

- Close to adulthood a meal can remove less than a second. Such a meal still uses the item, and the baby finishes growing on its own.
- With `growthPercent = 100` one item can finish the growth at once. As with baby animals in vanilla, the time skipped this way can then become a short breeding cooldown for the new adult.
- Self-feeding pauses while the baby sleeps. The time until the next meal starts over after the world is reopened, after the baby wakes up and after it runs out of food.
- Only the main hand feeds. Sleeping babies, name tags and spawn eggs keep their normal right-click.
- On NeoForge and Forge, sneaking while right-clicking skips villager interaction, so it does not feed. On Fabric, sneak-right-click feeds as well.

## Requirements

| | NeoForge | Forge | Fabric |
|---|---|---|---|
| Minecraft | 1.20.1 | 1.20.1 | 1.20 or 1.20.1 |
| Loader | NeoForge 47.1.5 or newer | Forge 47.1.0 or newer | Fabric Loader 0.14.21 or newer |
| Other | | | Fabric API 0.83.0 or newer |
| Jar | `feedbabyvillagers-forge-1.20.1-<version>.jar` | `feedbabyvillagers-forge-1.20.1-<version>.jar` | `feedbabyvillagers-fabric-1.20-1.20.1-<version>.jar` |

On Minecraft 1.20.1, NeoForge and Forge use the same Forge jar. Pick that jar for either loader, or the Fabric jar for Fabric.

The Fabric jar bundles [NightConfig 3.6.4](https://github.com/TheElectronWill/night-config/tree/v3.6.4) (core and toml, LGPL-3.0) to read the TOML config. NeoForge and Forge already ship it.

### Client and server

Install the mod on the server only, on every loader. It adds no items, blocks, entities, network messages or registries, so the server has nothing a client needs to know about:

- **NeoForge:** vanilla clients and NeoForge clients without the mod can join.
- **Forge:** vanilla clients and Forge clients without the mod can join. The mod is marked as server-only, so a Forge client without it still shows the server as compatible in the server list.
- **Fabric:** vanilla clients and Fabric clients without the mod can join. Fabric API is only needed on the server.

Installing it on a client as well does no harm. The client does not know the server's food list, so a baby may briefly shake its head when you feed it; the server still accepts the food and sends the particles and sound.

In singleplayer, install it in your own game; the built-in server runs it.

## Configuration

### Server: `config/feedbabyvillagers-server.toml`

Created with comments on the first server or world start:

```toml
# Enable main-hand feeding and automatic feeding from a baby's inventory.
enabled = true

# Registered item IDs accepted for both feeding methods. Every item has the same effect.
# Invalid entries are skipped. An empty list disables all feeding foods.
foods = ["minecraft:bread", "minecraft:carrot", "minecraft:potato", "minecraft:beetroot"]

# Percentage of remaining growth time removed per item. Whole number from 1 to 100.
growthPercent = 10

# Seconds between meals from a baby's inventory. Positive whole number.
# The first meal also waits this long. Does not add a cooldown to right-click feeding.
feedingIntervalSeconds = 3
```

- `foods` accepts any item ID, also from other mods. Babies then also pick those items up from the ground.
- Changes take effect after a server restart, or after closing and reopening a singleplayer world.
- Mistakes do not crash the server. An invalid value uses the default for that setting, unknown item IDs are skipped, and each problem is logged as a warning. If the file cannot be read at all (for example a TOML syntax error), all defaults are used, the file is left untouched and the log says why.

## Removing the mod

The world opens normally. Babies keep the growth they already gained and grow up at the vanilla pace. Food they picked up stays in their inventory, as it would in vanilla. The config file stays in `config/` until you delete it.

## Compatibility

- **Villager Trade Peek, Villager Trade Swap, Villager Trade Labels, Villager Trade Enchant:** work together.
- In vanilla, villagers of any age already pick up bread, carrots, potatoes and beetroot. With this mod, babies eat what they collect. In a villager breeder, keep babies away from the food meant for the adults (most breeders already drop the babies out).
- Mods that change how baby villagers react to right-clicks, pick up items or use their inventory may interfere.

## Building from source

```
./gradlew build
```

The jars are written to `forge/build/libs/` and `fabric/build/libs/`.

## License

MIT. The Fabric jar includes NightConfig, which is licensed under the LGPL-3.0.
