package com.github.cyberryan1.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    List<String> arguments = new ArrayList<String>();
    List<String> intArgs = new ArrayList<String>();
    List<String> intArgTwoCmds = new ArrayList<String>();
    List<String> playerArgTwoCmds = new ArrayList<String>();
    List<String> intArgThreeCmds = new ArrayList<String>();



    public TabComplete() {
        arguments.add( "help" ); arguments.add( "toggle" ); arguments.add( "setlevel" );
        arguments.add( "checklevel" ); arguments.add( "check" ); arguments.add( "list" );

        for ( int numb = 1; numb <= 5; numb++ ) {
            intArgs.add( numb + "" );
        }

        intArgTwoCmds.add( "setlevel" );
        playerArgTwoCmds.add( "toggle" ); playerArgTwoCmds.add( "setlevel" );
        playerArgTwoCmds.add( "checklevel" ); playerArgTwoCmds.add( "check" );

        intArgThreeCmds.add( "setlevel" );
    }



    public List<String> onTabComplete( CommandSender sender, Command command, String label, String args[] ) {
        List<String> results = new ArrayList<String>();
        if ( args.length == 1 ) {
            for ( String a : arguments ) {
                if ( a.toLowerCase().startsWith( args[0].toLowerCase() ) ) {
                    results.add( a );
                }
            }

            return results;
        }

        else if ( args.length == 2 ) {
            if ( intArgTwoCmds.contains( args[0].toLowerCase() ) && args[1].length() == 0 ) {
                for ( String i : intArgs ) {
                    results.add( i );
                }
            }

            if ( playerArgTwoCmds.contains( args[0].toLowerCase() ) ) {
                for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {
                    if ( p.getName().toLowerCase().startsWith( args[1].toLowerCase() ) ) {
                        results.add( p.getName() );
                    }
                }
            }

            return results;
        }

        else if ( args.length == 3 ) {
            if ( intArgThreeCmds.contains( args[0].toLowerCase() ) ) {
                return intArgs;
            }
        }

        return null;
    }
}
