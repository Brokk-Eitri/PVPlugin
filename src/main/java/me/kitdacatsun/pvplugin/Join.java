package me.kitdacatsun.pvplugin;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static me.kitdacatsun.pvplugin.PVPlugin.*;
import static org.bukkit.Bukkit.*;

public class Join implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Ensure correct number of args
        if (args.length < 2) {
            sender.sendMessage("Invalid arguments");
            return false;
        }

        // Ensure team is valid
        Team team = null;
        for (Team value : teams) {
            if (value.name.equalsIgnoreCase(args[0])) {
                team = value;
                break;
            }
        }

        if (team == null) {
            sender.sendMessage("That is not a valid team");
            return false;
        }

        // Get player
        Player player = (Player)CommandUtils.getTargets(sender, args[1])[0];

        if (player == null) {
            sender.sendMessage("Player not found");
            return false;
        }

        // Reset player
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setExp(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Add player to team
        String commandLine = "team join " + team.name + " " + player.getName();
        team.inPlay = true;
        getServer().dispatchCommand(getConsoleSender(), commandLine);
        for (Player p : inGame) {
            if (p != player) {
                p.sendMessage(player.getName() + " has joined " + team.name + " team");
            }
        }

        // Teleport player to start position
        int rnd = new Random().nextInt(team.spawnPoints.length);
        player.teleport(team.spawnPoints[rnd]);

        equip(player);
        inGame.add(player);
        team.players.add(player);

        if (team.spawnReady) {
            getServer().dispatchCommand(player, "ready");
        }

        return true;
    }
}
