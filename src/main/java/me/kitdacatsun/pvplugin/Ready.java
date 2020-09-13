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
            if (!ready.contains((Player)sender)) {
                ready.add((Player)sender);
            }
            if (ready.size() == inGame.size() && inGame.size() > 0) {
                getServer().dispatchCommand(Bukkit.getConsoleSender(), "sg");
            }
        }

        sender.sendMessage("Ready: " + ready.size() + "/" + inGame.size());

        return false;
    }
}
