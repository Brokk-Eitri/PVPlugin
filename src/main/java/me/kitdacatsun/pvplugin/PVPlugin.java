package me.kitdacatsun.pvplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

import static org.bukkit.Bukkit.dispatchCommand;

public final class PVPlugin extends JavaPlugin {

    public static Location lobby;

    public static Team[] teams;
    public static Location[] spawnBarriers;
    public static Material spawnBarrierBlock;

    public static ArrayList<Player> ready;
    public static ArrayList<Player> inGame;

    @Override
    public void onEnable() {
        System.out.println("Started Up");

        lobby = new Location(Bukkit.getWorld("world_nether"), 0.5, 125, 0.5, 0, 0);

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

        inGame = new ArrayList<>();
        ready = new ArrayList<>();

        this.getCommand("join").setExecutor(new Join());
        this.getCommand("startgame").setExecutor(new StartGame());
        this.getCommand("endgame").setExecutor(new EndGame());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("ready").setExecutor(new Ready());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    public static void resetPlayer(Player player) {
        player.teleport(lobby);
        player.setGameMode(GameMode.ADVENTURE);

        player.getInventory().clear();
        player.updateInventory();

        player.setHealth(20);
        player.setExp(0);
        player.setLevel(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        dispatchCommand(player, "team join NoTeam");
    }
}


