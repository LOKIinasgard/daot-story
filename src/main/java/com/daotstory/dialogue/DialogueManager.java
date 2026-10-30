package com.daotstory.dialogue;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class DialogueManager {

    // Текущий диалог игрока
    private static final Map<UUID, DialogueSession> activeSessions = new HashMap<>();

    public static void startDialogue(ServerPlayerEntity player, String npcId, String dialogueId) {
        DialogueTree tree = DialogueRegistry.get(dialogueId);
        if (tree == null) {
            player.sendMessage(Text.literal("§c[Ошибка] Диалог не найден: " + dialogueId), false);
            return;
        }

        DialogueSession session = new DialogueSession(npcId, tree, 0);
        activeSessions.put(player.getUuid(), session);
        showCurrentNode(player, session);
    }

    public static void chooseOption(ServerPlayerEntity player, int optionIndex) {
        DialogueSession session = activeSessions.get(player.getUuid());
        if (session == null) return;

        DialogueNode current = session.tree.nodes.get(session.currentNodeIndex);
        if (optionIndex < 0 || optionIndex >= current.options.size()) {
            player.sendMessage(Text.literal("§cНеверный выбор."), false);
            return;
        }

        DialogueOption option = current.options.get(optionIndex);

        // Выполняем действие выбора (если есть)
        if (option.onSelect != null) {
            option.onSelect.accept(player);
        }

        if (option.nextNodeIndex == -1) {
            // Конец диалога
            endDialogue(player);
            player.sendMessage(Text.literal("§8[Диалог завершён]"), false);
        } else {
            session.currentNodeIndex = option.nextNodeIndex;
            showCurrentNode(player, session);
        }
    }

    public static void endDialogue(ServerPlayerEntity player) {
        activeSessions.remove(player.getUuid());
    }

    public static boolean hasActiveDialogue(ServerPlayerEntity player) {
        return activeSessions.containsKey(player.getUuid());
    }

    private static void showCurrentNode(ServerPlayerEntity player, DialogueSession session) {
        DialogueNode node = session.tree.nodes.get(session.currentNodeIndex);

        player.sendMessage(Text.literal(""), false);
        player.sendMessage(Text.literal("§6§l[" + session.npcId.toUpperCase() + "]§r"), false);
        player.sendMessage(Text.literal("§f" + node.text), false);
        player.sendMessage(Text.literal(""), false);

        for (int i = 0; i < node.options.size(); i++) {
            DialogueOption opt = node.options.get(i);
            player.sendMessage(Text.literal("§e[" + (i + 1) + "] §7" + opt.text), false);
        }
        player.sendMessage(Text.literal("§8Напиши в чат номер ответа (1, 2, 3...)"), false);
    }

    // Внутренние классы
    public static class DialogueSession {
        public final String npcId;
        public final DialogueTree tree;
        public int currentNodeIndex;

        public DialogueSession(String npcId, DialogueTree tree, int start) {
            this.npcId = npcId;
            this.tree = tree;
            this.currentNodeIndex = start;
        }
    }

    public static class DialogueTree {
        public final List<DialogueNode> nodes = new ArrayList<>();
    }

    public static class DialogueNode {
        public final String text;
        public final List<DialogueOption> options = new ArrayList<>();

        public DialogueNode(String text) {
            this.text = text;
        }
    }

    public static class DialogueOption {
        public final String text;
        public final int nextNodeIndex; // -1 = конец
        public final java.util.function.Consumer<ServerPlayerEntity> onSelect;

        public DialogueOption(String text, int nextNodeIndex) {
            this(text, nextNodeIndex, null);
        }

        public DialogueOption(String text, int nextNodeIndex, java.util.function.Consumer<ServerPlayerEntity> onSelect) {
            this.text = text;
            this.nextNodeIndex = nextNodeIndex;
            this.onSelect = onSelect;
        }
    }
}
