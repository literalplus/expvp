/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.yaml.AbstractYamlManager;

import java.io.File;

/**
 * Manages map spawn instances, keeping a list of all known ones by id and delegating loading
 * and saving.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
@Singleton
public class SpawnManager extends AbstractYamlManager<MapSpawn> {

    @Inject
    public SpawnManager(@DataFolder File dataFolder) {
        super(new File(dataFolder, "spawns"));
        loadAll();
    }

    @Override
    protected SpawnLoader createLoader() {
        return new SpawnLoader(this);
    }
}
