package me.kitdacatsun.pvplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static me.kitdacatsun.pvplugin.EndGame.endGame;
import static me.kitdacatsun.pvplugin.PVPlugin.*;
import static org.bukkit.Bukkit.getServer;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        resetPlayer(player);
        player.setBedSpawnLocation(lobby);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        resetPlayer(player);
        inGame.remove(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (inGame.contains(event.getEntity())) {
            getServer().broadcastMessage(event.getEntity().getName() + " was killed");
        }

        inGame.remove(event.getEntity());

        int teamsWithPlayers = 0;
        String teamName = "";
        for (Team team : teams) {
            if (team.players.size() > 0) {
                teamsWithPlayers++;
                teamName = team.name;
            } else if (team.inPlay) {
                getServer().broadcastMessage("Team " + team.name + " is out");
                team.inPlay = false;
            }
        }

        if (teamsWithPlayers == 1) {
            getServer().broadcastMessage(teamName + " has won!");
            endGame();
        }
    }
}
