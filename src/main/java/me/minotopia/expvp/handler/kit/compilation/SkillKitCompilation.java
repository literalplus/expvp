/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.handler.kit.compilation;

import com.google.common.base.Preconditions;
import li.l1t.common.collections.Pair;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import me.minotopia.expvp.api.handler.kit.compilation.KitElementBuilder;
import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A kit compilation for kits based on players' skill sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SkillKitCompilation implements KitCompilation {
    private final Player player;
    private final PlayerData playerData;
    private final Map<Integer, KitElementBuilder> slotMap = new HashMap<>();

    public SkillKitCompilation(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
    }

    @Override
    public Player getTargetPlayer() {
        return player;
    }

    @Override
    public PlayerData getTargetData() {
        return playerData;
    }

    @Override
    public Map<Integer, KitElement> getResult() {
        return slotMap.entrySet().stream()
                .map(e -> Pair.pairOf(e.getKey(), e.getValue().build()))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Override
    public KitElementBuilder slot(int slotId, Material type) {
        Preconditions.checkElementIndex(slotId, 40,
                "Bukkit player slot ids are between 0, inclusive, and 39, inclusive.");
        return slotMap.computeIfAbsent(slotId, id -> new ItemKitElementBuilder(type, id));
    }

    @Override
    public KitElement snapshotOf(int slotId) {
        return Optional.ofNullable(slotMap.get(slotId))
                .map(KitElementBuilder::build)
                .orElse(new SimpleKitElement(new ItemStack(Material.AIR)));
    }
}
