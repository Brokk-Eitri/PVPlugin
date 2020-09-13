package me.kitdacatsun.pvplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class PVPlugin extends JavaPlugin {

    public static Team[] teams;

    @Override
    public void onEnable() {
        System.out.println("Started Up");

        teams = new Team[] {
                new Team("Red", -12.5f, 130, 0.5f),
                new Team("Blue", 12.5f, 130, 0.5f),
                new Team("None", 0.5f, 130, 0.5f)
        };

        this.getCommand("join").setExecutor(new Join());
    }
}


