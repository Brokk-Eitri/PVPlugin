package me.kitdacatsun.pvplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class EndGame implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        endGame();

        return true;
    }

    public static void endGame() {
        for (int i = 0; i < PVPlugin.spawnBarriers.length; i++) {
            Location location = PVPlugin.spawnBarriers[i];
            location.getBlock().setType(PVPlugin.spawnBarrierBlock);
        }

        for (Player player : inGame) {
            resetPlayer(player);
        }

        for (Team team : teams) {
            team.players = new ArrayList<>();
            team.inPlay = false;
        }

        ready = new ArrayList<>();
        inGame = new ArrayList<>();

    }
}
