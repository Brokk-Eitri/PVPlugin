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
            ready++;
            if(ready == inGame && inGame > 0) {
                getServer().dispatchCommand(Bukkit.getConsoleSender(), "sg");
            }
        }
        return false;
    }
}
