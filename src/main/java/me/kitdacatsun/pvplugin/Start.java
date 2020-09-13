package me.kitdacatsun.pvplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Start implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (int i = 0; i < PVPlugin.spawnBarriers.length; i++) {
            Location location = PVPlugin.spawnBarriers[i];

        }

        return false;
    }
}
