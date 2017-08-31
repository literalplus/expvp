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

package me.minotopia.expvp.injection;


import com.google.inject.Guice;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.EPPluginAwareTest;
import me.minotopia.expvp.EPRootModule;
import me.minotopia.expvp.command.CommandsModule;
import org.junit.Test;

public class InjectionTest extends EPPluginAwareTest {
    @Test
    public void testInjection() {
        //given
        EPPlugin plugin = givenAPluginInstance();
        //when
        Guice.createInjector(new EPRootModule(plugin), new CommandsModule());
        //then no error is thrown
    }
}
