/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.damage;

import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.handler.HandlerAdapter;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Adapter for damage handlers with no-op implementations. Provides a mechanism for probability checking through {@link
 * #isChanceMet()}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public abstract class DamageHandlerAdapter extends HandlerAdapter implements DamageHandler {
    protected static final Random RANDOM = new Random();
    private final int probabilityPerCent;

    public DamageHandlerAdapter(Skill skill, int probabilityPerCent) {
        super(skill);
        this.probabilityPerCent = probabilityPerCent;
    }

    @Override
    public void handleVictim(Player victim, Player culprit) {

    }

    @Override
    public void handleCulprit(Player culprit, Player victim) {

    }

    protected boolean isChanceMet() {
        return RANDOM.nextInt(100) > probabilityPerCent;
    }
}
