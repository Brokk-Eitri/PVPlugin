package me.kitdacatsun.pvplugin;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class Ready implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            ready((Player)sender);
        }

        for (Player player : inGame) {
            if (player != sender) {
                player.sendMessage(sender.getName() + " is ready (" + ready.size() + "/" + inGame.size() + ")");
            } else {
                sender.sendMessage("You are ready (" + ready.size() + "/" + inGame.size() + ")");
            }
        }

        return true;
    }

    private void ready(Player player) {
        if (ready.contains(player) || !inGame.contains(player)) {
            return;
        }

        ready.add(player);

        if (ready.size() == inGame.size() && inGame.size() >= 2) {
            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> setTitle("3"), 0);
            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> setTitle("2"), 20);
            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> setTitle("1"), 40);
            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> setTitle("Go"), 60);
            server.getScheduler().scheduleSyncDelayedTask(plugin, Ready::clearTitle, 80);
            server.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    server.dispatchCommand(Bukkit.getConsoleSender(), "sg"), 60);
        }
    }

    private static void setTitle(String message) {
        for (Player player : inGame) {
            player.sendTitle(new Title(message));
        }
    }

    private static void clearTitle() {
        for (Player player : inGame) {
            player.hideTitle();
        }
    }
}
