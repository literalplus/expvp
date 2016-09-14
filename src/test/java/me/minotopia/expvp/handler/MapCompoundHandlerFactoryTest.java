/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import me.minotopia.expvp.EPPluginAwareTest;
import me.minotopia.expvp.api.handler.SkillHandler;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


public class MapCompoundHandlerFactoryTest extends EPPluginAwareTest {
    @Test
    public void testRootLevelDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenARootFactory()
                .withChild(new NoopSkillHandlerFactory("marc"));
        //when
        SkillHandler handler = factory.createHandler(plugin, "marc/submarc");
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    private MapCompoundHandlerFactory givenARootFactory() {
        return givenACompoundFactory("");
    }

    private MapCompoundHandlerFactory givenACompoundFactory(String baseHandlerSpec) {
        return new MapCompoundHandlerFactory(baseHandlerSpec, "/");
    }

    @Test
    public void testNamedDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenACompoundFactory("horse")
                .withChild(new NoopSkillHandlerFactory("battery"));
        //when
        SkillHandler handler = factory.createHandler(plugin, "battery/subbattery");
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testNestedDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenACompoundFactory("correct")
                .withChild(givenACompoundFactory("horse")
                        .withChild(givenACompoundFactory("battery")
                                .withChild(new NoopSkillHandlerFactory("staple"))
                        )
                );
        //when
        SkillHandler handler = factory.createHandler(plugin, "horse/battery/staple/substaple");
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testMultiChildDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenACompoundFactory("neef")
                .withChild(new NoopSkillHandlerFactory("lit"))
                .withChild(new NoopSkillHandlerFactory("hot"));
        //when
        SkillHandler handler = factory.createHandler(plugin, "lit/sublit");
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
        assertThat(handler.getHandlerSpec(), is("sublit"));
    }
}
