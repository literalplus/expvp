/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
