package io.github.emberbocor.feedbabyvillagers.config;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import io.github.emberbocor.feedbabyvillagers.FeedBabyVillagers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

final class ServerConfigReader {
    private static final Set<String> KEYS = Set.of("enabled", "foods", "growthPercent", "feedingIntervalSeconds");

    private final Path file;

    ServerConfigReader(Path file) {
        this.file = file;
    }

    ServerConfig.Settings read(@Nullable UnmodifiableConfig config) {
        ServerConfig.Settings defaults = ServerConfig.DEFAULTS;
        if (config == null) {
            return defaults;
        }
        for (UnmodifiableConfig.Entry entry : config.entrySet()) {
            if (!KEYS.contains(entry.getKey())) {
                warn("unknown option '{}', ignored", entry.getKey());
            }
        }
        return new ServerConfig.Settings(enabled(config, defaults.enabled()),
                foods(config, defaults.foods()), percent(config, defaults.growthPercent()),
                interval(config, defaults.feedingIntervalSeconds()));
    }

    private boolean enabled(UnmodifiableConfig config, boolean fallback) {
        Object value = config.get("enabled");
        if (value == null) {
            return fallback;
        }
        if (value instanceof Boolean enabled) {
            return enabled;
        }
        warn("enabled must be true or false, got '{}'; using {}", value, fallback);
        return fallback;
    }

    private Set<Item> foods(UnmodifiableConfig config, Set<Item> fallback) {
        Object value = config.get("foods");
        if (value == null) {
            return fallback;
        }
        if (!(value instanceof List<?> entries)) {
            warn("foods must be a list of item IDs; using the default foods");
            return fallback;
        }
        Set<Item> foods = new HashSet<>();
        for (Object entry : entries) {
            ResourceLocation id = entry instanceof String text ? ResourceLocation.tryParse(text) : null;
            if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) {
                warn("invalid or unknown food item ID '{}', skipped", entry);
            } else {
                foods.add(BuiltInRegistries.ITEM.get(id));
            }
        }
        return foods;
    }

    private int percent(UnmodifiableConfig config, int fallback) {
        Object value = config.get("growthPercent");
        if (value == null) {
            return fallback;
        }
        if ((value instanceof Integer || value instanceof Long)
                && ((Number) value).longValue() >= 1 && ((Number) value).longValue() <= 100) {
            return ((Number) value).intValue();
        }
        warn("growthPercent must be a whole number from 1 to 100, got '{}'; using {}", value, fallback);
        return fallback;
    }

    private int interval(UnmodifiableConfig config, int fallback) {
        Object value = config.get("feedingIntervalSeconds");
        if (value == null) {
            return fallback;
        }
        if ((value instanceof Integer || value instanceof Long)
                && ((Number) value).longValue() >= 1 && ((Number) value).longValue() <= Integer.MAX_VALUE) {
            return ((Number) value).intValue();
        }
        warn("feedingIntervalSeconds must be a positive whole number no greater than {}, got '{}'; using {}",
                Integer.MAX_VALUE, value, fallback);
        return fallback;
    }

    private void warn(String message, Object... args) {
        FeedBabyVillagers.LOGGER.warn("{}: {}", file, MessageFormatter.arrayFormat(message, args).getMessage());
    }
}
