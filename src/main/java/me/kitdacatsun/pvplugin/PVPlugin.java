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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        System.out.println("Started Up");

        World nether = Bukkit.getWorld("nether");

        teams = new Team[] {
                new Team("Red", new Location(nether, -12.5f, 130, 0.5f)),
                new Team("Blue", new Location(nether, 12.5f, 130, 0.5f)),
                new Team("None", new Location(nether, 0.5f, 130, 0.5f))
        };

        spawnBarriers = new Location[] {

        };

        spawnBarrierBlock  = Material.IRON_BARS;

        this.getCommand("join").setExecutor(new Join());
        this.getCommand("start").setExecutor(new Start());
    }
}


