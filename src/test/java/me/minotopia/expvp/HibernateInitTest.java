/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import org.junit.Test;

import java.io.IOException;

public class HibernateInitTest extends HibernateAwareTest {
    @Test
    public void testInitHibernate() throws IOException {
        //given
        EPPlugin plugin = givenAPluginInstance();
        //when
        whenHibernateIsInitialisedOn(plugin);
        //then no exception should be thrown
    }
}
