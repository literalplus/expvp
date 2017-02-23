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
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.skill.meta.Skill;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class MapCompoundHandlerFactoryTest extends EPPluginAwareTest {
    @Test
    public void testRootLevelDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenARootFactory();
        factory.withChild(new NoopHandlerFactory(factory, "marc"));
        //when
        SkillHandler handler = factory.createHandler(plugin, skill("/marc/submarc"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    private MapCompoundHandlerFactory givenARootFactory() {
        return givenACompoundFactory(null, "");
    }

    private MapCompoundHandlerFactory givenACompoundFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        return new MapCompoundHandlerFactory(parent, ownHandlerSpec);
    }

    private Skill skill(String handlerSpec) {
        Skill skill = new Skill("topkek");
        skill.setHandlerSpec(handlerSpec);
        return skill;
    }

    @Test
    public void testNamedDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenARootFactory();
        factory.withChild(new NoopHandlerFactory(factory, "battery"));
        //when
        SkillHandler handler = factory.createHandler(plugin, skill("/battery/subbattery"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testNestedDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory root = givenARootFactory();
        MapCompoundHandlerFactory parent = givenACompoundFactory(root, "horse");
        MapCompoundHandlerFactory child = givenACompoundFactory(parent, "battery");
        root.addChild(parent);
        parent.addChild(child);
        child.addChild(new NoopHandlerFactory(child, "staple"));
        //when
        SkillHandler handler = root.createHandler(plugin, skill("/horse/battery/staple/substaple"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testMultiChildDispatch() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenARootFactory();
        factory.addChild(new NoopHandlerFactory(factory, "lit"));
        factory.addChild(new NoopHandlerFactory(factory, "hot"));
        //when
        SkillHandler handler = factory.createHandler(plugin, skill("/lit/sublit"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
        assertThat(handler.getHandlerSpec(), is("sublit"));
    }

    @Test
    public void testFullHandlerSpec__root() throws Exception {
        //given
        MapCompoundHandlerFactory factory = givenARootFactory();
        //when
        String fullSpec = factory.getFullHandlerSpec();
        //then
        assertThat(fullSpec, is(""));
    }

    @Test
    public void testFullHandlerSpec__child() throws Exception {
        //given
        MapCompoundHandlerFactory root = givenARootFactory();
        MapCompoundHandlerFactory child = givenACompoundFactory(root, "kek");
        //when
        String fullSpec = child.getFullHandlerSpec();
        //then
        assertThat(fullSpec, is("/kek"));
    }
}
