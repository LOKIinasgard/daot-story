package com.daotstory.quest;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class Quest {

    public final String id;
    public final String title;
    public final String description;
    public final Consumer<ServerPlayerEntity> onStart;
    public final Consumer<ServerPlayerEntity> onComplete;

    public Quest(String id, String title, String description,
                 Consumer<ServerPlayerEntity> onStart,
                 Consumer<ServerPlayerEntity> onComplete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.onStart = onStart;
        this.onComplete = onComplete;
    }

    public void start(ServerPlayerEntity player) {
        player.sendMessage(Text.literal(""), false);
        player.sendMessage(Text.literal("§6§l[Новый квест]§r §e" + title), false);
        player.sendMessage(Text.literal("§7" + description), false);
        player.sendMessage(Text.literal(""), false);
        if (onStart != null) onStart.accept(player);
    }

    public void complete(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("§a§l[Квест выполнен]§r §e" + title), false);
        if (onComplete != null) onComplete.accept(player);
    }
}
