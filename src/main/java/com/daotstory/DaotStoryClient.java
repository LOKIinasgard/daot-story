package com.daotstory;

import com.daotstory.entity.ModEntities;
import com.daotstory.entity.StoryNPCEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DaotStoryClient implements ClientModInitializer {

    private static final Identifier STEVE = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");

    @Override
    public void onInitializeClient() {
        register(ModEntities.MIKASA);
        register(ModEntities.ARMIN);
        register(ModEntities.LEVI);
        register(ModEntities.HANJI);
        register(ModEntities.ERWIN);
        register(ModEntities.JEAN);
        register(ModEntities.SASHA);
        register(ModEntities.CONNIE);
        DaotStory.LOGGER.info("DAOT Story client initialized.");
    }

    private static void register(EntityType<StoryNPCEntity> type) {
        EntityRendererRegistry.register(type, ctx ->
                new BipedEntityRenderer<StoryNPCEntity, BipedEntityModel<StoryNPCEntity>>(
                        ctx, new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER)), 0.5f) {
                    @Override
                    public Identifier getTexture(StoryNPCEntity entity) {
                        return STEVE;
                    }
                });
    }
}
