package me.kitdacatsun.pvplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

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

        spawnBarriers = new Location[] {

        };

        spawnBarrierBlock  = Material.IRON_BARS;

        this.getCommand("join").setExecutor(new Join());
        this.getCommand("start").setExecutor(new Start());
    }
}


