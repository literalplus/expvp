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

package me.minotopia.expvp.i18n;

import li.l1t.common.i18n.Message;

/**
 * Resolves message strings for names that need to be pluralised.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-02
 */
public class Plurals {
    private Plurals() {

    }

    public static Message talentPointPlural(long count) {
        return plural("score!tp", count);
    }

    public static Message killPlural(long count) {
        return plural("score!kill", count);
    }

    public static Message deathPlural(long count) {
        return plural("score!death", count);
    }

    public static Message minutePlural(long count) {
        return plural("core!minute", count);
    }

    /**
     * Resolves the correct singular/plural form for a thing. For singular (count = 1), {@code .one} is appended to the
     * base key, and for plural, {@code .many} is appended to the base key. The resolved key is then passed to the
     * {@code core!plural-base} message as a second argument, with the count as the first argument.
     *
     * @param baseKey the base key for the word
     * @param count   the actual count
     * @return a resolved message
     */
    public static Message plural(String baseKey, long count) {
        return Message.of("core!plural-base", resolveCountString(count), Message.of(resolveKey(baseKey, count)));
    }

    private static Object resolveCountString(long count) {
        return count == 0 ? Message.of("core!plural-none") : count;
    }

    private static String resolveKey(String baseKey, long count) {
        return baseKey + (count == 1 ? ".one" : ".many");
    }
}
