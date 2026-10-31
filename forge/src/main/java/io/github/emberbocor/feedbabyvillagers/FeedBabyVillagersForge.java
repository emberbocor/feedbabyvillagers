package io.github.emberbocor.feedbabyvillagers;

import io.github.emberbocor.feedbabyvillagers.config.ServerConfig;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(FeedBabyVillagers.MODID)
public class FeedBabyVillagersForge {
    public FeedBabyVillagersForge() {
        ServerAboutToStartEvent.BUS.addListener(event -> ServerConfig.load());
    }
}
