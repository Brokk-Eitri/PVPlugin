package me.kitdacatsun.pvplugin;

import org.bukkit.Location;

public class Team {
    public String name;
    public Location[] spawnPoints;

    public Team(String name, Location spawnPoint) {
        this.name = name;
        spawnPoints = new Location[]{
                spawnPoint
        };
    }

    public Team(String name, Location[] spawnPoints) {
        this.name = name;
        this.spawnPoints = spawnPoints;
    }
}
