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

package me.minotopia.expvp.chat;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.chat.AdFilterService;
import li.l1t.common.chat.CapsFilterService;
import li.l1t.common.yaml.XyConfiguration;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages the configuration file of the chat module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-03
 */
@Singleton
public class ChatConfig extends XyConfiguration {
    private final Logger LOGGER = LoggingManager.getLogger(ChatConfig.class);

    @Inject
    public ChatConfig(@DataFolder File dataFolder) throws IOException {
        super(new File(dataFolder, "chat.cfg.yml"));
        tryLoad();
        options().copyDefaults(true);
    }

    public void configure(AdFilterService ads) {
        addDefault("ads.aggressive-dot-matching", true);
        addDefault("ads.match-ip-addresses", true);
        addDefault("ads.ignored-domains", Lists.newArrayList());
        ads.setFindHiddenDots(getBoolean("ads.aggressive-dot-matching"));
        ads.setFindIpAddresses(getBoolean("ads.match-ip-addresses"));
        ads.addIgnoredDomains(getStringListOrSet("ads.ignored-domains", "minotopia.me", "expvp.eu", "l1t.li"));
    }

    private List<String> getStringListOrSet(String path, String... defaults) {
        List<String> result = getStringList(path);
        if (result.isEmpty()) {
            result = Lists.newArrayList(defaults);
            set(path, result);
            trySave();
        }
        return result;
    }

    public int getMockMessageCount() {
        return getIntOrSet("ad.mock-message-count", 5);
    }

    private int getIntOrSet(String path, int def) {
        int value = getInt(path, -1);
        if (value <= 0) {
            value = def;
            set(path, value);
            trySave();
        }
        return value;
    }

    public void configure(CapsFilterService capsFilterService) {
        float capsFactor = ((float) getIntOrSet("caps.max-percent-caps", 50)) / 100F;
        int ignoreUntilLength = getIntOrSet("caps.ignore-messages-shorter-than-characters", 5);
        capsFilterService.setCapsFactor(capsFactor);
        capsFilterService.setIgnoreUntilLength(ignoreUntilLength);
    }
}
