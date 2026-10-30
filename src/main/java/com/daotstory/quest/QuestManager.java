package com.daotstory.quest;

import com.daotstory.event.StoryEventHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class QuestManager {

    private static final Map<UUID, Set<String>> activeQuests = new HashMap<>();
    private static final Map<UUID, Set<String>> completedQuests = new HashMap<>();
    private static final Map<String, Quest> registeredQuests = new HashMap<>();

    static {
        registerAll();
    }

    private static void registerAll() {
        // Квест 1: Поговорить с Армином
        registeredQuests.put("talk_to_armin", new Quest(
                "talk_to_armin",
                "Первый друг",
                "Найди Армина и поговори с ним.",
                null,
                player -> {
                    player.sendMessage(Text.literal("§bАрмин теперь твой союзник."), false);
                    StoryEventHandler.advanceStage(player);
                }
        ));

        // Квест 2: Поговорить с Микасой
        registeredQuests.put("talk_to_mikasa", new Quest(
                "talk_to_mikasa",
                "Та, что всегда рядом",
                "Найди Микасу. Она ищет тебя.",
                null,
                player -> {
                    player.sendMessage(Text.literal("§cМикаса будет защищать тебя."), false);
                }
        ));

        // Квест 3: Добраться до безопасной зоны (пока просто стадия)
        registeredQuests.put("reach_safety", new Quest(
                "reach_safety",
                "Беги к стене Розы",
                "Стена Марии пала. Доберись до относительно безопасного места.",
                null,
                player -> {
                    player.sendMessage(Text.literal("§eТы добрался. Но это только начало."), false);
                    StoryEventHandler.advanceStage(player);
                }
        ));

        // Квест 4: Встретить Леви
        registeredQuests.put("meet_levi", new Quest(
                "meet_levi",
                "Капитан",
                "Говорят, в Разведкорпусе есть человек, которого боятся сами титаны. Найди его.",
                null,
                player -> {
                    player.sendMessage(Text.literal("§7Леви заметил тебя."), false);
                }
        ));
    }

    public static void startQuest(ServerPlayerEntity player, String questId) {
        Quest quest = registeredQuests.get(questId);
        if (quest == null) return;

        UUID id = player.getUuid();
        activeQuests.computeIfAbsent(id, k -> new HashSet<>());
        completedQuests.computeIfAbsent(id, k -> new HashSet<>());

        if (completedQuests.get(id).contains(questId) || activeQuests.get(id).contains(questId)) {
            return;
        }

        activeQuests.get(id).add(questId);
        quest.start(player);
    }

    public static void completeQuest(ServerPlayerEntity player, String questId) {
        UUID id = player.getUuid();
        if (!activeQuests.containsKey(id) || !activeQuests.get(id).contains(questId)) return;

        activeQuests.get(id).remove(questId);
        completedQuests.computeIfAbsent(id, k -> new HashSet<>()).add(questId);

        Quest quest = registeredQuests.get(questId);
        if (quest != null) {
            quest.complete(player);
        }
    }

    public static boolean hasActiveQuest(ServerPlayerEntity player, String questId) {
        Set<String> active = activeQuests.get(player.getUuid());
        return active != null && active.contains(questId);
    }

    public static boolean isCompleted(ServerPlayerEntity player, String questId) {
        Set<String> completed = completedQuests.get(player.getUuid());
        return completed != null && completed.contains(questId);
    }

    public static void giveStarterQuests(ServerPlayerEntity player) {
        startQuest(player, "talk_to_armin");
        startQuest(player, "talk_to_mikasa");
    }
}
