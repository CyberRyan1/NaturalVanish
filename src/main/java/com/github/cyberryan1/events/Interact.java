package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.spigot.CyberCore;
import com.github.cyberryan1.cybercore.spigot.utils.CyberMsgUtils;
import com.github.cyberryan1.cybercore.spigot.utils.CyberVaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
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

import static org.bukkit.Material.*;

public class Interact implements Listener {

    private final ArrayList<Material> chestBlocks = new ArrayList<>( Arrays.asList( CHEST, TRAPPED_CHEST, ENDER_CHEST, SHULKER_BOX, WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX,
            MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX,
            CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX, BARREL ) );

    private final ArrayList<Material> redstoneBlocks = new ArrayList<>( Arrays.asList( OAK_BUTTON, BIRCH_BUTTON, JUNGLE_BUTTON, ACACIA_BUTTON, DARK_OAK_BUTTON, SPRUCE_BUTTON,
            STONE_BUTTON, LEVER, OAK_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, SPRUCE_DOOR, IRON_DOOR, OAK_FENCE_GATE, BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE,
            ACACIA_FENCE_GATE, DARK_OAK_FENCE_GATE, SPRUCE_FENCE_GATE ) );

    private final ArrayList<Material> pressureBlocks = new ArrayList<>( Arrays.asList( OAK_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, ACACIA_PRESSURE_PLATE,
            DARK_OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, STONE_PRESSURE_PLATE, HEAVY_WEIGHTED_PRESSURE_PLATE, LIGHT_WEIGHTED_PRESSURE_PLATE, TRIPWIRE, TRIPWIRE_HOOK ) );

    private ArrayList<String> physicalCooldown = new ArrayList<>();


    @EventHandler
    public void onInteract( PlayerInteractEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) == false ) { return; }

        if ( event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            Player player = event.getPlayer();

            if ( player.isSneaking() == false || ( player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInHand().getType().isBlock() == false
                    && player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType().isBlock() == false ) ) {
                // SilentChest opening
                if ( Settings.VANISH_EVENTS_CHEST_SILENT.bool() && chestBlocks.contains( event.getClickedBlock().getType() ) ) {

                    if ( event.getClickedBlock().getType() == Material.ENDER_CHEST ) {
                        event.setCancelled( true );
                        player.openInventory( player.getEnderChest() );
                    }

                    else {
                        GameMode prev = player.getGameMode();
                        player.setGameMode( GameMode.SPECTATOR );
                        Bukkit.getScheduler().runTaskLater( CyberCore.getPlugin(), () -> {
                            player.setGameMode( prev );
                        }, 2L );
                    }

                    String silentMsg = Settings.VANISH_EVENTS_CHEST_SILENT_MSG.coloredString();
                    if ( silentMsg.isBlank() == false ) {
                        CyberMsgUtils.sendMsg( player, silentMsg.replace( "[PLAYER]", player.getName() ) );
                    }
                }

                // Using a button, lever, door, or fencegate
                else if ( Settings.VANISH_EVENTS_INTERACT_CANCEL.bool() && redstoneBlocks.contains( event.getClickedBlock().getType() ) ) {

                    if ( Settings.VANISH_EVENTS_INTERACT_BYPASS.bool() == false
                            || CyberVaultUtils.hasPerms( player, Settings.VANISH_EVENTS_INTERACT_BYPASS_PERMISSION.string() ) == false ) {
                        event.setCancelled( true );

                        String cancelMsg = Settings.VANISH_EVENTS_INTERACT_CANCEL_MSG.coloredString();
                        if ( cancelMsg.isBlank() == false ) {
                            CyberMsgUtils.sendMsg( player, cancelMsg.replace( "[PLAYER]", player.getName() ) );
                        }
                    }
                }
            }
        }

        else if ( Settings.VANISH_EVENTS_INTERACT_CANCEL.bool() && event.getAction() == Action.PHYSICAL ) {
            if ( pressureBlocks.contains( event.getClickedBlock().getType() ) == false ) { return; }
            Player player = event.getPlayer();

            if ( Settings.VANISH_EVENTS_INTERACT_BYPASS.bool() == false
                    || CyberVaultUtils.hasPerms( player, Settings.VANISH_EVENTS_INTERACT_BYPASS_PERMISSION.string() ) == false ) {
                event.setCancelled( true );

                String cancelMsg = Settings.VANISH_EVENTS_INTERACT_CANCEL_MSG.coloredString();
                if ( cancelMsg.isBlank() == false  &&
                        physicalCooldown.contains( event.getPlayer().getName() ) == false ) {
                    CyberMsgUtils.sendMsg( player, cancelMsg.replace( "[PLAYER]", player.getName() ) );

                    physicalCooldown.add( player.getName() );
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( CyberCore.getPlugin(), new Runnable() {
                        public void run() { physicalCooldown.remove( player.getName() ); }
                    }, 60L );
                }
            }
        }
    }
}
