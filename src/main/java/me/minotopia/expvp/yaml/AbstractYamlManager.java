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

package me.minotopia.expvp.yaml;

import me.minotopia.expvp.api.Identifiable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages YAML-backed objects in-memory and delegates loading.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public abstract class AbstractYamlManager<T extends Identifiable> implements YamlManager<T> {
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
                .forEach(object -> registry.put(object.getId(), object));
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
        registry.put(obj.getId(), obj);
        return obj;
    }

    @Override
    public void save(T obj) throws IOException {
        loader.saveToFile(obj);
    }

    @Override
    public void remove(T obj) throws IOException {
        loader.delete(obj.getId());
        if (!registry.remove(obj.getId(), obj)) {
            throw new IllegalStateException("object does not exist: " + obj.getId());
        }
    }

    /**
     * Creates a new loader for this object type. Not a constructor parameter because you cannot
     * reference this before calling the supertype constructor.
     *
     * @return a new loader for this object type
     */
    protected abstract YamlLoader<T> createLoader();
}
