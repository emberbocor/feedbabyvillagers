package io.github.emberbocor.feedbabyvillagers;

import io.github.emberbocor.feedbabyvillagers.config.ServerConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Mod(FeedBabyVillagers.MODID)
public class FeedBabyVillagersNeoForge {
    public FeedBabyVillagersNeoForge() {
        NeoForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> ServerConfig.load());
    }
}
