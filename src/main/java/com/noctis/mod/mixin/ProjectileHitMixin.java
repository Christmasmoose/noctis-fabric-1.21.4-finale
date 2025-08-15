package com.noctis.mod.mixin;

import com.noctis.mod.mask.MaskItem;
import com.noctis.mod.mask.MaskType;
import com.noctis.mod.util.CombatUtil;
import com.noctis.mod.util.Chance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileHitMixin {
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void noctis$reflect(EntityHitResult hit, CallbackInfo ci) {
        Entity target = hit.getEntity();
        if (target instanceof PlayerEntity p) {
            MaskItem mi = CombatUtil.getWornMask(p);
            if (mi != null && mi.getMaskType() == MaskType.GLASS && Chance.roll(0.4)) {
                PersistentProjectileEntity proj = (PersistentProjectileEntity)(Object)this;
                proj.setVelocity(proj.getVelocity().multiply(-1));
                ci.cancel();
            }
        }
    }
}
