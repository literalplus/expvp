/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.extimes.cfg;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.yaml.XyConfiguration;
import me.minotopia.expvp.api.extimes.ExTimesConfig;
import me.minotopia.expvp.api.inject.DataFolder;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/**
 * An ExTimes configuration backed by a YAML configuration file.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
@Singleton
public class YamlExTimesConfig extends XyConfiguration implements ExTimesConfig {
    private List<YamlSpecialExTime> specialTimes;
    private List<YamlDOWExTime> weekendTimes;
    private List<YamlDOWExTime> weekTimes;

    @Inject
    protected YamlExTimesConfig(@DataFolder File dataFolder) {
        super(new File(dataFolder, "extimes.stor.yml"));
        tryLoad();
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);
        this.specialTimes = getListChecked("special", YamlSpecialExTime.class);
        this.weekendTimes = getListChecked("weekend", YamlDOWExTime.class);
        this.weekTimes = getListChecked("week", YamlDOWExTime.class);
        sortAll();
    }

    private void sortAll() {
        specialTimes.sort(Comparator.comparing(AbstractExTime::getStart));
        weekendTimes.sort(Comparator.comparing(AbstractExTime::getStart));
        weekTimes.sort(Comparator.comparing(AbstractExTime::getStart));
    }

    @Override
    public List<YamlSpecialExTime> getSpecialTimes() {
        return specialTimes;
    }

    @Override
    public YamlSpecialExTime addSpecialTime(LocalDate date, LocalTime start, LocalTime end) {
        YamlSpecialExTime result = new YamlSpecialExTime(date, start, end);
        specialTimes.add(result);
        sortAll();
        trySave();
        return result;
    }

    @Override
    public List<YamlDOWExTime> getWeekendTimes() {
        return weekendTimes;
    }

    @Override
    public YamlDOWExTime addWeekendTime(LocalTime start, LocalTime end) {
        YamlDOWExTime result = new YamlDOWExTime(start, end);
        weekendTimes.add(result);
        sortAll();
        trySave();
        return result;
    }

    @Override
    public List<YamlDOWExTime> getWeekTimes() {
        return weekTimes;
    }

    @Override
    public YamlDOWExTime addWeekTime(LocalTime start, LocalTime end) {
        YamlDOWExTime result = new YamlDOWExTime(start, end);
        weekTimes.add(result);
        sortAll();
        trySave();
        return result;
    }
}
