package me.kitdacatsun.pvplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public final class PVPlugin extends JavaPlugin {
    public static Server server;
    public static Plugin plugin;

    public static Location lobby;

    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Location> spawnBarriers = new ArrayList<>();
    public static Material barrierBlock;

    public static boolean gameInProgress = false;

    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Player> ready = new ArrayList<>();
    public static ArrayList<Team> defeated = new ArrayList<>();

    @Override
    public void onEnable() {
        server = getServer();
        plugin = this;

        try {
            ParseJSON(getDataFolder().getAbsolutePath() + "\\settings.json");
        } catch (IOException | ParseException e) {
            System.out.println("Error parsing settings JSON");
            System.out.println(e);
        }

        System.out.println(spawnBarriers.get(0));

        getCommand("join").setExecutor(new Join());
        getCommand("ready").setExecutor(new Ready());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    private void ParseJSON(String file) throws IOException, ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(file));

        JSONArray lobbyLocation = (JSONArray)json.get("lobby_location");
        lobby = new Location(Bukkit.getWorld("world"), (double)lobbyLocation.get(0),
                                                             (double)lobbyLocation.get(1),
                                                             (double)lobbyLocation.get(2));

        JSONArray teamsJSON = (JSONArray)json.get("teams");
        for (Object team : teamsJSON) {
            JSONArray teamJSON = (JSONArray)team;
            JSONArray positionJSON = (JSONArray)teamJSON.get(1);

            String name = (String)(teamJSON.get(0));
            Location spawnPosition = new Location(Bukkit.getWorld("world"), (double)positionJSON.get(0),
                                                                                  (double)positionJSON.get(1),
                                                                                  (double)positionJSON.get(2));
            teams.add(new Team(name, spawnPosition));
        }

        JSONArray spawnBarriersJSON = (JSONArray)json.get("spawn_barriers");
        for (Object positionJSON : spawnBarriersJSON) {
            JSONArray positionArray = (JSONArray)positionJSON;
            Location location = new Location(Bukkit.getWorld("world"), (double)positionArray.get(0),
                                                                             (double)positionArray.get(1),
                                                                             (double)positionArray.get(2));
            spawnBarriers.add(location);
        }

        barrierBlock = Material.getMaterial((String)json.get("barrier_block"));
    }

    public static Player ClosestPlayer(CommandSender sender) {
        Location senderLocation;
        if (sender instanceof Player) {
            senderLocation = ((Player) sender).getLocation();
        } else if (sender instanceof BlockCommandSender) {
            senderLocation = ((BlockCommandSender) sender).getBlock().getLocation().add(0.5, 0, 0.5);
        } else {
            return null;
        }

        Player closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Player player : server.getOnlinePlayers()) {
            double distance = senderLocation.distanceSquared(player.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                closest = player;
            }
        }

        return closest;
    }

    public static void giveKit(Player player) {
        player.setHealth(20);
        player.sendMessage("Pretend this gave you stuff");
    }

    public static void endGame() {
        Team winner = null;
        for (Team team : teams) {
            if (!defeated.contains(team)) {
                winner = team;
                break;
            }
        }

        if (winner == null) {
            for (Player player : players) {
                player.sendMessage("Could not find winner");
            }
            return;
        }

        String subtitle;
        for (Player player : players) {
            player.teleport(lobby);
            if (winner.members.contains(player)) {
                subtitle = "GG EZ WP";
            } else {
                subtitle = "Better luck next time";
            }
            player.sendTitle("Team " + winner.name + " has won!", subtitle, 0, 15, 5);
        }

        ready.clear();
        players.clear();
        defeated.clear();

        gameInProgress = false;
    }

    public static void resetGame() {
        for (Location location : spawnBarriers) {
            Bukkit.getWorld("world").getBlockAt(location).setType(barrierBlock);
        }

        for (Team t : teams) {
            for (Player p : t.members) {
                p.teleport(t.spawnPosition);
                giveKit(p);
            }
        }
    }

    public static void startGame() {
        for (Location location : spawnBarriers) {
            Bukkit.getWorld("world").getBlockAt(location).setType(Material.AIR);
        }

        gameInProgress = true;
    }
}
