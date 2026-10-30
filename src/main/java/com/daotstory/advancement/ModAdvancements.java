package com.daotstory.advancement;

import com.daotstory.DaotStory;
import net.minecraft.advancement.criterion.Criteria;

public class ModAdvancements {

    public static void register() {
        // Пока базовые. Позже добавим кастомные CriteriaTriggers
        // типа "wall_breached", "first_transformation", "joined_survey_corps" и т.д.

        DaotStory.LOGGER.info("Story advancements system ready.");
    }
}
