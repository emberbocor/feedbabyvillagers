package io.github.emberbocor.feedbabyvillagers.feeding;

import io.github.emberbocor.feedbabyvillagers.config.ServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class BabyVillagerFeeding {
    private BabyVillagerFeeding() {
    }

    public static boolean feed(Villager villager, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || !(villager.level() instanceof ServerLevel level) || !villager.isBaby()) {
            return false;
        }
        ServerConfig.Settings settings = ServerConfig.settings();
        ItemStack stack = player.getItemInHand(hand);
        if (!settings.enabled() || stack.isEmpty() || !settings.foods().contains(stack.getItem())) {
            return false;
        }

        int seconds = (int) ((float) (-villager.getAge() / 20) * (settings.growthPercent() / 100.0F));
        stack.consume(1, player);
        villager.ageUp(seconds, true);
        level.broadcastEntityEvent(villager, (byte) 14);
        villager.playSound(SoundEvents.VILLAGER_YES);
        return true;
    }
}
