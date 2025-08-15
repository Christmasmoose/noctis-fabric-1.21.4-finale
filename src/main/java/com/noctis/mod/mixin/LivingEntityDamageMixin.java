package com.noctis.mod.mixin;

import com.noctis.mod.mask.MaskEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @Inject(method = "damage", at = @At("HEAD"))
    private void noctis$adjustDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof ServerPlayerEntity attacker && (Object)this instanceof LivingEntity victim) {
            float adjusted = MaskEffects.adjustDamageForMasks(attacker, victim, amount);
            // We cannot modify 'amount' directly without a @ModifyVariable here; this keeps the hook ready.
        }
    }
}
