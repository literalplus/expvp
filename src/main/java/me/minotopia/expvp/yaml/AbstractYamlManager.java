/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.yaml;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages YAML-backed objects in-memory and delegates loading.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public abstract class AbstractYamlManager<T> implements YamlManager<T> {
    private final File directory;
    private final YamlLoader<T> loader;
    private final Map<String, T> registry = new HashMap<>();
    private final Collection<T> registryView = Collections.unmodifiableCollection(registry.values());

    public AbstractYamlManager(File directory) {
        this.directory = directory;
        this.loader = createLoader();
    }

    @Override
    public void loadAll() {
        registry.clear();
        loader.loadFromDirectory()
                .forEach(tree -> registry.put(getId(tree), tree));
    }

    @Override
    public File getDirectory() {
        return directory;
    }

    /**
     * @return the loader associated with this manager
     */
    YamlLoader<T> getLoader() {
        return loader;
    }

    @Override
    public Collection<T> getAll() {
        return registryView;
    }

    @Override
    public T get(String objId) {
        return registry.get(objId);
    }

    @Override
    public boolean contains(String objId) {
        return registry.containsKey(objId);
    }

    @Override
    public T create(String objId) throws IOException {
        T obj = loader.create(objId);
        registry.put(getId(obj), obj);
        return obj;
    }

    @Override
    public void save(T obj) throws IOException {
        loader.saveToFile(obj);
    }

    /**
     * Creates a new loader for this object type. Not a constructor parameter because you cannot
     * reference this before calling the supertype constructor.
     * @return a new loader for this object type
     */
    protected abstract YamlLoader<T> createLoader();
}
