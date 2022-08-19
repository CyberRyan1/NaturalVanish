package com.github.cyberryan1;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.utils.Utils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
        if ( VaultUtils.hasPerms( sender, Settings.VANISH_PERMISSION.string() ) == false ) {
            sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            return true;
        }



        // toggle the players vanish
        else if ( args.length == 0 ) {
            if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                // making sure the player's permission for the highest level hasn't changed, and changing it to the highest
                    // available level if it has
                if ( VanishUtils.getVanishLevel( player ) > VanishUtils.getMaxVanishLevel( player ) ) {
                    YMLUtils.getData().set( "data." + player.getUniqueId().toString() + ".level", VanishUtils.getMaxVanishLevel( player ) );
                    YMLUtils.getData().save();
                }

                VanishUtils.toggleVanish( player );
            }

            // someone besides a player tried to execute the command, which cannot happen
            else {
                sender.sendMessage( Settings.VANISH_CANT_USE_MSG.coloredString() );
            }

            return true;
        }



        // help sub-command
        else if ( args[0].equalsIgnoreCase( "help" ) ) {
            String helpMsg[] = { "\n", CoreUtils.getColored( "&8/&svanish" ), CoreUtils.getColored( "&8/&svanish &phelp" ), CoreUtils.getColored( "&8/&svanish &ptoggle (player)"),
                    CoreUtils.getColored("&8/&svanish &psetlevel (level)"), CoreUtils.getColored("&8/&svanish &psetlevel (player) (level)"),
                    CoreUtils.getColored( "&8/&svanish &pchecklevel [player]"), CoreUtils.getColored("&8/&svanish &pcheck [player]"),
                    CoreUtils.getColored( "&8/&svanish &plist"), CoreUtils.getColored("&8/&svanish &preload"), "\n" };
            for ( String s : helpMsg ) {
                sender.sendMessage( s );
            }

            return true;
        }



        // toggle (player) sub-command
        else if ( args[0].equalsIgnoreCase( "toggle" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_TOGGLE_PERMISSION.string() ) ) {
                if ( args.length >= 2 ) {
                    Player target = Bukkit.getServer().getPlayer( args[1] );
                    if ( target != null ) {
                        if ( VanishUtils.getMaxVanishLevel( sender ) >= VanishUtils.getMaxVanishLevel( target ) ) {
                            if ( VanishUtils.toggleVanish( target ) ) {
                                sender.sendMessage( Settings.VANISH_TOGGLE_SENDER_ENABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[TARGET]", target.getName() ) );
                                target.sendMessage( Settings.VANISH_TOGGLE_TARGET_ENABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[TARGET]", target.getName() ) );
                            }

                            else {
                                sender.sendMessage( Settings.VANISH_TOGGLE_SENDER_DISABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[TARGET]", target.getName() ) );
                                target.sendMessage( Settings.VANISH_TOGGLE_TARGET_DISABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[TARGET]", target.getName() ) );
                            }
                        }

                        else {
                            sender.sendMessage( Settings.VANISH_TOGGLE_LEVEL_LOW_MSG.coloredString()
                                    .replace( "[PLAYER]", sender.getName() )
                                    .replace( "[ARG]", target.getName() ) );
                        }
                    }

                    // target is offline
                    else {
                        OfflinePlayer offline = Bukkit.getServer().getOfflinePlayer( args[1] );
                        // target has played before
                        if ( offline.hasPlayedBefore() ) {
                            String uuid = offline.getUniqueId().toString();

                            if ( YMLUtils.getData().getBool( "vanish." + uuid + ".enabled" ) ) {
                                if ( YMLUtils.getData().getBool( "data.offline.enabled." + uuid ) ) { YMLUtils.getData().set( "data.offline.enabled." + uuid, null ); }
                                YMLUtils.getData().set( "data.offline.disabled." + uuid, true );
                                YMLUtils.getData().set( "vanish." + uuid + ".enabled", null );
                                YMLUtils.getData().save();


                                sender.sendMessage( Settings.VANISH_TOGGLE_SENDER_OFFLINE_DISABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[ARG]", offline.getName() ) );
                            }


                            // Target's vanish was disabled, turning it to enabled
                            else {
                                if ( YMLUtils.getData().getBool( "data.offline.disabled." + uuid ) ) { YMLUtils.getData().set( "data.offline.disabled." + uuid, null ); }
                                YMLUtils.getData().set( "data.offline.enabled." + uuid, true );
                                YMLUtils.getData().set( "vanish." + uuid + ".enabled", true );
                                YMLUtils.getData().save();

                                sender.sendMessage( Settings.VANISH_TOGGLE_SENDER_OFFLINE_ENABLED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[ARG]", offline.getName() ) );
                            }
                        }

                        else {
                            sender.sendMessage( Settings.VANISH_TOGGLE_NOT_JOINED_MSG.coloredString()
                                    .replace( "[PLAYER]", sender.getName() )
                                    .replace( "[ARG]", offline.getName() ) );
                        }
                    }
                }

                else {
                    sender.sendMessage( CoreUtils.getColored( "&8/&svanish &ptoggle (player)") );
                }
            }

            else {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            return true;
        }



        // setlevel (integer) sub-command
        // setlevel (player) (integer) sub-command
        else if ( args[0].equalsIgnoreCase( "setlevel" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_SETLEVEL_PERMISSION.string() ) ) {
                if ( args.length >= 2 ) {
                    int newLevel;

                    // setlevel (integer) sub-command
                    if ( args.length == 2 ) {
                        if ( sender instanceof Player ) {
                            try {
                                newLevel = Integer.parseInt( args[ 1 ] );
                                if ( newLevel < 1 || newLevel > 5 ) {
                                    throw new NumberFormatException();
                                }
                            }
                            catch ( NumberFormatException e ) {
                                sender.sendMessage( Settings.VANISH_SETLEVEL_INVALID_LEVEL_MSG.coloredString() );
                                return true;
                            }

                            // making sure the vanish level provided is less than or equal to the sender's maximum vanish level
                            if ( newLevel <= VanishUtils.getMaxVanishLevel( sender ) ) {
                                String playerUUID = ( ( Player ) sender ).getUniqueId().toString();
                                YMLUtils.getData().set( "data." + playerUUID + ".level", newLevel );
                                sender.sendMessage( CoreUtils.getColored( "&sNew vanish level: &p" + newLevel ) );
                                YMLUtils.saveData();
                            }

                            else {
                                sender.sendMessage( Settings.VANISH_SETLEVEL_HIGH_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[ARG]", newLevel + "" ) );
                            }
                        }

                        else {
                            sender.sendMessage( Settings.VANISH_CANT_USE_MSG.coloredString() );
                        }
                    }


                    // setlevel (player) (integer) sub-command
                    else if ( args.length >= 3 ) {
                        if ( VaultUtils.hasPerms( sender, Settings.VANISH_LEVEL_OTHERS_PERMISSION.string() ) ) {
                            try {
                                newLevel = Integer.parseInt( args[2] );
                                if ( newLevel < 1 || newLevel > 5 ) { throw new NumberFormatException(); }
                            }
                            catch ( NumberFormatException e ) {
                                sender.sendMessage( Settings.VANISH_SETLEVEL_INVALID_LEVEL_MSG.coloredString() );
                                return true;
                            }

                            // making sure that vanish level provided is lower than or equal to the sender's maximum vanish level
                            if ( newLevel <= VanishUtils.getMaxVanishLevel( sender ) ) {
                                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );
                                if ( target.hasPlayedBefore() ) {
                                    String targetUUID = target.getUniqueId().toString();
                                    YMLUtils.getData().set( "data." + targetUUID + ".level", newLevel );

                                    sender.sendMessage( CoreUtils.getColored( "&p" + target.getName() + "&s's vanish level has been set to &p" + newLevel ) );

                                    if ( target.isOnline() ) {
                                        Player targetOnline = Bukkit.getServer().getPlayer( args[1] );
                                        targetOnline.sendMessage( CoreUtils.getColored( "&sYour vanish level has been set to &p" + newLevel ) );
                                    }

                                    else {
                                        YMLUtils.getData().set( "data.offline.level-changed." + targetUUID, true );
                                    }

                                    YMLUtils.saveData();
                                }

                                else {
                                    sender.sendMessage( Settings.VANISH_LEVEL_OTHERS_NOT_JOINED_MSG.coloredString()
                                            .replace( "[PLAYER]", sender.getName() )
                                            .replace( "[ARG]", target.getName() ) );
                                }
                            }

                            else {
                                sender.sendMessage( Settings.VANISH_LEVEL_OTHERS_NOT_JOINED_MSG.coloredString()
                                        .replace( "[PLAYER]", sender.getName() )
                                        .replace( "[ARG]", newLevel + "" ) );
                            }
                        }

                        else {
                            sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString()
                                    .replace( "[PLAYER]", sender.getName() ) );
                        }
                    }
                }

                else {
                    sender.sendMessage( CoreUtils.getColored( "&8/&svanish &psetlevel (level)" ) );
                    sender.sendMessage( CoreUtils.getColored( "&8/&svanish &psetlevel (player) (level)" ) );
                }
            }

            else {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            return true;
        }



        // checklevel [player] sub-command
        else if ( args[0].equalsIgnoreCase( "checklevel" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_CHECKLEVEL_PERMISSION.string() ) ) {
                // checklevel sub-command
                if ( args.length >= 2 ) {
                    if ( sender instanceof Player == false ) {
                        sender.sendMessage( Settings.VANISH_CANT_USE_MSG.coloredString() );
                    }

                    else {
                        sender.sendMessage( CoreUtils.getColored( "&p" + sender.getName() + "&s's current level is &p" + VanishUtils.getVanishLevel( ( Player ) sender ) ) );
                    }
                }


                // checklevel [player] sub-command
                else if ( VaultUtils.hasPerms( sender, Settings.VANISH_CHECKLEVEL_OTHERS_PERMISSION.string() ) ) {
                    OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );
                    if ( target.hasPlayedBefore() ) {
                        if ( VanishUtils.getMaxVanishLevel( target ) == 0 ) {
                            sender.sendMessage( CoreUtils.getColored( "&p" + target.getName() + "&s doesn't have a vanish level set!" ) );
                        }

                        else {
                            sender.sendMessage( CoreUtils.getColored( "&p" + target.getName() + "&s's vanish level is &p" + VanishUtils.getVanishLevel( target ) ) );
                        }
                    }

                    else {
                        sender.sendMessage( Settings.VANISH_CHECKLEVEL_OTHERS_NOT_JOINED_MSG.coloredString() );
                    }
                }

                else {
                    sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
                }
            }

            else {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            return true;
        }



        // check [player] sub-command
        else if ( args[0].equalsIgnoreCase( "check" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_CHECK_PERMISSION.string() ) ) {
                // check sub-command
                if ( args.length >= 2 ) {
                    if ( sender instanceof Player == false ) {
                        sender.sendMessage( Settings.VANISH_CANT_USE_MSG.coloredString() );

                        return true;
                    }

                    String UUID = ( ( Player ) sender ).getUniqueId().toString();

                    // player is vanished
                    if ( YMLUtils.getData().getConfig().contains( "vanish." + UUID + ".enabled" ) ) {
                        sender.sendMessage( CoreUtils.getColored( "&sYour vanish is currently &penabled" ) );
                    }

                    // player is not vanished
                    else {
                        sender.sendMessage( CoreUtils.getColored( "&sYour vanish is currently &cdisabled" ) );
                    }
                }


                // check [player] sub-command
                else if ( VaultUtils.hasPerms( sender, Settings.VANISH_CHECK_OTHERS_PERMISSION.string() ) ) {
                    OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );

                    // target is vanished
                    if ( YMLUtils.getData().getConfig().contains( "vanish." + target.getUniqueId().toString() + ".enabled" ) ) {
                        sender.sendMessage( CoreUtils.getColored( "&p" + target.getName() + "&s's vanish is currently &penabled" ) );
                    }

                    // target is not vanished
                    else {
                        sender.sendMessage( CoreUtils.getColored( "&p" + target.getName() + "&s's vanish is currently &cdisabled" ) );
                    }
                }

                else {
                    sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
                }
            }

            else {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            return true;
        }



        // list sub-command
        else if ( args[0].equalsIgnoreCase( "list" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_LIST_PERMISSION.string() ) ) {

                // get an ArrayList of all the vanished players
                ArrayList<String> vanishedPlayers = new ArrayList<>();
                for ( String uuid : VanishUtils.getCurrentlyVanishedUUID() ) {
                    UUID u = UUID.fromString( uuid );

                    // checking if the looped player is online
                    Player p = Bukkit.getServer().getPlayer( u );
                    if ( p != null ) {
                        // ensuring that either the sender has the override permission or the senders max level is higher than the looped players max level
                        int targetLevel = YMLUtils.getData().getInt( "vanish." + uuid + ".level" );
                        if ( VaultUtils.hasPerms( sender, Settings.VANISH_LIST_PERMISSION_ALL.string() ) || VanishUtils.getMaxVanishLevel( sender ) >= targetLevel ) {
                            vanishedPlayers.add( "&p" + p.getName() + "&s" );
                        }
                    }

                    else {
                        // looped player is offline
                        OfflinePlayer offline = Bukkit.getOfflinePlayer( u );
                        if ( offline != null ) {
                            // ensuring that either the sender has the override permission or the senders max level is higher than the looped players max level
                            int targetLevel = YMLUtils.getData().getInt( "vanish." + uuid + ".level" );
                            if ( VaultUtils.hasPerms( sender, Settings.VANISH_LIST_PERMISSION_ALL.string() ) || VanishUtils.getMaxVanishLevel( sender ) >= targetLevel ) {
                                vanishedPlayers.add( "&s" + offline.getName() );
                            }
                        }

                        // issue with finding the player from the UUID provided
                        // most likely means that the data file was editted by an idiot
                        else {
                            CoreUtils.logError( "Could not find an offlineplayer from UUID " + uuid );
                        }
                    }
                }

                sender.sendMessage( CoreUtils.getColored( "&pGreen&s = online, Gray = offline" ) );
                sender.sendMessage( CoreUtils.getColored( "&sList of all vanished players &8- &s\n" + Utils.getStringFromList( vanishedPlayers ) ) );
            }

            else {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            return true;
        }



        // reload sub-command
        else if ( args[0].equalsIgnoreCase( "reload" ) ) {
            if ( VaultUtils.hasPerms( sender, Settings.VANISH_RELOAD_PERMISSION.string() ) == false ) {
                sender.sendMessage( Settings.VANISH_PERMISSION_MSG.coloredString() );
            }

            else {
                sender.sendMessage( CoreUtils.getColored( "&sAttempting to reload &pNaturalVanish&s..." ) );
                CoreUtils.logInfo( CoreUtils.getColored( "Attempting to reload NaturalVanish..." ) );
                
                YMLUtils.getConfig().getYMLManager().initialize();
                YMLUtils.getData().getYMLManager().initialize();
                for ( Settings setting : Settings.values() ) {
                    setting.reload();
                }

                sender.sendMessage( CoreUtils.getColored( "&sSuccessfully reloaded &pNaturalVanish" ) );
                CoreUtils.logInfo( CoreUtils.getColored( "Successfully reloaded NaturalVanish and its files" ) );
            }

            return true;
        }



        String helpMsg[] = { "\n", CoreUtils.getColored( "&8/&svanish" ), CoreUtils.getColored( "&8/&svanish &phelp" ), CoreUtils.getColored( "&8/&svanish &ptoggle (player)"),
                CoreUtils.getColored("&8/&svanish &psetlevel (level)"), CoreUtils.getColored("&8/&svanish &psetlevel (player) (level)"),
                CoreUtils.getColored( "&8/&svanish &pchecklevel [player]"), CoreUtils.getColored("&8/&svanish &pcheck [player]"),
                CoreUtils.getColored( "&8/&svanish &plist"), CoreUtils.getColored("&8/&svanish &preload"), "\n" };
        for ( String s : helpMsg ) {
            sender.sendMessage( s );
        }

        return true;
    }
}
