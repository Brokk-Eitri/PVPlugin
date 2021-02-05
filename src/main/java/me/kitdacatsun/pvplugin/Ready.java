package me.kitdacatsun.pvplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class Ready implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player)sender;
        if (ready.contains(player)) {
            sender.sendMessage("Already ready");
            return true;
        }

        ready.add(player);

        for (Player p : players) {
            p.sendMessage("Ready Players: " + ready.size() + "/" + Math.max(players.size(), 2));
        }

        if (ready.size() >= Math.max(players.size(), 2)) {
            for (Player p : players) {
                for (int i = 0; i < 5; i++) {
                    String number = String.valueOf(5 - i);
                    server.getScheduler().scheduleSyncDelayedTask(plugin, () -> sendTitle(p, number, ready.size() + " players"), i * 20);
                }
                server.getScheduler().scheduleSyncDelayedTask(plugin, () -> sendTitle(p, "Go", ready.size() + " players"), 5 * 20);
            }

            server.getScheduler().scheduleSyncDelayedTask(plugin, PVPlugin::startGame, 5 * 20);
        }

        return true;
    }

    private void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 0, 15, 5);
    }
}
