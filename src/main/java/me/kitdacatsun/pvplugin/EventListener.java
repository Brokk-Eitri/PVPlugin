package me.kitdacatsun.pvplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(lobby);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (gameInProgress) {
            players.remove(player);

            for (Team team : teams) {
                team.members.remove(player);
                if (team.members.size() == 0 && !defeated.contains(team)) {
                    defeated.add(team);
                    for (Player p : players) {
                        p.sendTitle("Team " + team.name + " has been defeated!", null, 0, 15, 5);
                    }

                    if (defeated.size() >= teams.size() - 1) {
                        endGame();
                    }
                }
            }
        }

    }
}
