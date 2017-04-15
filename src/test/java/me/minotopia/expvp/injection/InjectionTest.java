/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
