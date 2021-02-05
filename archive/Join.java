package me.kitdacatsun.pvplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.kitdacatsun.pvplugin.PVPlugin.*;
import static org.bukkit.Bukkit.*;

public class Join implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

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

        Player player = (Player)CommandUtils.getTargets(sender, args[1])[0];

        if (player == null) {
            sender.sendMessage("Player not found");
            return false;
        }

        if (inGame.contains(player)) {
            player.sendMessage("You are already in a team");
            return true;
        }

        resetPlayer(player);
        addToTeam(player, team);
        equip(player);
        inGame.add(player);

        if (team.spawnReady) {
            getServer().dispatchCommand(player, "ready");
        }

        for (Player p : inGame) {
            p.setLevel(inGame.size());
        }

        return true;
    }

}
