package io.github.emberbocor.feedbabyvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.emberbocor.feedbabyvillagers.feeding.BabyVillagerFeeding;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;isBaby()Z"), cancellable = true)
    private void feedbabyvillagers$feedBaby(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (BabyVillagerFeeding.feed((Villager) (Object) this, player, hand)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
