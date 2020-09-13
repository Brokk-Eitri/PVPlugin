package me.kitdacatsun.pvplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public final class PVPlugin extends JavaPlugin {

    public static Team[] teams;
    public static Location[] spawnBarriers;
    public static Material spawnBarrierBlock;

    @Override
    public void onEnable() {
        System.out.println("Started Up");

        World nether = Bukkit.getWorld("world_nether");

        teams = new Team[] {
                new Team("Red", new Location(nether, -12.5f, 130, 0.5f,-90,0)),
                new Team("Blue", new Location(nether, 12.5f, 130, 0.5f,90,0)),
                new Team("None", new Location[]{
                        new Location(nether, -40, 130, 4,-90,0),
                        new Location(nether, -25, 130, -27,0,0),
                        new Location(nether, 40, 132, -19,90,0),
                        new Location(nether, 34, 130, 23,0,0),
                })
        };

        spawnBarriers = new Location[]{
                new Location(nether, 10, 129, 0,0,0),
                new Location(nether, 10, 130, 0,0,0),
                new Location(nether, -10, 129, 0,0,0),
                new Location(nether, -10, 130, 0,0,0),
        };

        spawnBarrierBlock = Material.IRON_BARS;

        this.getCommand("join").setExecutor(new Join());
        this.getCommand("startgame").setExecutor(new StartGame());
        this.getCommand("endgame").setExecutor(new EndGame());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }


}


