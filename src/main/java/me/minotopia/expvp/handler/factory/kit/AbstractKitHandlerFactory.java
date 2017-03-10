/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import li.l1t.common.string.ArgumentFormatException;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.api.handler.factory.KitHandlerFactory;
import me.minotopia.expvp.api.handler.kit.KitHandler;
import me.minotopia.expvp.handler.factory.AbstractHandlerSpecNode;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.Material;

/**
 * Abstract base class for kit handler factories.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public abstract class AbstractKitHandlerFactory<R extends KitHandler> extends AbstractHandlerSpecNode
        implements KitHandlerFactory<R> {
    private static int SLOT_ID_INDEX = 0;
    private static int MATERIAL_INDEX = 1;
    private static int AMOUNT_INDEX = 2;

    public AbstractKitHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    @Override
    public R createHandler(EPPlugin plugin, Skill skill) throws InvalidHandlerSpecException {
        try {
            String relativeSpec = findRelativeSpec(skill);
            return createHandler(plugin, skill, new KitArgs(relativeSpec));
        } catch (ArgumentFormatException e) {
            throw new InvalidHandlerSpecException(e.getMessage(), skill.getHandlerSpec(), this);
        }
    }

    /**
     * Creates a handler using this kit handler factory's specific creation mechanism.
     *
     * @param plugin the plugin
     * @param skill  the skill to create the handler for
     * @param args   the argument list, retrieved from the skill's handler spec, where 1 is the first argument that is
     *               specifically mean for the implementation
     * @return the created kit handler for given arguments
     */
    protected abstract R createHandler(EPPlugin plugin, Skill skill, KitArgs args);

    /**
     * @param args the arguments to get the slot id from
     * @return the slot id for given arguments
     */
    protected int slotId(KitArgs args) {
        return args.intArg(SLOT_ID_INDEX);
    }

    protected Material material(KitArgs args) {
        return args.enumArg(Material.class, MATERIAL_INDEX);
    }

    protected int amount(KitArgs args) {
        return args.intArg(AMOUNT_INDEX);
    }
}
