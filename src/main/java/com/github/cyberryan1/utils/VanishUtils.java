package com.github.cyberryan1.utils;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreItemUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class VanishUtils {



    // Toggles the vanish for the argument
    // true = vanish enabled, false = vanish disabled
    public static boolean toggleVanish( Player player ) {
        // going into vanish
        if ( checkVanished( player ) == false ) {
            enableVanish( player );
            return true;
        }

        else {
            disableVanish( player );
            return false;
        }
    }


    public static void enableVanish( Player player ) {
        String usePerm = Settings.VANISH_PERMISSION.string();
        int playerLevel = getVanishLevel( player );
        String playerUUID = player.getUniqueId().toString();

        YMLUtils.getData().set( "vanish." + playerUUID + ".enabled", true );
        YMLUtils.getData().set( "vanish." + playerUUID + ".level", playerLevel );

        String vanishedMsg = switch ( playerLevel ) {
            case 2 -> Settings.VANISH_LEVEL_TWO_ENABLE_MSG.coloredString();
            case 3 -> Settings.VANISH_LEVEL_THREE_ENABLE_MSG.coloredString();
            case 4 -> Settings.VANISH_LEVEL_FOUR_ENABLE_MSG.coloredString();
            case 5 -> Settings.VANISH_LEVEL_FIVE_ENABLE_MSG.coloredString();
            default -> Settings.VANISH_LEVEL_ONE_ENABLE_MSG.coloredString();
        };
        vanishedMsg = vanishedMsg.replace( "[PLAYER]", player.getName() );

        for ( Player p : Bukkit.getOnlinePlayers() ) {
            if ( VaultUtils.hasPerms( p, usePerm ) == false ) {
                p.hidePlayer( CyberCore.getPlugin(), player );
            }
            else if ( playerLevel <= getMaxVanishLevel( p ) ) {
                p.sendMessage( vanishedMsg );
            }
            else {
                p.hidePlayer( CyberCore.getPlugin(), player );
            }
        }

        // config: enable flight
        if ( Settings.VANISH_FLIGHT_ENABLE.bool() ) {
            // config: reset flight
            if ( Settings.VANISH_RESET_FLIGHT.bool() ) {
                YMLUtils.getData().set( "vanish." + playerUUID + ".previous.flight-state", player.getAllowFlight() );
            }

            player.setAllowFlight( true );

            // config: flight speed
            if ( Settings.VANISH_FLIGHT_SPEED.getFloat() > 1 ) {
                YMLUtils.getData().set( "vanish." + playerUUID + ".previous.flight-speed", player.getFlySpeed() );
                player.setFlySpeed( 0.1f * Settings.VANISH_FLIGHT_SPEED.getFloat() );
            }
        }

        // config: night vision
        if ( Settings.VANISH_NIGHT_VISION.bool() ) {
            if ( player.getPotionEffect( PotionEffectType.NIGHT_VISION ) != null ) {
                YMLUtils.getData().set( "vanish." + playerUUID + ".previous.night-vision", player.getPotionEffect( PotionEffectType.NIGHT_VISION ) );
            }

            PotionEffect nightvis = new PotionEffect( PotionEffectType.NIGHT_VISION, 200000, 2, true, false );
            player.addPotionEffect( nightvis );
        }

        // config: see self
        if ( Settings.VANISH_SEE_SELF.bool() == false ) {
            if ( player.getPotionEffect( PotionEffectType.INVISIBILITY ) != null ) {
                YMLUtils.getData().set( "vanish." + playerUUID + ".previous.invisibility", player.getPotionEffect( PotionEffectType.INVISIBILITY ) );
            }

            PotionEffect invis = new PotionEffect( PotionEffectType.INVISIBILITY, 200000, 2, true, false );
            player.addPotionEffect( invis );

            // config: skull-helmet
            if ( Settings.VANISH_SKULL_HELMET.bool() ) {
                YMLUtils.getData().set( "vanish." + playerUUID + ".previous.helmet", player.getInventory().getHelmet() );

                ItemStack armor[] = new ItemStack[4];
                armor[0] = player.getInventory().getBoots();
                armor[1] = player.getInventory().getLeggings();
                armor[2] = player.getInventory().getChestplate();
                armor[3] = CoreItemUtils.getPlayerSkull( player );

                player.getInventory().setArmorContents( armor );
            }
        }

        // config: walk speed
        if ( Settings.VANISH_WALK_SPEED.getFloat() > 1 ) {
            YMLUtils.getData().set( "vanish." + playerUUID + ".previous.walk-speed", player.getWalkSpeed() );
            player.setWalkSpeed( 0.1f * Settings.VANISH_WALK_SPEED.getFloat() );
        }

        // config: bossbar
        if ( Settings.VANISH_BOSSBAR_ENABLE.bool() ) {
            BossbarUtils.addBossbar( player );
        }

        // config (other-events): hunger
        if ( Settings.VANISH_EVENTS_HUNGER_CANCEL.bool() ) {
            YMLUtils.getData().set( "vanish." + playerUUID + ".previous.hunger", player.getFoodLevel() );
            player.setFoodLevel( 20 );
        }

        YMLUtils.getData().save();
    }


    public static void disableVanish( Player player ) {
        String usePerm = Settings.VANISH_PERMISSION.string();
        int playerLevel = getVanishLevel( player );
        String playerUUID = player.getUniqueId().toString();

        String unvanishedMsg = switch ( playerLevel ) {
            case 2 -> Settings.VANISH_LEVEL_TWO_DISABLE_MSG.coloredString();
            case 3 -> Settings.VANISH_LEVEL_THREE_DISABLE_MSG.coloredString();
            case 4 -> Settings.VANISH_LEVEL_FOUR_DISABLE_MSG.coloredString();
            case 5 -> Settings.VANISH_LEVEL_FIVE_DISABLE_MSG.coloredString();
            default -> Settings.VANISH_LEVEL_ONE_DISABLE_MSG.coloredString();
        };
        unvanishedMsg = unvanishedMsg.replace( "[PLAYER]", player.getName() );

        for ( Player p : Bukkit.getOnlinePlayers() ) {
            p.showPlayer( CyberCore.getPlugin(), player );

            if ( playerLevel <= getMaxVanishLevel( p ) ) {
                p.sendMessage( unvanishedMsg );
            }
        }

        // config: enable flight
        if ( Settings.VANISH_FLIGHT_ENABLE.bool() ) {
            // config: reset flight
            if ( Settings.VANISH_RESET_FLIGHT.bool() ) {
                player.setAllowFlight( YMLUtils.getData().getBool( "vanish." + playerUUID + ".previous.flight-state" ) );
            }

            // config: flight-speed
            if ( Settings.VANISH_FLIGHT_SPEED.getFloat() > 1 ) {
                player.setFlySpeed( YMLUtils.getData().getFloat( "vanish." + playerUUID + ".previous.flight-speed" ) );
            }
        }

        // config: night vision
        if ( Settings.VANISH_NIGHT_VISION.bool() ) {
            player.removePotionEffect( PotionEffectType.NIGHT_VISION );
            if ( YMLUtils.getData().get( "vanish." + playerUUID + ".previous.night-vision" ) != null ) {
                player.addPotionEffect( ( PotionEffect ) YMLUtils.getData().get( "vanish." + playerUUID + ".previous.night-vision" ) );
            }
        }

        // config: see self
        if ( Settings.VANISH_SEE_SELF.bool() == false ) {
            player.removePotionEffect( PotionEffectType.INVISIBILITY );
            if ( YMLUtils.getData().get( "vanish." + playerUUID + ".previous.invisibility" ) != null ) {
                player.addPotionEffect( ( PotionEffect ) YMLUtils.getData().get( "vanish." + playerUUID + ".previous.invisibility" ) );
            }

            // config: skull helmet
            if ( Settings.VANISH_SKULL_HELMET.bool() ) {
                ItemStack armor[] = new ItemStack[4];
                armor[0] = player.getInventory().getBoots();
                armor[1] = player.getInventory().getLeggings();
                armor[2] = player.getInventory().getChestplate();
                armor[3] = ( ItemStack ) YMLUtils.getData().get( "vanish." + playerUUID + ".previous.helmet" );

                player.getInventory().setArmorContents( armor );
            }
        }

        // config: walk speed
        if ( Settings.VANISH_WALK_SPEED.getFloat() > 1 ) {
            player.setWalkSpeed( YMLUtils.getData().getFloat( "vanish." + playerUUID + ".previous.walk-speed" ) );
        }

        // config: bossbar
        if ( Settings.VANISH_BOSSBAR_ENABLE.bool() ) {
            BossbarUtils.removeBossbar( player );
        }

        // config (other-events): hunger
        if ( Settings.VANISH_EVENTS_HUNGER_CANCEL.bool() ) {
            player.setFoodLevel( YMLUtils.getData().getInt( "vanish." + playerUUID + ".previous.hunger" ) );
        }

        YMLUtils.getData().set( "vanish." + playerUUID, null );
        YMLUtils.getData().save();
    }



    public static boolean checkVanished( Player player ) {
        String uuid = player.getUniqueId().toString();
        if ( YMLUtils.getData().getBool( "vanish." + uuid + ".enabled" ) ) {
            return true;
        }
        return false;
    }



    public static int getVanishLevel( Player player ) {
        return getVanishLevel( ( ( OfflinePlayer ) player ) );
    }


    public static int getVanishLevel( OfflinePlayer player ) {
        String uuid = player.getUniqueId().toString();
        if ( YMLUtils.getData().getInt( "data." + uuid + ".level" ) == 0 ) {
            YMLUtils.getData().set( "data." + uuid + ".level", getMaxVanishLevel( player ) );
        }

        return YMLUtils.getData().getInt( "data." + uuid + ".level" );
    }



    public static int getMaxVanishLevel( CommandSender sender ) {
        if ( sender instanceof Player ) {
            OfflinePlayer player = ( OfflinePlayer ) sender;
            return getMaxVanishLevel( player );
        }

        return 5;
    }


    public static int getMaxVanishLevel( Player target ) {
        return getMaxVanishLevel( ( ( OfflinePlayer ) target ) );
    }


    public static int getMaxVanishLevel( OfflinePlayer target ) {
        if ( VaultUtils.hasPerms( target, Settings.VANISH_LEVEL_FIVE_PERMISSION.string() ) ) { return 5; }
        if ( VaultUtils.hasPerms( target, Settings.VANISH_LEVEL_FOUR_PERMISSION.string() ) ) { return 4; }
        if ( VaultUtils.hasPerms( target, Settings.VANISH_LEVEL_THREE_PERMISSION.string() ) ) { return 3; }
        if ( VaultUtils.hasPerms( target, Settings.VANISH_LEVEL_TWO_PERMISSION.string() ) ) { return 2; }
        if ( VaultUtils.hasPerms( target, Settings.VANISH_PERMISSION.string() ) ) { return 1; }

        return 0;
    }



    // gets all the uuids that are currently vanished
    public static String[] getCurrentlyVanishedUUID() {
        ArrayList<String> uuid = new ArrayList<>();

        for ( String key : YMLUtils.getData().getConfig().getKeys( true ) ) {
            if ( key.contains( "vanish." ) ) {
                String u = key.substring( 7 );
                if ( u.indexOf( '.' ) != -1 ) {
                    u = u.substring( 0, u.indexOf( '.' ) );
                    if ( uuid.contains( u ) == false ) {
                        uuid.add( u );
                    }
                }
            }
        }

        if ( uuid.size() == 0 ) {
            return new String[0];
        }
        return uuid.toArray( new String[ uuid.size() - 1 ] );
    }
}