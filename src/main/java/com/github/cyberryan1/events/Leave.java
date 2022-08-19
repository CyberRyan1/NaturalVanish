package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.BossbarUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener {

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        if ( YMLUtils.getData().getBool( "vanish." + event.getPlayer().getUniqueId().toString() + ".enabled" ) ) {
            // disable the vanish (config)
            if ( Settings.VANISH_LEAVE_DISABLE_VANISH.bool() ) {
                VanishUtils.disableVanish( event.getPlayer() );
            }


            // cancel the quit message (config)
            if ( Settings.VANISH_LEAVE_CANCEL_MSG.bool() ) {
                event.setQuitMessage( null );
            }


            // remove the player's bossbar (if enabled)
            if ( Settings.VANISH_BOSSBAR_ENABLE.bool() ) {
                BossbarUtils.removeBossbar( event.getPlayer() );
            }
        }
    }
}
