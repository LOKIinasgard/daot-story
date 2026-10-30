package com.daotstory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daotstory.advancement.ModAdvancements;
import com.daotstory.command.StoryCommands;
import com.daotstory.entity.ModEntities;
import com.daotstory.event.ChatHandler;
import com.daotstory.event.StoryEventHandler;

public class DaotStory implements ModInitializer {

    public static final String MOD_ID = "daot-story";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("DAOT: Path of the Attack — initializing story campaign...");

        // Register entities (NPCs)
        ModEntities.register();

        // Register advancements / criteria
        ModAdvancements.register();

        // Story event handler
        StoryEventHandler.register();

        // Chat handler for dialogue choices
        ChatHandler.register();

        // Commands
        StoryCommands.register();

        // Server started
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("Story campaign ready. The walls will fall...");
        });

        LOGGER.info("DAOT Story loaded successfully.");
    }
}
