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

package me.minotopia.expvp.spawn;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.spawn.SpawnChangeService;
import me.minotopia.expvp.api.spawn.SpawnDisplayService;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import me.minotopia.expvp.api.spawn.button.VoteButtonService;
import me.minotopia.expvp.spawn.button.VoteButtonListener;
import me.minotopia.expvp.spawn.button.YamlVoteButtonService;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Provides the dependency wiring for the spawn module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class SpawnModule extends AbstractModule {
    @Override
    protected void configure() {
        ConfigurationSerialization.registerClass(YamlMapSpawn.class, "exp.map-spawn");
        bind(SpawnManager.class);
        bind(SpawnService.class).to(YamlSpawnService.class);
        bind(SpawnVoteService.class).to(MapSpawnVoteService.class);
        bind(SpawnChangeService.class).to(FullHourSpawnChangeService.class);
        bind(SpawnDisplayService.class).to(BossBarSpawnDisplayService.class);
        bind(VoteButtonService.class).to(YamlVoteButtonService.class);
        bind(VoteButtonListener.class);
        bind(SpawnVoteTask.class);
        bind(SpawnDayTimeTask.class);
        bind(SpawnDisplayTask.class);
    }
}
