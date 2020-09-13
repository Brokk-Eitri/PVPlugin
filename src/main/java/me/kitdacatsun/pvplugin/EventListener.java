package me.kitdacatsun.pvplugin;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import static org.bukkit.Bukkit.getServer;
import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);

        player.damage(25);

        String commandLine = "team join NoTeam";
        getServer().dispatchCommand(player, commandLine);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.updateInventory();
        player.setHealth(20);
        player.setExp(0);
        player.setLevel(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        String commandLine = "team join NoTeam";
        getServer().dispatchCommand(player, commandLine);

        inGame.remove(player);
    }
}
