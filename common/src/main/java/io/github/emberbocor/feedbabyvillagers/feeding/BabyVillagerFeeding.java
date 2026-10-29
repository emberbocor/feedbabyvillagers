package io.github.emberbocor.feedbabyvillagers.feeding;

import io.github.emberbocor.feedbabyvillagers.config.ServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
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

        stack.consume(1, player);
        grow(villager, level, settings.growthPercent());
        return true;
    }

    public static boolean wantsFood(Villager villager, ItemStack stack) {
        if (!(villager.level() instanceof ServerLevel) || !villager.isBaby()) {
            return false;
        }
        ServerConfig.Settings settings = ServerConfig.settings();
        return settings.enabled() && !stack.isEmpty() && settings.foods().contains(stack.getItem())
                && villager.getInventory().canAddItem(stack);
    }

    public static long tickInventoryFeeding(Villager villager, long feedingTicks) {
        if (!(villager.level() instanceof ServerLevel level) || !villager.isAlive()
                || !villager.isBaby() || villager.isSleeping()) {
            return 0;
        }
        ServerConfig.Settings settings = ServerConfig.settings();
        if (!settings.enabled()) {
            return 0;
        }

        SimpleContainer inventory = villager.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && settings.foods().contains(stack.getItem())) {
                long nextTicks = feedingTicks + 1;
                if (nextTicks < settings.feedingIntervalSeconds() * 20L) {
                    return nextTicks;
                }
                inventory.removeItem(slot, 1);
                grow(villager, level, settings.growthPercent());
                return 0;
            }
        }
        return 0;
    }

    private static void grow(Villager villager, ServerLevel level, int growthPercent) {
        int seconds = (int) ((float) (-villager.getAge() / 20) * (growthPercent / 100.0F));
        villager.ageUp(seconds, true);
        level.broadcastEntityEvent(villager, (byte) 14);
        villager.playSound(SoundEvents.VILLAGER_YES);
    }
}
