package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.Utilities;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class DamageByEntity implements Listener {

    private final ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onEntityDamageByEntity( EntityDamageByEntityEvent event ) {
        if ( event.getDamager() instanceof Player && event.getEntity() instanceof Player ) {
            Player attacker = ( Player ) event.getDamager();
            Player victim = ( Player ) event.getEntity();

            if ( ConfigUtils.getBool( "vanish.other-events.pvp.cancel" ) ) {
                if ( VanishUtils.checkVanished( attacker ) &&
                        ( ConfigUtils.getBool( "vanish.other-events.pvp.bypass" ) == false ||
                                VaultUtils.hasPerms( attacker, ConfigUtils.getStr( "vanish.other-events.pvp.bypass-perm" ) ) == false ) ) {
                    event.setCancelled( true );

                    if ( ConfigUtils.getStr( "vanish.other-events.pvp.cancel-msg" ).equals( "" ) == false &&
                            cooldown.contains( attacker.getName() ) == false ) {
                        attacker.sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.pvp.cancel-msg", attacker, victim ) );
                        cooldown.add( attacker.getName() );

                        Bukkit.getScheduler().runTaskLater( Utilities.getPlugin(), () -> {
                            cooldown.remove( attacker.getName() );
                        }, 40L );
                    }
                }
            }
        }
    }
}
