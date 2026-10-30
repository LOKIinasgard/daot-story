package com.daotstory.event;

import com.daotstory.DaotStory;
import com.daotstory.quest.QuestManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;

public class WallBreachEvent {

    private static boolean hasTriggered = false;

    public static void trigger(MinecraftServer server) {
        if (hasTriggered) return;
        hasTriggered = true;

        DaotStory.LOGGER.info("WALL BREACH EVENT TRIGGERED");

        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        if (players.isEmpty()) return;

        // Берём первого игрока как точку отсчёта
        ServerPlayerEntity mainPlayer = players.get(0);
        ServerWorld world = mainPlayer.getServerWorld();
        BlockPos playerPos = mainPlayer.getBlockPos();

        // Спавним Колоссального титана в ~80 блоках от игрока
        spawnColossalTitan(world, playerPos);

        for (ServerPlayerEntity player : players) {
            // Эффекты
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 20 * 10, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 7, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 6, 1));

            // Звуки
            player.playSoundToPlayer(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.MASTER, 1.2f, 0.4f);
            player.playSoundToPlayer(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.MASTER, 1.0f, 0.6f);
            player.playSoundToPlayer(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.MASTER, 0.7f, 0.5f);

            // Сообщения
            player.sendMessage(Text.literal(""), false);
            player.sendMessage(Text.literal("§c§l════════════════════════════════════════"), false);
            player.sendMessage(Text.literal("§4§l            СТЕНЫ ПАЛИ"), false);
            player.sendMessage(Text.literal("§c§l════════════════════════════════════════"), false);
            player.sendMessage(Text.literal(""), false);
            player.sendMessage(Text.literal("§fНебо потемнело."), false);
            player.sendMessage(Text.literal("§fОгромная фигура поднялась над стеной."), false);
            player.sendMessage(Text.literal("§fОдин удар — и камень разлетелся, как стекло."), false);
            player.sendMessage(Text.literal("§fТитаны хлынули внутрь."), false);
            player.sendMessage(Text.literal(""), false);
            player.sendMessage(Text.literal("§7Твой мир больше никогда не будет прежним."), false);
            player.sendMessage(Text.literal(""), false);

            // Прогресс
            StoryEventHandler.setStage(player, Math.max(StoryEventHandler.getStage(player), 2));
            QuestManager.startQuest(player, "reach_safety");

            player.sendMessage(Text.literal("§6[Достижение]§r Акт 1 — Падение"), false);
        }
    }

    private static void spawnColossalTitan(ServerWorld world, BlockPos nearPos) {
        // Пробуем разные возможные ID
        String[] possibleIds = {
                "dannys-aot:colossal_titan",
                "dannys-aot:colossaltitan",
                "dannys_aot:colossal_titan",
                "daot:colossal_titan"
        };

        EntityType<?> colossalType = null;
        for (String id : possibleIds) {
            Optional<EntityType<?>> opt = Registries.ENTITY_TYPE.getOrEmpty(Identifier.tryParse(id));
            if (opt.isPresent()) {
                colossalType = opt.get();
                DaotStory.LOGGER.info("Found Colossal Titan entity: " + id);
                break;
            }
        }

        if (colossalType == null) {
            DaotStory.LOGGER.warn("Could not find Colossal Titan entity type from Danny's AoT. Spawning skipped.");
            // Fallback сообщение всем игрокам
            world.getPlayers().forEach(p -> 
                p.sendMessage(Text.literal("§c[!] Колоссальный титан не найден. Убедись, что Danny's AoT загружен."), false)
            );
            return;
        }

        // Позиция спавна — в 60-90 блоках по горизонтали, высоко
        double angle = world.random.nextDouble() * Math.PI * 2;
        double dist = 70 + world.random.nextDouble() * 30;
        double x = nearPos.getX() + Math.cos(angle) * dist;
        double z = nearPos.getZ() + Math.sin(angle) * dist;
        double y = nearPos.getY() + 40; // высоко над землёй

        Entity colossal = colossalType.create(world);
        if (colossal != null) {
            colossal.refreshPositionAndAngles(x, y, z, 0, 0);
            world.spawnEntity(colossal);
            DaotStory.LOGGER.info("Colossal Titan spawned at " + x + ", " + y + ", " + z);
        }
    }

    public static boolean hasTriggered() {
        return hasTriggered;
    }

    public static void reset() {
        hasTriggered = false;
    }
}
