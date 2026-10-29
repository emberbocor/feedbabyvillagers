package io.github.emberbocor.feedbabyvillagers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(FeedBabyVillagers.MODID)
public class FeedBabyVillagers {
    public static final String MODID = "feedbabyvillagers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FeedBabyVillagers(IEventBus modEventBus, ModContainer modContainer) {
    }
}
