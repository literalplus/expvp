/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.score.KillStreakService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * It's one of these implementations where the classifier is basically just a random word so that it's not the same word
 * as the interface but {@code Simple} is boring so I chose another word for this
 * <p>Class JavaDoc would be exactly the same as the interface JavaDoc, so there's that</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
@Singleton
public class PlayerKillStreakService implements KillStreakService {
    private final Map<UUID, Integer> playerStreaks = new HashMap<>();
    private final PlayerDataService players;
    private final Server server;
    private final KitService kitService;

    @Inject
    public PlayerKillStreakService(PlayerDataService players, Server server, KitService kitService, PlayerInitService initService) {
        this.players = players;
        this.server = server;
        this.kitService = kitService;
        initService.registerDeInitHandler(this::resetStreak);
    }

    @Override
    public int getCurrentStreak(Player player) {
        Preconditions.checkNotNull(player, "player");
        return playerStreaks.getOrDefault(player.getUniqueId(), 0);
    }

    @Override
    public void increaseStreak(Player player) {
        Preconditions.checkNotNull(player, "player");
        Integer newStreak = playerStreaks.merge(player.getUniqueId(), 1, Integer::sum);
        checkIsNewBestStreak(player, newStreak);
        checkIsAFifthKill(player, newStreak);
    }

    private void checkIsNewBestStreak(Player player, Integer newStreak) {
        players.withMutable(player.getUniqueId(), playerData -> {
            if (newStreak > playerData.getBestKillStreak()) {
                playerData.setBestKillStreak(newStreak);
                I18n.sendLoc(player, "score!streak.highscore", Plurals.killPlural(newStreak));
            }
        });
    }

    private void checkIsAFifthKill(Player player, Integer newStreak) {
        if ((newStreak % 5) == 0) {
            server.getOnlinePlayers().forEach(onlinePlayer ->
                    I18n.sendLoc(onlinePlayer, "score!streak.fifth", player.getName(), newStreak)
            );
            kitService.applyKit(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 0));
        }
    }

    @Override
    public void resetStreak(Player player) {
        Preconditions.checkNotNull(player, "player");
        playerStreaks.remove(player.getUniqueId());
    }
}
