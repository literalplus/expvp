/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit;

import me.minotopia.expvp.api.handler.kit.KitHandler;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitElementBuilder;
import me.minotopia.expvp.handler.HandlerAdapter;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.Material;

/**
 * A simple kit handler that just adds an item with amount.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class SimpleKitHandler extends HandlerAdapter implements KitHandler {
    private final int slotId;
    private final Material type;
    private final int amount;

    public SimpleKitHandler(Skill skill, int slotId, Material type, int amount) {
        super(skill);
        this.slotId = slotId;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void handle(KitCompilation compilation) {
        element(compilation).addToAmount(amount);
    }

    /**
     * @param compilation the compilation to get the element from
     * @return the element this handler targets in given compilation
     * @see KitCompilation#slot(int, Material)
     */
    protected KitElementBuilder element(KitCompilation compilation) {
        return compilation.slot(slotId, type);
    }

    @Override
    public int getSlotId() {
        return slotId;
    }

    @Override
    public Material getType() {
        return type;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ">" + (getSkill() == null ? "null" : getSkill().getId());
    }
}
