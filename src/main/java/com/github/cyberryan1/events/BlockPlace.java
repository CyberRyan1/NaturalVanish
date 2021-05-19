package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.Utilities;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BlockPlace implements Listener {

    private ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onBlockPlace( BlockPlaceEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) ) {
            if ( ConfigUtils.getBool( "vanish.other-events.build.cancel" ) ) {
                if ( ConfigUtils.getBool( "vanish.other-events.build.bypass" ) == false ||
                        VaultUtils.hasPerms( event.getPlayer(), ConfigUtils.getStr( "vanish.other-events.build.bypass-perm" ) ) == false ) {
                    event.setCancelled( true );

                    if ( ConfigUtils.getStr( "vanish.other-events.build.cancel-msg" ).equals( "" ) == false &&
                            cooldown.contains( event.getPlayer().getName() ) == false ) {
                        cooldown.add( event.getPlayer().getName() );
                        Bukkit.getScheduler().runTaskLater( Utilities.getPlugin(), () -> {
                            cooldown.remove( event.getPlayer().getName() );
                        }, 40L );
                    }
                }
            }
        }
    }
}
