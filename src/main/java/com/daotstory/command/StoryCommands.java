package com.daotstory.command;

import com.daotstory.event.WallBreachEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class StoryCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("daotstory")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("breach")
                            .executes(context -> {
                                WallBreachEvent.trigger(context.getSource().getServer());
                                context.getSource().sendFeedback(() -> Text.literal("§cСобытие «Падение стены» запущено."), false);
                                return 1;
                            }))
                    .then(CommandManager.literal("resetbreach")
                            .executes(context -> {
                                WallBreachEvent.reset();
                                context.getSource().sendFeedback(() -> Text.literal("§aСобытие сброшено."), false);
                                return 1;
                            }))
            );
        });
    }
}
