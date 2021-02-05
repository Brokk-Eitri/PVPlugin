package me.kitdacatsun.pvplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.kitdacatsun.pvplugin.PVPlugin.*;

public class Join implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }

        Team team = null;
        for (Team t: teams) {
            if (t.name.equalsIgnoreCase(args[0])) {
                team = t;
                break;
            }
        }

        if (team == null) {
            sender.sendMessage("Invalid team name");
            return false;
        }

        Player player = ClosestPlayer(sender);

        if (player == null) {
            sender.sendMessage("Could not find closest player");
            return false;
        }

        if (gameInProgress) {
            player.sendMessage("Game is already in progress");
            return true;
        }

        if (team.members.contains(player)) {
            player.sendMessage("You are already in that team");
            return true;
        }

        for (Team t : teams) {
            t.members.remove(player);
        }

        resetGame();

        player.teleport(team.spawnPosition);
        team.members.add(player);
        if (!players.contains(player)) {
            players.add(player);
        }
        giveKit(player);

        for (Player p : players) {
            p.sendMessage(player.getName() + " has joined " + team.name + " team");
        }

        return true;
    }
}
