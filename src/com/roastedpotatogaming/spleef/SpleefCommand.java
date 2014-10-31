package com.roastedpotatogaming.spleef;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@SuppressWarnings("SpellCheckingInspection")
public class SpleefCommand implements CommandExecutor {
    private final Spleef spleef;
    private static final String SPLEEF_HELP_ADD = "Usage: /spleef add <region_id>";
    private static final String SPLEEF_HELP_REMOVE = "Usage: /spleef remove <region_id>";
    private static final String SPLEEF_HELP_JOIN = "Usage: /spleef join <region_id/arena_name>";

    public SpleefCommand(Spleef spleef) {
        this.spleef = spleef;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (!(commandSender instanceof Player)) {commandSender.sendMessage("Only a player can do that!");return true;}
        if (args.length == 0) {return false;} // Azda wants this here (by 'this' I mean the comment)
        Player player = (Player) commandSender;
        if (args[0].equalsIgnoreCase("add")) { // Adds Arena
            if (args.length != 2) {player.sendMessage(SPLEEF_HELP_ADD);return true;}
            if (WGBukkit.getRegionManager(player.getWorld()).hasRegion(args[1])) {
                if (spleef.arenas.contains(args[1])) {player.sendMessage("The arena \'" + args[1] + "\' already exists!");return true;}
                player.getLocation().getBlock().getRelative(0, -1, 0).setType(Material.EMERALD_BLOCK);

                String loc = player.getLocation().getBlockX() +
                        " " + player.getLocation().getBlockY() +
                        " " + player.getLocation().getBlockZ();

                spleef.saveLocation(player.getLocation(), args[1]);

                player.sendMessage("The arena \'" + args[1] + "\' has been made!");
                player.sendMessage("The lobby has been set to " + loc);
                return true;
            } else {
                player.sendMessage("Please make sure that the region \'" + args[1] + "\' exists!");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("remove")) { // Removes Arena
            if (args.length != 2) {player.sendMessage(SPLEEF_HELP_REMOVE);return true;}
            if (spleef.arenas.contains(args[1])) {
                spleef.getLocation(args[1]).getBlock().getRelative(0, -1, 0).setType(Material.AIR);
                spleef.arenas.set(args[1], null);
                spleef.saveConfig();
                player.sendMessage("Arena removed!");
                return true;
            } else {
                player.sendMessage("\'" + args[1] + "\' isn't an arena!");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("list")) { // Lists all Arenas
            String arenas = "";
            if (spleef.arenas.getValues(true).isEmpty()) {player.sendMessage("There are no arenas currently!");return true;}
            for (String arena : spleef.arenas.getValues(false).keySet()) {
                arenas += ", " + arena;
            }
            player.sendMessage("Arenas: " + arenas.substring(2));
            return true;
        } else if (args[0].equalsIgnoreCase("join")) { // Joins Arena
            if (args.length != 2) {player.sendMessage(SPLEEF_HELP_JOIN);return true;}
            if (WGBukkit.getRegionManager(player.getWorld()).hasRegion(args[1])) {
                if (player.hasMetadata("Spleef-Joined")) {player.sendMessage("You have already joined!");return true;}
                player.setMetadata("Spleef-LastPlace", new FixedMetadataValue(spleef, player.getLocation()));
                player.setMetadata("Spleef-Joined", new FixedMetadataValue(spleef, args[1]));
                player.teleport(spleef.getLocation(args[1]));
                return true;
            } else {
                player.sendMessage("Please make sure that the arena \'" + args[1] + "\' exists!");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (!player.hasMetadata("Spleef-Joined")) {player.sendMessage("You need to join an arena before you can leave one! :)");return true;}
            player.teleport((Location) player.getMetadata("Spleef-LastPlace").get(0).value());
            player.removeMetadata("Spleef-Joined", spleef);
            player.removeMetadata("Spleef-LastPlace", spleef);
            player.removeMetadata("Spleef-Ready", spleef);
            player.sendMessage("You have left the arena!");
            return true;
        }
        return false;
    }
}
