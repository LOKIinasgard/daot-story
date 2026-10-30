package com.daotstory.event;

import com.daotstory.DaotStory;
import com.daotstory.quest.QuestManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StoryEventHandler {

    private static final Map<UUID, Integer> playerStoryStage = new HashMap<>();

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            UUID id = player.getUuid();

            if (!playerStoryStage.containsKey(id)) {
                playerStoryStage.put(id, 0);
                player.sendMessage(Text.literal("§8§l══════════════════════════════════"), false);
                player.sendMessage(Text.literal("§c§l  DAOT: Path of the Attack"), false);
                player.sendMessage(Text.literal("§7  Ты родился за стенами."), false);
                player.sendMessage(Text.literal("§7  Пока они стоят — ты в безопасности."), false);
                player.sendMessage(Text.literal("§7  Но однажды всё изменится..."), false);
                player.sendMessage(Text.literal("§8§l══════════════════════════════════"), false);

                // Стартовые квесты
                QuestManager.giveStarterQuests(player);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Здесь позже — глобальные события (падение стены и т.д.)
        });

        DaotStory.LOGGER.info("Story event handler registered.");
    }

    public static int getStage(ServerPlayerEntity player) {
        return playerStoryStage.getOrDefault(player.getUuid(), 0);
    }

    public static void setStage(ServerPlayerEntity player, int stage) {
        playerStoryStage.put(player.getUuid(), stage);
    }

    public static void advanceStage(ServerPlayerEntity player) {
        int current = getStage(player);
        setStage(player, current + 1);
        player.sendMessage(Text.literal("§a[Сюжет]§r Прогресс обновлён. Этап: §e" + (current + 1)), false);
    }
}
