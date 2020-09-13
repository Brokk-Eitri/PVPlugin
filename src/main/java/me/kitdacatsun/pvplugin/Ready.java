package me.kitdacatsun.pvplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.kitdacatsun.pvplugin.PVPlugin.*;
import static org.bukkit.Bukkit.getServer;

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

        return false;
    }

    private void ready(Player player) {
        if (!ready.contains(player) && inGame.contains(player)) {
            ready.add(player);
        }

        if (ready.size() == inGame.size() && inGame.size() >= 2) {
            getServer().dispatchCommand(Bukkit.getConsoleSender(), "sg");
        }
    }
}
