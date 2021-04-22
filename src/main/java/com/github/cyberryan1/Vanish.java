package com.github.cyberryan1;

import com.github.cyberryan1.utils.*;

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
        if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.use.permission" ) ) == false ) {
            sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            return true;
        }



        // toggle the players vanish
        else if ( args.length == 0 ) {
            if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                // making sure the player's permission for the highest level hasn't changed, and changing it to the highest
                    // available level if it has
                if ( VanishUtils.getVanishLevel( player ) > VanishUtils.getMaxVanishLevel( player ) ) {
                    DataUtils.set( "data." + player.getUniqueId().toString() + ".level", VanishUtils.getMaxVanishLevel( player ) );
                }

                VanishUtils.toggleVanish( player );
            }

            // someone besides a player tried to execute the command, which cannot happen
            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.cant-use-message" ) );
            }

            return true;
        }



        // help sub-command
        else if ( args[0].equalsIgnoreCase( "help" ) ) {
            String helpMsg[] = { "\n", Utilities.getColored( "&8/&7vanish" ), Utilities.getColored( "&8/&7vanish &ahelp" ), Utilities.getColored( "&8/&7vanish &atoggle (player)"),
                    Utilities.getColored("&8/&7vanish &asetlevel (level)"), Utilities.getColored("&8/&7vanish &asetlevel (player) (level)"),
                    Utilities.getColored( "&8/&7vanish &achecklevel [player]"), Utilities.getColored("&8/&7vanish &acheck [player]"),
                    Utilities.getColored( "&8/&7vanish &alist"), Utilities.getColored("&8/&7vanish &areload"), "\n" };
            for ( String s : helpMsg ) {
                sender.sendMessage( s );
            }

            return true;
        }



        // toggle (player) sub-command
        else if ( args[0].equalsIgnoreCase( "toggle" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.toggle-others.permission" ) ) ) {
                if ( Utilities.isOutOfBounds( args, 1 ) == false ) {
                    Player target = Bukkit.getServer().getPlayer( args[1] );
                    if ( target != null ) {
                        if ( VanishUtils.getMaxVanishLevel( sender ) >= VanishUtils.getMaxVanishLevel( target ) ) {
                            if ( VanishUtils.toggleVanish( target ) ) {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.enabled-sender", sender, target ) );
                                target.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.enabled-target", sender, target ) );
                            }

                            else {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.disabled-sender", sender, target ) );
                                target.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.disabled-target", sender, target ) );
                            }
                        }

                        else {
                            sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.level-low", sender, target ) );
                        }
                    }

                    // target is offline
                    else {
                        OfflinePlayer offline = Bukkit.getServer().getOfflinePlayer( args[1] );
                        // target has played before
                        if ( offline.hasPlayedBefore() ) {
                            String uuid = offline.getUniqueId().toString();

                            if ( DataUtils.getBool( "vanish." + uuid + ".enabled" ) ) {
                                if ( DataUtils.getBool( "data.offline.enabled." + uuid ) ) { DataUtils.set( "data.offline.enabled." + uuid, null ); }
                                DataUtils.set( "data.offline.disabled." + uuid, true );
                                DataUtils.set( "vanish." + uuid + ".enabled", null );
                                DataUtils.save();

                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.offline.disabled-sender", sender, offline ) );
                            }


                            // Target's vanish was disabled, turning it to enabled
                            else {
                                if ( DataUtils.getBool( "data.offline.disabled." + uuid ) ) { DataUtils.set( "data.offline.disabled." + uuid, null ); }
                                DataUtils.set( "data.offline.enabled." + uuid, true );
                                DataUtils.set( "vanish." + uuid + ".enabled", true );
                                DataUtils.save();

                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.offline.enabled-sender", sender, offline ) );
                            }
                        }

                        else {
                            sender.sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.target-no-join", sender, offline ) );
                        }
                    }
                }

                else {
                    sender.sendMessage( Utilities.getColored( "&8/&7vanish &atoggle (player)") );
                }
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            }

            return true;
        }



        // setlevel (integer) sub-command
        // setlevel (player) (integer) sub-command
        else if ( args[0].equalsIgnoreCase( "setlevel" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.level.use-permission" ) ) ) {
                if ( Utilities.isOutOfBounds( args, 1 ) == false ) {
                    int newLevel;

                    // setlevel (integer) sub-command
                    if ( Utilities.isOutOfBounds( args, 2 ) ) {
                        if ( sender instanceof Player ) {
                            try {
                                newLevel = Integer.parseInt( args[ 1 ] );
                                if ( newLevel < 1 || newLevel > 5 ) {
                                    throw new NumberFormatException();
                                }
                            }
                            catch ( NumberFormatException e ) {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.level.invalid-level-message" ) );
                                return true;
                            }

                            // making sure the vanish level provided is less than or equal to the sender's maximum vanish level
                            if ( newLevel <= VanishUtils.getMaxVanishLevel( sender ) ) {
                                String playerUUID = ( ( Player ) sender ).getUniqueId().toString();
                                DataUtils.set( "data." + playerUUID + ".level", newLevel );
                                sender.sendMessage( Utilities.getColored( "&7New vanish level: &a" + DataUtils.getStr( "data." + playerUUID + ".level" ) ) );
                            }

                            else {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.level.too-high", sender, newLevel + "" ) );
                            }
                        }

                        else {
                            sender.sendMessage( ConfigUtils.getColoredStr( "vanish.cant-use-message" ) );
                        }
                    }


                    // setlevel (player) (integer) sub-command
                    else if ( Utilities.isOutOfBounds( args, 2 ) == false ) {
                        if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.level.use-permission-other" ) ) ) {
                            try {
                                newLevel = Integer.parseInt( args[2] );
                                if ( newLevel < 1 || newLevel > 5 ) { throw new NumberFormatException(); }
                            }
                            catch ( NumberFormatException e ) {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.level.invalid-level-message" ) );
                                return true;
                            }

                            // making sure that vanish level provided is lower than or equal to the sender's maximum vanish level
                            if ( newLevel <= VanishUtils.getMaxVanishLevel( sender ) ) {
                                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );
                                if ( target.hasPlayedBefore() ) {
                                    String targetUUID = target.getUniqueId().toString();
                                    DataUtils.set( "data." + targetUUID + ".level", newLevel );

                                    sender.sendMessage( Utilities.getColored( "&a" + target.getName() + "&7's vanish level has been set to &a" + DataUtils.getStr( "data." + targetUUID + ".level" ) ) );

                                    if ( target.isOnline() ) {
                                        Player targetOnline = Bukkit.getServer().getPlayer( args[1] );
                                        targetOnline.sendMessage( Utilities.getColored( "&7Your vanish level has been set to &a" + DataUtils.getStr( "data." + targetUUID + ".level" ) ) );
                                    }

                                    else {
                                        DataUtils.set( "data.offline.level-changed." + targetUUID, true );
                                    }
                                }

                                else {
                                    sender.sendMessage( ConfigUtils.getColoredStr( "vanish.level.target-no-join", sender, target ) );
                                }
                            }

                            else {
                                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.level.too-high", sender, newLevel + "" ) );
                            }
                        }

                        else {
                            sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message", sender ) );
                        }
                    }
                }

                else {
                    sender.sendMessage( Utilities.getColored( "&8/&7vanish &asetlevel (level)" ) );
                    sender.sendMessage( Utilities.getColored( "&8/&7vanish &asetlevel (player) (level)" ) );
                }
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            }

            return true;
        }



        // checklevel [player] sub-command
        else if ( args[0].equalsIgnoreCase( "checklevel" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.check-level.permission") ) ) {
                // checklevel sub-command
                if ( Utilities.isOutOfBounds( args, 1 ) ) {
                    if ( sender instanceof Player == false ) {
                        sender.sendMessage( ConfigUtils.getColoredStr( "vanish.cant-use-message" ) );
                    }

                    else {
                        sender.sendMessage( Utilities.getColored( "&a" + sender.getName() + "&7's current level is &a" + VanishUtils.getVanishLevel( ( Player ) sender ) ) );
                    }
                }


                // checklevel [player] sub-command
                else if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.check-level.permission-other" ) ) ) {
                    OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );
                    if ( target.hasPlayedBefore() ) {
                        if ( VanishUtils.getMaxVanishLevel( target ) == 0 ) {
                            sender.sendMessage( Utilities.getColored( "&a" + target.getName() + "&7 doesn't have a vanish level set!" ) );
                        }

                        else {
                            sender.sendMessage( Utilities.getColored( "&a" + target.getName() + "&7's vanish level is &a" + VanishUtils.getVanishLevel( target ) ) );
                        }
                    }

                    else {
                        sender.sendMessage( ConfigUtils.getColoredStr( "vanish.check-level.target-not-joined" ) );
                    }
                }

                else {
                    sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
                }
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            }

            return true;
        }



        // check [player] sub-command
        else if ( args[0].equalsIgnoreCase( "check" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.check.permission" ) ) ) {
                // check sub-command
                if ( Utilities.isOutOfBounds( args, 1 ) ) {
                    if ( sender instanceof Player == false ) {
                        sender.sendMessage( ConfigUtils.getColoredStr( "vanish.cant-use-message" ) );

                        return true;
                    }

                    String UUID = ( ( Player ) sender ).getUniqueId().toString();

                    // player is vanished
                    if ( DataUtils.getConfig().contains( "vanish." + UUID + ".enabled" ) ) {
                        sender.sendMessage( Utilities.getColored( "&7Your vanish is currently &aenabled" ) );
                    }

                    // player is not vanished
                    else {
                        sender.sendMessage( Utilities.getColored( "&7Your vanish is currently &cdisabled" ) );
                    }
                }


                // check [player] sub-command
                else if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.check.permission-other" ) ) ) {
                    OfflinePlayer target = Bukkit.getServer().getOfflinePlayer( args[1] );

                    // target is vanished
                    if ( DataUtils.getConfig().contains( "vanish." + target.getUniqueId().toString() + ".enabled" ) ) {
                        sender.sendMessage( Utilities.getColored( "&a" + target.getName() + "&7's vanish is currently &aenabled" ) );
                    }

                    // target is not vanished
                    else {
                        sender.sendMessage( Utilities.getColored( "&a" + target.getName() + "&7's vanish is currently &cdisabled" ) );
                    }
                }

                else {
                    sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
                }
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            }

            return true;
        }



        // list sub-command
        else if ( args[0].equalsIgnoreCase( "list" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.list.permission" ) ) ) {

                // get an ArrayList of all the vanished players
                ArrayList<String> vanishedPlayers = new ArrayList<>();
                for ( String uuid : VanishUtils.getCurrentlyVanishedUUID() ) {
                    UUID u = UUID.fromString( uuid );

                    // checking if the looped player is online
                    Player p = Bukkit.getServer().getPlayer( u );
                    if ( p != null ) {
                        // ensuring that either the sender has the override permission or the senders max level is higher than the looped players max level
                        int targetLevel = DataUtils.getInt( "vanish." + uuid + ".level" );
                        if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.list.permission-override" ) ) || VanishUtils.getMaxVanishLevel( sender ) >= targetLevel ) {
                            vanishedPlayers.add( "&a" + p.getName() + "&7" );
                        }
                    }

                    else {
                        // looped player is offline
                        OfflinePlayer offline = Bukkit.getOfflinePlayer( u );
                        if ( offline != null ) {
                            // ensuring that either the sender has the override permission or the senders max level is higher than the looped players max level
                            int targetLevel = DataUtils.getInt( "vanish." + uuid + ".level" );
                            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "vanish.list.permission-override" ) ) || VanishUtils.getMaxVanishLevel( sender ) >= targetLevel ) {
                                vanishedPlayers.add( "&7" + offline.getName() );
                            }
                        }

                        // issue with finding the player from the UUID provided
                        // most likely means that the data file was editted by an idiot
                        else {
                            Utilities.logError( "Could not find an offlineplayer from UUID " + uuid );
                        }
                    }
                }

                sender.sendMessage( Utilities.getColored( "&aGreen&7 = online, Gray = offline" ) );
                sender.sendMessage( Utilities.getColored( "&7List of all vanished players &8- &7\n" + Utilities.getStringFromList( vanishedPlayers ) ) );
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "vanish.permission-message" ) );
            }

            return true;
        }



        // reload sub-command
        else if ( args[0].equalsIgnoreCase( "reload" ) ) {
            if ( VaultUtils.hasPerms( sender, ConfigUtils.getStr( "reload.permission" ) ) == false ) {
                sender.sendMessage( ConfigUtils.getColoredStr( "reload.permission-message" ) );
            }

            else {
                sender.sendMessage( ConfigUtils.getColoredStr( "reload.player-message-start", sender ) );
                Utilities.logInfo( ConfigUtils.getColoredStr( "reload.console-message-start", sender ) );

                ConfigUtils.getConfigManager().reloadConfig();
                DataUtils.getDataManager().reloadConfig();

                sender.sendMessage( ConfigUtils.getColoredStr( "reload.player-message-complete", sender ) );
                Utilities.logInfo( ConfigUtils.getColoredStr( "reload.console-message-complete", sender ) );
            }

            return true;
        }



        String helpMsg[] = { "\n", Utilities.getColored( "&8/&7vanish" ), Utilities.getColored( "&8/&7vanish &ahelp" ), Utilities.getColored( "&8/&7vanish &atoggle (player)"),
                Utilities.getColored("&8/&7vanish &asetlevel (level)"), Utilities.getColored("&8/&7vanish &asetlevel (player) (level)"),
                Utilities.getColored( "&8/&7vanish &achecklevel [player]"), Utilities.getColored("&8/&7vanish &acheck [player]"),
                Utilities.getColored( "&8/&7vanish &alist"), Utilities.getColored("&8/&7vanish &areload"), "\n" };
        for ( String s : helpMsg ) {
            sender.sendMessage( s );
        }

        return true;
    }
}
