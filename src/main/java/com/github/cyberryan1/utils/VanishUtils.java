package com.github.cyberryan1.utils;

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
        String usePerm = ConfigUtils.getStr( "vanish.permission" );
        int playerLevel = getVanishLevel( player );
        String playerUUID = player.getUniqueId().toString();

        DataUtils.set( "vanish." + playerUUID + ".enabled", true );
        DataUtils.set( "vanish." + playerUUID + ".level", playerLevel );

        String vanishedMsg = ConfigUtils.getColoredStr( "vanish.level." + playerLevel + ".enable-msg", player );

        for ( Player p : Bukkit.getOnlinePlayers() ) {
            if ( VaultUtils.hasPerms( p, usePerm ) == false ) {
                p.hidePlayer( Utilities.getPlugin(), player );
            }
            else if ( playerLevel <= getMaxVanishLevel( p ) ) {
                p.sendMessage( vanishedMsg );
            }
            else {
                p.hidePlayer( Utilities.getPlugin(), player );
            }
        }

        // config: enable flight
        if ( ConfigUtils.getBool( "vanish.use.flight.enable" ) ) {
            // config: reset flight
            if ( ConfigUtils.getBool( "vanish.use.flight.reset" ) ) {
                DataUtils.set( "vanish." + playerUUID + ".previous.flight-state", player.getAllowFlight() );
            }

            player.setAllowFlight( true );

            // config: flight speed
            if ( ConfigUtils.getFloat( "vanish.use.flight.speed" ) > 1 ) {
                DataUtils.set( "vanish." + playerUUID + ".previous.flight-speed", player.getFlySpeed() );
                player.setFlySpeed( 0.1f * ConfigUtils.getFloat( "vanish.use.flight.speed" ) );
            }
        }

        // config: night vision
        if ( ConfigUtils.getBool( "vanish.use.night-vision" ) ) {
            if ( player.getPotionEffect( PotionEffectType.NIGHT_VISION ) != null ) {
                DataUtils.set( "vanish." + playerUUID + ".previous.night-vision", player.getPotionEffect( PotionEffectType.NIGHT_VISION ) );
            }

            PotionEffect nightvis = new PotionEffect( PotionEffectType.NIGHT_VISION, 200000, 2, true, false );
            player.addPotionEffect( nightvis );
        }

        // config: see self
        if ( ConfigUtils.getBool( "vanish.use.see-self" ) == false ) {
            if ( player.getPotionEffect( PotionEffectType.INVISIBILITY ) != null ) {
                DataUtils.set( "vanish." + playerUUID + ".previous.invisibility", player.getPotionEffect( PotionEffectType.INVISIBILITY ) );
            }

            PotionEffect invis = new PotionEffect( PotionEffectType.INVISIBILITY, 200000, 2, true, false );
            player.addPotionEffect( invis );

            // config: skull-helmet
            if ( ConfigUtils.getBool( "vanish.use.skull-helmet" ) ) {
                DataUtils.set( "vanish." + playerUUID + ".previous.helmet", player.getInventory().getHelmet() );

                ItemStack armor[] = new ItemStack[4];
                armor[0] = player.getInventory().getBoots();
                armor[1] = player.getInventory().getLeggings();
                armor[2] = player.getInventory().getChestplate();
                armor[3] = Utilities.getPlayerHead( player );

                player.getInventory().setArmorContents( armor );
            }
        }

        // config: walk speed
        if ( ConfigUtils.getInt( "vanish.use.walk-speed" ) > 1 ) {
            DataUtils.set( "vanish." + playerUUID + ".previous.walk-speed", player.getWalkSpeed() );
            player.setWalkSpeed( 0.1f * ConfigUtils.getInt( "vanish.use.walk-speed" ) );
        }

        // config: bossbar
        if ( ConfigUtils.getBool( "vanish.use.bossbar.enable" ) ) {
            BossbarUtils.addBossbar( player );
        }

        // config (other-events): hunger
        if ( ConfigUtils.getBool( "vanish.other-events.hunger.cancel" ) ) {
            DataUtils.set( "vanish." + playerUUID + ".previous.hunger", player.getFoodLevel() );
            player.setFoodLevel( 20 );
        }

        DataUtils.save();
    }


    public static void disableVanish( Player player ) {
        String usePerm = ConfigUtils.getStr( "vanish.permission" );
        int playerLevel = getVanishLevel( player );
        String playerUUID = player.getUniqueId().toString();
        String unvanishedMsg = ConfigUtils.getColoredStr( "vanish.level." + playerLevel + ".disable-msg", player );

        for ( Player p : Bukkit.getOnlinePlayers() ) {
            p.showPlayer( Utilities.getPlugin(), player );

            if ( playerLevel <= getMaxVanishLevel( p ) ) {
                p.sendMessage( unvanishedMsg );
            }
        }

        // config: enable flight
        if ( ConfigUtils.getBool( "vanish.use.flight.enable" ) ) {
            // config: reset flight
            if ( ConfigUtils.getBool( "vanish.use.flight.reset" ) ) {
                player.setAllowFlight( DataUtils.getBool( "vanish." + playerUUID + ".previous.flight-state" ) );
            }

            // config: flight-speed
            if ( ConfigUtils.getInt( "vanish.use.flight.speed" ) > 1 ) {
                player.setFlySpeed( DataUtils.getFloat( "vanish." + playerUUID + ".previous.flight-speed" ) );
            }
        }

        // config: night vision
        if ( ConfigUtils.getBool( "vanish.use.night-vision" ) ) {
            player.removePotionEffect( PotionEffectType.NIGHT_VISION );
            if ( DataUtils.get( "vanish." + playerUUID + ".previous.night-vision" ) != null ) {
                player.addPotionEffect( ( PotionEffect ) DataUtils.get( "vanish." + playerUUID + ".previous.night-vision" ) );
            }
        }

        // config: see self
        if ( ConfigUtils.getBool( "vanish.use.see-self") == false ) {
            player.removePotionEffect( PotionEffectType.INVISIBILITY );
            if ( DataUtils.get( "vanish." + playerUUID + ".previous.invisibility" ) != null ) {
                player.addPotionEffect( ( PotionEffect ) DataUtils.get( "vanish." + playerUUID + ".previous.invisibility" ) );
            }

            // config: skull helmet
            if ( ConfigUtils.getBool( "vanish.use.skull-helmet" ) ) {
                ItemStack armor[] = new ItemStack[4];
                armor[0] = player.getInventory().getBoots();
                armor[1] = player.getInventory().getLeggings();
                armor[2] = player.getInventory().getChestplate();
                armor[3] = ( ItemStack ) DataUtils.get( "vanish." + playerUUID + ".previous.helmet" );

                player.getInventory().setArmorContents( armor );
            }
        }

        // config: walk speed
        if ( ConfigUtils.getInt( "vanish.use.walk-speed" ) > 1 ) {
            player.setWalkSpeed( DataUtils.getFloat( "vanish." + playerUUID + ".previous.walk-speed" ) );
        }

        // config: bossbar
        if ( ConfigUtils.getBool( "vanish.use.bossbar.enable" ) ) {
            BossbarUtils.removeBossbar( player );
        }

        // config (other-events): hunger
        if ( ConfigUtils.getBool( "vanish.other-events.hunger.cancel" ) ) {
            player.setFoodLevel( DataUtils.getInt( "vanish." + playerUUID + ".previous.hunger" ) );
        }

        DataUtils.set( "vanish." + playerUUID, null );
        DataUtils.save();
    }



    public static boolean checkVanished( Player player ) {
        String uuid = player.getUniqueId().toString();
        if ( DataUtils.getBool( "vanish." + uuid + ".enabled" ) ) {
            return true;
        }
        return false;
    }



    public static int getVanishLevel( Player player ) {
        return getVanishLevel( ( ( OfflinePlayer ) player ) );
    }


    public static int getVanishLevel( OfflinePlayer player ) {
        String uuid = player.getUniqueId().toString();
        if ( DataUtils.getInt( "data." + uuid + ".level" ) == 0 ) {
            DataUtils.set( "data." + uuid + ".level", getMaxVanishLevel( player ) );
        }

        return DataUtils.getInt( "data." + uuid + ".level" );
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
        if ( VaultUtils.hasPerms( target, ConfigUtils.getStr( "vanish.level.5.permission" ) ) ) { return 5; }
        if ( VaultUtils.hasPerms( target, ConfigUtils.getStr( "vanish.level.4.permission" ) ) ) { return 4; }
        if ( VaultUtils.hasPerms( target, ConfigUtils.getStr( "vanish.level.3.permission" ) ) ) { return 3; }
        if ( VaultUtils.hasPerms( target, ConfigUtils.getStr( "vanish.level.2.permission" ) ) ) { return 2; }
        if ( VaultUtils.hasPerms( target, ConfigUtils.getStr( "vanish.permission" ) ) ) { return 1; }

        return 0;
    }



    // gets all the uuids that are currently vanished
    public static String[] getCurrentlyVanishedUUID() {
        ArrayList<String> uuid = new ArrayList<>();

        for ( String key : DataUtils.getDataManager().getConfig().getKeys( true ) ) {
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