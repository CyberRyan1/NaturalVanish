package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.BossbarUtils;
import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.DataUtils;
import com.github.cyberryan1.utils.VanishUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener {

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        if ( DataUtils.getBool( "vanish." + event.getPlayer().getUniqueId().toString() + ".enabled" ) ) {
            // disable the vanish (config)
            if ( ConfigUtils.getBool( "vanish.leave.disable" ) ) {
                VanishUtils.disableVanish( event.getPlayer() );
            }


            // cancel the quit message (config)
            if ( ConfigUtils.getBool( "vanish.leave.cancel-msg" ) ) {
                event.setQuitMessage( null );
            }


            // remove the player's bossbar (if enabled)
            if ( ConfigUtils.getBool( "vanish.use.bossbar.enable" ) ) {
                BossbarUtils.removeBossbar( event.getPlayer() );
            }
        }
    }
}
