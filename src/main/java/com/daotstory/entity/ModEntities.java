package com.daotstory.entity;

import com.daotstory.DaotStory;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<StoryNPCEntity> MIKASA = register("mikasa", 0.6f, 1.8f);
    public static final EntityType<StoryNPCEntity> ARMIN = register("armin", 0.6f, 1.7f);
    public static final EntityType<StoryNPCEntity> LEVI = register("levi", 0.6f, 1.65f);
    public static final EntityType<StoryNPCEntity> HANJI = register("hanji", 0.6f, 1.75f);
    public static final EntityType<StoryNPCEntity> ERWIN = register("erwin", 0.6f, 1.85f);
    public static final EntityType<StoryNPCEntity> JEAN = register("jean", 0.6f, 1.8f);
    public static final EntityType<StoryNPCEntity> SASHA = register("sasha", 0.6f, 1.7f);
    public static final EntityType<StoryNPCEntity> CONNIE = register("connie", 0.6f, 1.7f);

    private static EntityType<StoryNPCEntity> register(String name, float width, float height) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(DaotStory.MOD_ID, name),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, StoryNPCEntity::new)
                        .dimensions(EntityDimensions.fixed(width, height))
                        .build()
        );
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(MIKASA, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(ARMIN, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(LEVI, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(HANJI, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(ERWIN, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(JEAN, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(SASHA, StoryNPCEntity.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(CONNIE, StoryNPCEntity.createNPCAttributes());

        DaotStory.LOGGER.info("Registered story NPCs: Mikasa, Armin, Levi, Hanji, Erwin, Jean, Sasha, Connie");
    }
}
