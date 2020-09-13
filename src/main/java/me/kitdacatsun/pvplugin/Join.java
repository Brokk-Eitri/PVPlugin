package me.kitdacatsun.pvplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class Join implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Ensure correct number of args
        if (args.length < 1) {
            sender.sendMessage("Invalid arguments");
            return false;
        }

        // Ensure team is valid
        Team team = null;
        for (int i = 0; i < PVPlugin.teams.length; i++) {
            if (PVPlugin.teams[i].name.equalsIgnoreCase(args[0])) {
                team = PVPlugin.teams[i];
                break;
            }
        }

        if (team == null) {
            sender.sendMessage("That is not a valid team");
            return false;
        }

        // Get player
        Player player;
        if (args.length > 1) {
            player = getServer().getPlayer(args[1]);
        } else if (sender instanceof Player) {
            player = (Player)sender;
        } else {
            sender.sendMessage("No player found");
            return false;
        }

        // Reset player
        player.setHealth(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Add player to team
        String commandLine = "team join " + team.name + " " + player.getName();
        getServer().dispatchCommand(sender, commandLine);

        // Teleport player to start position
        int rnd = new Random().nextInt(team.spawnPoints.length);
        player.teleport(team.spawnPoints[rnd]);

        return true;
    }
}
