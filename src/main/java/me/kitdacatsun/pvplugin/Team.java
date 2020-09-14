package me.kitdacatsun.pvplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    public String name;
    public Location[] spawnPoints;
    public ArrayList<Player> players;
    public boolean spawnReady = false;
    public boolean inPlay = false;

    public Team(String name, Location spawnPoint) {
        this.name = name;
        spawnPoints = new Location[]{
                spawnPoint
        };
        
        players = new ArrayList<>();
    }

    public Team(String name, Location[] spawnPoints) {
        this.name = name;
        this.spawnPoints = spawnPoints;
        players = new ArrayList<>();

    }
}
