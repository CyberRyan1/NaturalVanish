package com.github.cyberryan1;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.events.*;
import com.github.cyberryan1.skriptelements.conditions.RegisterConditions;
import com.github.cyberryan1.utils.BossbarUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class NaturalVanish extends JavaPlugin {

    // Skript
    public SkriptAddon addon;
    public boolean enabled = true;
    
    @Override
    public void onEnable() {
        // Initialize CyberCore
        CyberCore.setPlugin( this );
        new VaultUtils();
        CyberCore.setPrimaryColor( Settings.PRIMARY_COLOR.coloredString() );
        CyberCore.setSecondaryColor( Settings.SECONDARY_COLOR.coloredString() );

        // Update/reload config/data files
        YMLUtils.getConfig().getYMLManager().initialize();
        YMLUtils.getData().getYMLManager().initialize();
        YMLUtils.getData().sendPathNotFoundWarns( false );

        // Remove all bossbars
        BossbarUtils.removeAllBossbars();

        // Attempt to register Skript syntax
        try {
            addon = Skript.registerAddon( this );
            try {
                addon.loadClasses( "com.github.cyberryan1", "skriptelements" );
            } catch ( IOException e ) {
                CoreUtils.logWarn( "Could not enable as a skript addon, will still enable without this syntax!" );
                enabled = false;
            }
            CoreUtils.logInfo( "Enabled as a skript addon" );
            RegisterConditions.register();
        } catch ( NoClassDefFoundError error ) {
            CoreUtils.logWarn( "Could not enable as a skript addon, will still enable without this syntax!" );
            enabled = false;
        }

        loadCommands();
        loadEvents();
    }

    private void loadCommands() {
        this.getCommand( "vanish" ).setExecutor( new Vanish() );
        this.getCommand( "vanish" ).setTabCompleter( new TabComplete() );
    }

    private void loadEvents() {
        this.getServer().getPluginManager().registerEvents( new InventoryClick(), this );
        this.getServer().getPluginManager().registerEvents( new Join(), this );
        this.getServer().getPluginManager().registerEvents( new Leave(), this );
        this.getServer().getPluginManager().registerEvents( new ItemDrop(), this );
        this.getServer().getPluginManager().registerEvents( new ItemPickup(), this );
        this.getServer().getPluginManager().registerEvents( new Interact(), this );
        this.getServer().getPluginManager().registerEvents( new BlockPlace(), this );
        this.getServer().getPluginManager().registerEvents( new BlockBreak(), this );
        this.getServer().getPluginManager().registerEvents( new Damage(), this );
        this.getServer().getPluginManager().registerEvents( new DamageByEntity(), this );
        this.getServer().getPluginManager().registerEvents( new Hunger(), this );
    }
}
