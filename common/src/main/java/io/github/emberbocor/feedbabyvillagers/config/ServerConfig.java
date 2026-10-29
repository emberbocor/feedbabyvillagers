package io.github.emberbocor.feedbabyvillagers.config;

import java.nio.file.Path;
import java.util.Set;

import io.github.emberbocor.feedbabyvillagers.platform.Services;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ServerConfig {
    static final Settings DEFAULTS = new Settings(true,
            Set.of(Items.BREAD, Items.CARROT, Items.POTATO, Items.BEETROOT), 10, 3);

    private static volatile Settings settings = DEFAULTS;

    private ServerConfig() {
    }

    public static void load() {
        Path file = Services.PLATFORM.getConfigDirectory().resolve(ServerConfigFile.FILE_NAME);
        settings = new ServerConfigReader(file).read(ServerConfigFile.load(file).orElse(null));
    }

    public static Settings settings() {
        return settings;
    }

    public record Settings(boolean enabled, Set<Item> foods, int growthPercent, int feedingIntervalSeconds) {
        public Settings {
            foods = Set.copyOf(foods);
        }
    }
}
