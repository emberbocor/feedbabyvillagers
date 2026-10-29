package io.github.emberbocor.feedbabyvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.emberbocor.feedbabyvillagers.feeding.BabyVillagerFeeding;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Unique
    private long feedbabyvillagers$inventoryFeedingTicks;

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;isBaby()Z"), cancellable = true)
    private void feedbabyvillagers$feedBaby(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (BabyVillagerFeeding.feed((Villager) (Object) this, player, hand)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "wantsToPickUp", at = @At("RETURN"), cancellable = true)
    private void feedbabyvillagers$wantFood(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (BabyVillagerFeeding.wantsFood((Villager) (Object) this, stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void feedbabyvillagers$eatStoredFood(CallbackInfo ci) {
        feedbabyvillagers$inventoryFeedingTicks = BabyVillagerFeeding.tickInventoryFeeding(
                (Villager) (Object) this, feedbabyvillagers$inventoryFeedingTicks);
    }
}
