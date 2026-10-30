package com.daotstory.entity;

import com.daotstory.dialogue.DialogueManager;
import com.daotstory.event.StoryEventHandler;
import com.daotstory.quest.QuestManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class StoryNPCEntity extends PathAwareEntity {

    public StoryNPCEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createNPCAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {

            if (DialogueManager.hasActiveDialogue(serverPlayer)) {
                serverPlayer.sendMessage(Text.literal("§cСначала закончи текущий диалог."), false);
                return ActionResult.SUCCESS;
            }

            String typeKey = this.getType().getTranslationKey();
            int stage = StoryEventHandler.getStage(serverPlayer);

            if (typeKey.contains("mikasa")) {
                if (QuestManager.hasActiveQuest(serverPlayer, "talk_to_mikasa")) {
                    QuestManager.completeQuest(serverPlayer, "talk_to_mikasa");
                }
                if (stage <= 1) {
                    DialogueManager.startDialogue(serverPlayer, "Микаса", "mikasa_first");
                } else {
                    DialogueManager.startDialogue(serverPlayer, "Микаса", "mikasa_after_fall");
                }
            } else if (typeKey.contains("armin")) {
                if (QuestManager.hasActiveQuest(serverPlayer, "talk_to_armin")) {
                    QuestManager.completeQuest(serverPlayer, "talk_to_armin");
                }
                DialogueManager.startDialogue(serverPlayer, "Армин", "armin_first");
            } else if (typeKey.contains("levi")) {
                if (QuestManager.hasActiveQuest(serverPlayer, "meet_levi")) {
                    QuestManager.completeQuest(serverPlayer, "meet_levi");
                }
                DialogueManager.startDialogue(serverPlayer, "Леви", "levi_first");
            } else if (typeKey.contains("hanji")) {
                serverPlayer.sendMessage(Text.literal("§d[Ханджи]§r Ооо! Новый подопытный... то есть солдат! Расскажи, что думаешь о титанах?!"), false);
            } else if (typeKey.contains("erwin")) {
                serverPlayer.sendMessage(Text.literal("§9[Эрвин]§r Любопытство — это то, что двигает человечество вперёд. Даже ценой жизней."), false);
            } else if (typeKey.contains("jean")) {
                serverPlayer.sendMessage(Text.literal("§e[Жан]§r Ещё один герой? Не геройствуй слишком сильно, а то сдохнешь."), false);
            } else if (typeKey.contains("sasha")) {
                serverPlayer.sendMessage(Text.literal("§6[Саша]§r Есть что-нибудь поесть? Я умираю с голоду..."), false);
            } else if (typeKey.contains("connie")) {
                serverPlayer.sendMessage(Text.literal("§a[Конни]§r Йоу! Ты тоже из нашей роты? Круто!"), false);
            } else {
                serverPlayer.sendMessage(Text.literal("§e[???]§r ..."), false);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }
}
