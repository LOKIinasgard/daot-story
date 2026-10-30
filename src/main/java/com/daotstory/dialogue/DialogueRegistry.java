package com.daotstory.dialogue;

import com.daotstory.event.StoryEventHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class DialogueRegistry {

    private static final Map<String, DialogueManager.DialogueTree> dialogues = new HashMap<>();

    static {
        registerMikasaFirst();
        registerArminFirst();
        registerLeviFirst();
        registerMikasaAfterFall();
    }

    public static DialogueManager.DialogueTree get(String id) {
        return dialogues.get(id);
    }

    private static void registerMikasaFirst() {
        DialogueManager.DialogueTree tree = new DialogueManager.DialogueTree();

        // Node 0
        DialogueManager.DialogueNode n0 = new DialogueManager.DialogueNode(
                "Ты... ты в порядке? Я слышала крики. Стены дрожали."
        );
        n0.options.add(new DialogueManager.DialogueOption("Я в порядке. А ты?", 1));
        n0.options.add(new DialogueManager.DialogueOption("Что произошло?!", 2));
        n0.options.add(new DialogueManager.DialogueOption("Мне нужно идти.", -1));
        tree.nodes.add(n0);

        // Node 1
        DialogueManager.DialogueNode n1 = new DialogueManager.DialogueNode(
                "Я тоже... Но мне страшно. Если стена действительно пала..."
        );
        n1.options.add(new DialogueManager.DialogueOption("Мы справимся. Вместе.", 3));
        n1.options.add(new DialogueManager.DialogueOption("Нужно бежать в стену Розы.", 3));
        tree.nodes.add(n1);

        // Node 2
        DialogueManager.DialogueNode n2 = new DialogueManager.DialogueNode(
                "Говорят, появился огромный титан... Колоссальный. Он ударил по стене."
        );
        n2.options.add(new DialogueManager.DialogueOption("Это невозможно...", 3));
        n2.options.add(new DialogueManager.DialogueOption("Мы должны сражаться.", 3));
        tree.nodes.add(n2);

        // Node 3
        DialogueManager.DialogueNode n3 = new DialogueManager.DialogueNode(
                "Я пойду с тобой. Не оставляй меня одну."
        );
        n3.options.add(new DialogueManager.DialogueOption("Хорошо. Держись рядом.", -1, player -> {
            StoryEventHandler.advanceStage(player);
            player.sendMessage(Text.literal("§a[Сюжет]§r Микаса теперь будет рядом с тобой."), false);
        }));
        tree.nodes.add(n3);

        dialogues.put("mikasa_first", tree);
    }

    private static void registerArminFirst() {
        DialogueManager.DialogueTree tree = new DialogueManager.DialogueTree();

        DialogueManager.DialogueNode n0 = new DialogueManager.DialogueNode(
                "Это... это конец? Книги говорили, что за стенами есть море... А теперь титаны здесь."
        );
        n0.options.add(new DialogueManager.DialogueOption("Мы ещё не закончили.", 1));
        n0.options.add(new DialogueManager.DialogueOption("Мне тоже страшно.", 1));
        tree.nodes.add(n0);

        DialogueManager.DialogueNode n1 = new DialogueManager.DialogueNode(
                "Тогда давай держаться вместе. У меня есть идея... но сначала нужно выжить."
        );
        n1.options.add(new DialogueManager.DialogueOption("Я с тобой, Армин.", -1, player -> {
            player.sendMessage(Text.literal("§b[Сюжет]§r Армин решил идти с тобой."), false);
        }));
        tree.nodes.add(n1);

        dialogues.put("armin_first", tree);
    }

    private static void registerLeviFirst() {
        DialogueManager.DialogueTree tree = new DialogueManager.DialogueTree();

        DialogueManager.DialogueNode n0 = new DialogueManager.DialogueNode(
                "Новенький? Глаза ещё не мертвые. Это хорошо. Или плохо."
        );
        n0.options.add(new DialogueManager.DialogueOption("Я хочу в Разведкорпус.", 1));
        n0.options.add(new DialogueManager.DialogueOption("Кто ты?", 1));
        tree.nodes.add(n0);

        DialogueManager.DialogueNode n1 = new DialogueManager.DialogueNode(
                "Капитан Леви. Если хочешь жить — слушай приказы и не мешайся под ногами. Понял?"
        );
        n1.options.add(new DialogueManager.DialogueOption("Понял.", -1));
        n1.options.add(new DialogueManager.DialogueOption("Я не просто так здесь.", -1, player -> {
            player.sendMessage(Text.literal("§7[Леви]§r ...Хм. Посмотрим."), false);
        }));
        tree.nodes.add(n1);

        dialogues.put("levi_first", tree);
    }

    private static void registerMikasaAfterFall() {
        DialogueManager.DialogueTree tree = new DialogueManager.DialogueTree();

        DialogueManager.DialogueNode n0 = new DialogueManager.DialogueNode(
                "Мы потеряли всё... Но я не позволю потерять тебя."
        );
        n0.options.add(new DialogueManager.DialogueOption("Спасибо, Микаса.", -1));
        n0.options.add(new DialogueManager.DialogueOption("Я защищу тебя.", -1));
        tree.nodes.add(n0);

        dialogues.put("mikasa_after_fall", tree);
    }
}
