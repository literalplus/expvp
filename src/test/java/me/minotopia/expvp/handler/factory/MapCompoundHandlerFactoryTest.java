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

package me.minotopia.expvp.handler.factory;

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
        TestHandlerFactory factory = givenARootFactory();
        factory.addChild(new NoopHandlerFactory(factory, "marc"));
        //when
        SkillHandler handler = factory.createHandler(skill("/marc/submarc"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    private TestHandlerFactory givenARootFactory() {
        return givenACompoundFactory(null, "");
    }

    private TestHandlerFactory givenACompoundFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        return new TestHandlerFactory(parent, ownHandlerSpec);
    }

    private Skill skill(String handlerSpec) {
        Skill skill = new Skill("topkek");
        skill.setHandlerSpec(handlerSpec);
        return skill;
    }

    @Test
    public void testNamedDispatch() throws Exception {
        //given
        TestHandlerFactory factory = givenARootFactory();
        factory.addChild(new NoopHandlerFactory(factory, "battery"));
        //when
        SkillHandler handler = factory.createHandler(skill("/battery/subbattery"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testNestedDispatch() throws Exception {
        //given
        TestHandlerFactory root = givenARootFactory();
        TestHandlerFactory parent = givenACompoundFactory(root, "horse");
        TestHandlerFactory child = givenACompoundFactory(parent, "battery");
        root.addChild(parent);
        parent.addChild(child);
        child.addChild(new NoopHandlerFactory(child, "staple"));
        //when
        SkillHandler handler = root.createHandler(skill("/horse/battery/staple/substaple"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testMultiChildDispatch() throws Exception {
        //given
        TestHandlerFactory factory = givenARootFactory();
        factory.addChild(new NoopHandlerFactory(factory, "lit"));
        factory.addChild(new NoopHandlerFactory(factory, "hot"));
        //when
        SkillHandler handler = factory.createHandler(skill("/lit/sublit"));
        //then
        assertThat(handler, is(not(nullValue())));
        assertThat(handler, is(instanceOf(NoopSkillHandler.class)));
    }

    @Test
    public void testFullHandlerSpec__root() throws Exception {
        //given
        TestHandlerFactory factory = givenARootFactory();
        //when
        String fullSpec = factory.getFullHandlerSpec();
        //then
        assertThat(fullSpec, is(""));
    }

    @Test
    public void testFullHandlerSpec__child() throws Exception {
        //given
        TestHandlerFactory root = givenARootFactory();
        TestHandlerFactory child = givenACompoundFactory(root, "kek");
        //when
        String fullSpec = child.getFullHandlerSpec();
        //then
        assertThat(fullSpec, is("/kek"));
    }
}
