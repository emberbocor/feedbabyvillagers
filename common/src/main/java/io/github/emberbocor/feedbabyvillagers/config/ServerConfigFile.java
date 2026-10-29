package io.github.emberbocor.feedbabyvillagers.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import io.github.emberbocor.feedbabyvillagers.FeedBabyVillagers;

final class ServerConfigFile {
    static final String FILE_NAME = FeedBabyVillagers.MODID + "-server.toml";

    private static final String DEFAULT_TEXT = """
            # Feed Baby Villagers server config.
            # Created on the first server or world start.
            # Restart the server or reopen the singleplayer world to apply changes.

            # Allow players to feed baby villagers with food in their main hand.
            enabled = true

            # Registered item IDs accepted for direct feeding. Every item has the same effect.
            # Invalid entries are skipped. An empty list disables all feeding foods.
            foods = ["minecraft:bread", "minecraft:carrot", "minecraft:potato", "minecraft:beetroot"]

            # Percentage of remaining growth time removed per item. Whole number from 1 to 100.
            # Uses vanilla animal-feeding rounding: remaining ticks / 20, then percentage,
            # rounded down to whole seconds. Near adulthood an item may remove zero seconds.
            growthPercent = 10
            """;

    private ServerConfigFile() {
    }

    static Optional<UnmodifiableConfig> load(Path file) {
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
                Files.writeString(file, DEFAULT_TEXT, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
                FeedBabyVillagers.LOGGER.info("Created {}", file);
            } catch (IOException e) {
                FeedBabyVillagers.LOGGER.warn("Could not create {}, using the default settings: {}", file, e.getMessage());
            }
            return parse(file, DEFAULT_TEXT);
        }

        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            return parse(file, text.startsWith("\uFEFF") ? text.substring(1) : text);
        } catch (IOException e) {
            FeedBabyVillagers.LOGGER.warn("Could not read {}, using the default settings; file left unchanged: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<UnmodifiableConfig> parse(Path file, String text) {
        try {
            return Optional.of(TomlFormat.instance().createParser().parse(text));
        } catch (RuntimeException e) {
            FeedBabyVillagers.LOGGER.warn("Could not parse {}, using the default settings; file left unchanged: {}", file, e.getMessage());
            return Optional.empty();
        }
    }
}
