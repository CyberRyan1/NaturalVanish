package com.github.cyberryan1;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.cyberryan1.events.*;
import com.github.cyberryan1.managers.ConfigManager;
import com.github.cyberryan1.managers.DataManager;
import com.github.cyberryan1.skriptelements.conditions.RegisterConditions;
import com.github.cyberryan1.utils.*;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class NaturalVanish extends JavaPlugin {

    // config/data managers
    public ConfigManager config;
    public DataManager data;

    // utilities that need to be initialized
    public Utilities util;
    public ConfigUtils configUtils;
    public DataUtils dataUtils;
    public VaultUtils vaultUtils;

    // Skript
    public SkriptAddon addon;
    public boolean enabled = true;
    
    @Override
    public void onEnable() {
        config = new ConfigManager( this );
        data = new DataManager( this );

        util = new Utilities( this, config, data );
        configUtils = new ConfigUtils( config );
        dataUtils = new DataUtils( data );
        vaultUtils = new VaultUtils();

        this.getCommand( "vanish" ).setExecutor( new Vanish() );
        this.getCommand( "vanish" ).setTabCompleter( new TabComplete() );

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

        ConfigUtils.getConfigManager().reloadConfig();

        // Remove all bossbars
        BossbarUtils.removeAllBossbars();

        // Attempt to register Skript syntax
        try {
            addon = Skript.registerAddon( this );
            try {
                addon.loadClasses( "com.github.cyberryan1", "skriptelements" );
            } catch ( IOException e ) {
                Utilities.logWarn( "Could not enable as a skript addon, will still enable without this syntax!" );
                enabled = false;
            }
            Utilities.logInfo( "Enabled as a skript addon" );
            RegisterConditions.register();
        } catch ( NoClassDefFoundError error ) {
            Utilities.logWarn( "Could not enable as a skript addon, will still enable without this syntax!" );
            enabled = false;
        }
    }
}
