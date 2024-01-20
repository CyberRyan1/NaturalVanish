package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.spigot.CyberCore;
import com.github.cyberryan1.cybercore.spigot.utils.CyberMsgUtils;
import com.github.cyberryan1.cybercore.spigot.utils.CyberVaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
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
        if ( Settings.VANISH_EVENTS_PVP_CANCEL.bool() == false ) { return; }
        if ( event.getDamager() instanceof Player && event.getEntity() instanceof Player ) {
            Player attacker = ( Player ) event.getDamager();
            Player victim = ( Player ) event.getEntity();

            if ( VanishUtils.checkVanished( attacker ) &&
                    ( Settings.VANISH_EVENTS_PVP_BYPASS.bool() == false
                            | CyberVaultUtils.hasPerms( attacker, Settings.VANISH_EVENTS_PVP_BYPASS_PERMISSION.string() ) ) ) {
                event.setCancelled( true );

                String cancelMsg = Settings.VANISH_EVENTS_PVP_CANCEL_MSG.coloredString();
                if ( cancelMsg.isBlank() == false && cooldown.contains( attacker.getName() ) == false ) {
                    cancelMsg = cancelMsg.replace( "[PLAYER]", attacker.getName() )
                            .replace( "[ARG]", victim.getName() );
                    CyberMsgUtils.sendMsg( attacker, cancelMsg );
                    cooldown.add( attacker.getName() );

                    Bukkit.getScheduler().runTaskLater( CyberCore.getPlugin(), () -> {
                        cooldown.remove( attacker.getName() );
                    }, 40L );
                }
            }
        }
    }
}
