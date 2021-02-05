package me.kitdacatsun.pvplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    public String name;
    public Location spawnPosition;
    public ArrayList<Player> members;

    public Team(String name, Location spawnPosition) {
        this.name = name;
        this.spawnPosition = spawnPosition;
        members = new ArrayList<>();
    }
}
