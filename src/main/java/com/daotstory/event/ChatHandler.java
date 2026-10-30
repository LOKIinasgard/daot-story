package com.daotstory.event;

import com.daotstory.dialogue.DialogueManager;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChatHandler {

    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (!(sender instanceof ServerPlayerEntity player)) return true;

            if (!DialogueManager.hasActiveDialogue(player)) return true;

            String content = message.getContent().getString().trim();

            try {
                int choice = Integer.parseInt(content);
                DialogueManager.chooseOption(player, choice - 1); // 1 → 0
                return false; // блокируем сообщение в общий чат
            } catch (NumberFormatException e) {
                player.sendMessage(Text.literal("§cНапиши номер ответа (1, 2, 3...)"), false);
                return false;
            }
        });
    }
}
