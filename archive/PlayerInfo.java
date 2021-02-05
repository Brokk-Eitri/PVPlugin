package me.kitdacatsun.pvplugin;

import org.bukkit.entity.Player;

public class PlayerInfo {
    Team team;
    Player player;

    public PlayerInfo(Team team, Player player) {
        this.team = team;
        this.player = player;
    }
}
