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

package me.minotopia.expvp.chat.glomu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.chat.GlobalMuteService;

import java.util.Optional;

/**
 * A simple global mute service.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
@Singleton
public class SimpleGlobalMuteService implements GlobalMuteService {
    private boolean enabled = false;
    private String reason = null;

    @Inject
    public SimpleGlobalMuteService() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
