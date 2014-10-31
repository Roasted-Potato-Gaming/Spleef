package com.roastedpotatogaming.spleef;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SpleefListener implements Listener {

    private final Spleef spleef;

    public SpleefListener(Spleef spleef) {
        this.spleef = spleef;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {return;}
        if (!event.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {return;}
        for (String arena : spleef.arenas.getValues(true).keySet()) {
            if (arena != null) {
                if (spleef.getLocation(arena)
                        .getBlock()
                        .getRelative(0, -1, 0)
                        .equals(event.getClickedBlock())) {
                    event.getPlayer().setMetadata("Spleef-Ready", new FixedMetadataValue(spleef, arena));
                    event.getPlayer().sendMessage("You are now ready!");
                }
            }
        }
    }
}
