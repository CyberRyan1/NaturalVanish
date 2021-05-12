package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.Utilities;
import com.github.cyberryan1.utils.VanishUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class Interact implements Listener {

    private final ArrayList<Material> chestBlocks = new ArrayList<>( Arrays.asList( Material.CHEST, Material.TRAPPED_CHEST, Material.ENDER_CHEST, Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BARREL ) );


    @EventHandler
    public void onInteract( PlayerInteractEvent event ) {
        if ( event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            if ( chestBlocks.contains( event.getClickedBlock().getType() ) ) {

                Player player = event.getPlayer();
                if ( player.isSneaking() == false || ( player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInHand().getType().isBlock() == false
                        && player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType().isBlock() == false ) ) {
                    if ( VanishUtils.checkVanished( player ) && ConfigUtils.getBool( "vanish.other-events.chest-open.silent" ) ) {

                        if ( event.getClickedBlock().getType() == Material.ENDER_CHEST ) {
                            event.setCancelled( true );
                            player.openInventory( player.getEnderChest() );
                        }

                        else {
                            GameMode prev = player.getGameMode();
                            player.setGameMode( GameMode.SPECTATOR );
                            Bukkit.getScheduler().runTaskLater( Utilities.getPlugin(), () -> {
                                player.setGameMode( prev );
                            }, 2L );
                        }

                        if ( ConfigUtils.getStr( "vanish.other-events.chest-open.silent-msg").equals( "" ) == false ) {
                            player.sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.chest-open.silent-msg", player ) );
                        }
                    }
                }
            }
        }
    }
}
