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

package me.minotopia.expvp.extimes;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.extimes.ExTimesConfig;
import me.minotopia.expvp.api.extimes.ExTimesService;
import me.minotopia.expvp.extimes.cfg.YamlExTimesConfig;

/**
 * Provides dependency wiring for the ExTimes modules.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public class ExTimesModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExTimesConfig.class).to(YamlExTimesConfig.class);
        bind(ExTimesService.class).to(YamlExTimesService.class);
        bind(ExTimesJoinListener.class);
    }
}
