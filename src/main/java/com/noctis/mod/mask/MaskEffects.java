package com.noctis.mod.mask;

import com.noctis.mod.util.Chance;
import com.noctis.mod.util.CombatUtil;
import com.noctis.mod.util.Cooldowns;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public final class MaskEffects {
    private static final Map<UUID, Long> STORM_PER_TARGET_COOLDOWN = new HashMap<>();
    private static long tickCounter = 0;

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, target, hitResult) -> {
            if (!(player instanceof ServerPlayerEntity sp) || !(target instanceof LivingEntity victim)) {
                return ActionResult.PASS;
            }
            var mask = CombatUtil.getWornMask(sp);
            if (mask == null) return ActionResult.PASS;

            switch (mask.getMaskType()) {
                case FLAME -> applyFlame(sp, victim, world);
                case FROST -> victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 4 * 20, 1));
                case STORM -> applyStormOnCrit(sp, victim, world);
                case BLOOM -> { if (Chance.roll(0.08)) victim.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 3*20, 0)); }
                case BLOOD -> CombatUtil.heal(sp, 0.5f);
                case HUNTER -> applyHunterMark(sp, victim);
                default -> {}
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.START_SERVER_TICK.register(MaskEffects::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;
        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity sp : world.getPlayers()) {
                var mask = CombatUtil.getWornMask(sp);
                if (mask == null) continue;
                switch (mask.getMaskType()) {
                    case RAGE -> {
                        if (sp.getHealth() <= sp.getMaxHealth() * 0.5f) {
                            sp.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30, 1, true, false));
                            sp.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 30, 0, true, false));
                        }
                    }
                    case SUN -> { if (sp.getWorld().isDay()) {
                        sp.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 30, 0, true, false));
                        sp.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30, 0, true, false));
                    }}
                    case MOON -> { if (!sp.getWorld().isDay()) {
                        sp.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 30, 0, true, false));
                        sp.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30, 0, true, false));
                    }}
                    case BLOOM -> {
                        var key = world.getBiome(sp.getBlockPos()).getKey().orElse(null);
                        if (key != null) {
                            String path = key.getValue().getPath();
                            if (path.contains("flower_forest") || path.contains("swamp")) {
                                sp.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0, true, false));
                            }
                        }
                    }
                    case VOID -> tickVoid(sp);
                    default -> {}
                }
                if (mask.getMaskType() == MaskType.STORM) tickStormTridents(world, sp);
            }
        }
    }

    private static void tickVoid(ServerPlayerEntity sp) {
        if (sp.fallDistance > 2 && sp.getVelocity().y < -0.2) {
            sp.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 10, 0, true, false));
        }
    }

    private static boolean isCritical(PlayerEntity p) {
        return p.fallDistance > 0.0F && !p.isOnGround() && !p.isClimbing() && !p.isTouchingWater() && !p.hasStatusEffect(StatusEffects.BLINDNESS) && !p.isSprinting();
    }

    private static void applyFlame(ServerPlayerEntity sp, LivingEntity victim, World world) {
        victim.setOnFireFor(6);
        if (isCritical(sp)) {
            Vec3d pos = victim.getPos();
            var around = world.getEntitiesByClass(LivingEntity.class, new Box(pos.add(-2, -2, -2), pos.add(2, 2, 2)), e -> e != sp && e.isAlive());
            for (LivingEntity e : around) {
                e.damage(world.getDamageSources().playerAttack(sp), 2f);
                e.setOnFireFor(4);
            }
            world.playSound(null, victim.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, sp.getSoundCategory(), 1f, 1f);
        }
    }

    private static void applyStormOnCrit(ServerPlayerEntity sp, LivingEntity victim, World world) {
        if (!isCritical(sp)) return;
        if (!Chance.roll(0.08)) return;
        long now = System.currentTimeMillis();
        long until = STORM_PER_TARGET_COOLDOWN.getOrDefault(victim.getUuid(), 0L);
        if (now < until) return;
        STORM_PER_TARGET_COOLDOWN.put(victim.getUuid(), now + 30_000L);
        summonLightning(world, victim.getPos());
    }

    private static void tickStormTridents(ServerWorld world, ServerPlayerEntity sp) {
        var tridents = world.getEntitiesByClass(TridentEntity.class, sp.getBoundingBox().expand(48), t -> t.getOwner() == sp && t.isAlive());
        for (TridentEntity t : tridents) {
            if (t.isNoClip() || t.inGround) {
                if (Cooldowns.ready(t, "storm_trident_once", 10_000)) {
                    if (Chance.roll(0.20)) summonLightning(world, t.getPos());
                }
            }
        }
    }

    private static void summonLightning(World world, Vec3d pos) {
        if (world instanceof ServerWorld sw) {
            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(sw);
            if (bolt != null) { bolt.refreshPositionAfterTeleport(pos); sw.spawnEntity(bolt); }
        }
    }

    private static void applyHunterMark(ServerPlayerEntity sp, LivingEntity victim) {
        victim.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10 * 20, 0));
        victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2 * 20, 4));
        Cooldowns.set(victim, "noctis_marked_by_" + sp.getUuidAsString(), 10_000);
    }

    // Adjuster used by mixin
    public static float adjustDamageForMasks(ServerPlayerEntity attacker, LivingEntity victim, float amount) {
        var atkMask = CombatUtil.getWornMask(attacker);
        float out = amount;
        if (atkMask != null && atkMask.getMaskType() == MaskType.ROGUE) {
            boolean invis = attacker.hasStatusEffect(StatusEffects.INVISIBILITY);
            boolean glowing = attacker.hasStatusEffect(StatusEffects.GLOWING);
            if (invis && !glowing) out *= 1.15f;
        }
        if (victim.hasStatusEffect(StatusEffects.GLOWING)) out *= 1.15f; // hunter mark simple check
        if (atkMask != null && atkMask.getMaskType() == MaskType.GLASS) out *= 1.10f;
        return out;
    }
}
