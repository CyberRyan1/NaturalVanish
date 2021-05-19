package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.Utilities;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import static org.bukkit.Material.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;

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
        if ( VanishUtils.checkVanished( event.getPlayer() ) ) {
            if ( event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
                Player player = event.getPlayer();

                if ( player.isSneaking() == false || ( player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInHand().getType().isBlock() == false
                        && player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType().isBlock() == false ) ) {
                    // SilentChest opening
                    if ( chestBlocks.contains( event.getClickedBlock().getType() ) ) {

                        if ( ConfigUtils.getBool( "vanish.other-events.chest-open.silent" ) ) {

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

                    // Using a button, lever, door, or fencegate
                    else if ( redstoneBlocks.contains( event.getClickedBlock().getType() ) ) {

                        if ( ConfigUtils.getBool( "vanish.other-events.interact.cancel" ) ) {
                            if ( ConfigUtils.getBool( "vanish.other-events.interact.bypass" ) == false
                                    || VaultUtils.hasPerms( player, ConfigUtils.getStr( "vanish.other-events.interact.bypass-perm" ) ) == false ) {
                                event.setCancelled( true );

                                if ( ConfigUtils.getStr( "vanish.other-events.interact.cancel-msg" ).equals( "" ) == false ) {
                                    player.sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.interact.cancel-msg", player ) );
                                }
                            }
                        }
                    }
                }
            }

            else if ( event.getAction() == Action.PHYSICAL ) {
                Player player = event.getPlayer();

                if ( pressureBlocks.contains( event.getClickedBlock().getType() ) ) {
                    if ( ConfigUtils.getBool( "vanish.other-events.interact.cancel" ) ) {
                        if ( ConfigUtils.getBool( "vanish.other-events.interact.bypass" ) == false ||
                                VaultUtils.hasPerms( player, ConfigUtils.getStr( "vanish.other-events.interact.bypass-perm" ) ) == false ) {
                            event.setCancelled( true );

                            if ( ConfigUtils.getStr( "vanish.other-events.interact.cancel-msg" ).equals( "" ) == false &&
                                    physicalCooldown.contains( event.getPlayer().getName() ) == false ) {
                                player.sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.interact.cancel-msg", player ) );

                                physicalCooldown.add( player.getName() );
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( Utilities.getPlugin(), new Runnable() {
                                    public void run() { physicalCooldown.remove( player.getName() ); }
                                }, 60L);
                            }
                        }
                    }
                }
            }
        }
    }
}
