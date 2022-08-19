package com.github.cyberryan1.utils.settings;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Settings {

    // General Settings
    VANISH_PERMISSION( "vanish.permission", "string" ),
    VANISH_PERMISSION_MSG( "vanish.permission-msg", "string" ),
    VANISH_OP_OVERRIDE( "vanish.op-override", "boolean" ),
    VANISH_CANT_USE_MSG( "vanish.cant-use-msg", "string" ),

    // Vanish Use Settings
    VANISH_FLIGHT_ENABLE( "vanish.flight.enable", "boolean" ),
    VANISH_RESET_FLIGHT( "vanish.flight.reset", "boolean" ),
    VANISH_FLIGHT_SPEED( "vanish.flight.speed", "float" ),

    VANISH_WALK_SPEED( "vanish.walk-speed", "float" ),
    VANISH_NIGHT_VISION( "vanish.night-vision", "boolean" ),
    VANISH_SEE_SELF( "vanish.see-self", "boolean" ),
    VANISH_SKULL_HELMET( "vanish.skull-helmet", "boolean" ),

    VANISH_BOSSBAR_ENABLE( "vanish.bossbar.enable", "boolean" ),
    VANISH_BOSSBAR_TITLE( "vanish.bossbar.title", "string" ),
    VANISH_BOSSBAR_COLOR( "vanish.bossbar.color", "string" ),
    VANISH_BOSSBAR_PERCENT( "vanish.bossbar.percent", "int" ),

    // Vanish Setlevel Settings
    VANISH_SETLEVEL_PERMISSION( "vanish.level.permission", "string" ),
    VANISH_HIGH_MSG( "vanish.level.high-msg", "string" ),
    VANISH_INVALID_LEVEL_MSG( "vanish.level.invalid-level-msg", "string" ),

    VANISH_LEVEL_ONE_ENABLE_MSG( "vanish.level.1.enable-msg", "string" ),
    VANISH_LEVEL_ONE_DISABLE_MSG( "vanish.level.1.disable-msg", "string" ),

    VANISH_LEVEL_TWO_PERMISSION( "vanish.level.2.permission", "string" ),
    VANISH_LEVEL_TWO_ENABLE_MSG( "vanish.level.2.enable-msg", "string" ),
    VANISH_LEVEL_TWO_DISABLE_MSG( "vanish.level.2.disable-msg", "string" ),

    VANISH_LEVEL_THREE_PERMISSION( "vanish.level.3.permission", "string" ),
    VANISH_LEVEL_THREE_ENABLE_MSG( "vanish.level.3.enable-msg", "string" ),
    VANISH_LEVEL_THREE_DISABLE_MSG( "vanish.level.3.disable-msg", "string" ),

    VANISH_LEVEL_FOUR_PERMISSION( "vanish.level.4.permission", "string" ),
    VANISH_LEVEL_FOUR_ENABLE_MSG( "vanish.level.4.enable-msg", "string" ),
    VANISH_LEVEL_FOUR_DISABLE_MSG( "vanish.level.4.disable-msg", "string" ),

    VANISH_LEVEL_FIVE_PERMISSION( "vanish.level.5.permission", "string" ),
    VANISH_LEVEL_FIVE_ENABLE_MSG( "vanish.level.5.enable-msg", "string" ),
    VANISH_LEVEL_FIVE_DISABLE_MSG( "vanish.level.5.disable-msg", "string" ),

    // Vanish Setlevel Other Players Settings
    VANISH_LEVEL_OTHERS_PERMISSION( "vanish.level-others.permission", "string" ),
    VANISH_LEVEL_OTHERS_NOT_JOINED_MSG( "vanish.level-others.not-joined-msg", "string" ),

    // Vanish Toggle Other Players Settings
    VANISH_TOGGLE_PERMISSION( "vanish.toggle.permission", "string" ),
    VANISH_TOGGLE_LEVEL_LOW_MSG( "vanish.toggle.level-low-msg", "string" ),
    VANISH_TOGGLE_NOT_JOINED_MSG( "vanish.toggle.not-joined-msg", "string" ),

    VANISH_TOGGLE_SENDER_ENABLED_MSG( "vanish.toggle.sender.enabled", "string" ),
    VANISH_TOGGLE_SENDER_DISABLED_MSG( "vanish.toggle.sender.disabled", "string" ),
    VANISH_TOGGLE_SENDER_OFFLINE_ENABLED_MSG( "vanish.toggle.sender.off-enabled", "string" ),
    VANISH_TOGGLE_SENDER_OFFLINE_DISABLED_MSG( "vanish.toggle.sender.off-disabled", "string" ),

    VANISH_TOGGLE_TARGET_ENABLED_MSG( "vanish.toggle.target.enabled", "string" ),
    VANISH_TOGGLE_TARGET_DISABLED_MSG( "vanish.toggle.target.disabled", "string" ),
    VANISH_TOGGLE_TARGET_OFFLINE_ENABLED_MSG( "vanish.toggle.target.off-enabled", "string" ),
    VANISH_TOGGLE_TARGET_OFFLINE_DISABLED_MSG( "vanish.toggle.target.off-disabled", "string" ),

    // Vanish Checklevel Settings
    VANISH_CHECKLEVEL_PERMISSION( "vanish.checklevel.permission", "string" ),

    // Vanish Checklevel Other Players Settings
    VANISH_CHECKLEVEL_OTHERS_PERMISSION( "vanish.checklevel-other.permission", "string" ),
    VANISH_CHECKLEVEL_OTHERS_NOT_JOINED_MSG( "vanish.checklevel-other.not-joined-msg", "string" ),

    // Vanish Check Settings
    VANISH_CHECK_PERMISSION( "vanish.check.permission", "string" ),

    // Vanish Check Other Players Settings
    VANISH_CHECK_OTHERS_PERMISSION( "vanish.check-other.permission", "string" ),

    // Vanish List Settings
    VANISH_LIST_PERMISSION( "vanish.list.permission", "string" ),
    VANISH_LIST_PERMISSION_ALL( "vanish.list.permission-all", "string" ),

    // Vanish Reload Settings
    VANISH_RELOAD_PERMISSION( "vanish.reload.permission", "string" ),

    // Cancel Settings
    VANISH_CANCEL_SKULL_MSG( "vanish.cancel.skull", "string" ),

    // Join Settings
    VANISH_JOIN_CANCEL_MSG( "vanish.join.cancel-msg", "boolean" ),

    // Leave Settings
    VANISH_LEAVE_CANCEL_MSG( "vanish.leave.cancel-msg", "boolean" ),
    VANISH_LEAVE_DISABLE_VANISH( "vanish.leave.disable", "boolean" ),

    // Other Events Settings
    VANISH_EVENTS_PICKUP_CANCEL( "vanish.other-events.item-pickup.cancel", "boolean" ),
    VANISH_EVENTS_PICKUP_CANCEL_MSG( "vanish.other-events.item-pickup.cancel-msg", "string" ),
    VANISH_EVENTS_PICKUP_BYPASS( "vanish.other-events.item-pickup.bypass", "boolean" ),
    VANISH_EVENTS_PICKUP_BYPASS_PERMISSION( "vanish.other-events.item-pickup.bypass-perm", "string" ),

    VANISH_EVENTS_DROP_CANCEL( "vanish.other-events.item-drop.cancel", "boolean" ),
    VANISH_EVENTS_DROP_CANCEL_MSG( "vanish.other-events.item-drop.cancel-msg", "string" ),
    VANISH_EVENTS_DROP_BYPASS( "vanish.other-events.item-drop.bypass", "boolean" ),
    VANISH_EVENTS_DROP_BYPASS_PERMISSION( "vanish.other-events.item-drop.bypass-perm", "string" ),

    VANISH_EVENTS_CHEST_SILENT( "vanish.other-events.chest-open.silent", "boolean" ),
    VANISH_EVENTS_CHEST_SILENT_MSG( "vanish.other-events.chest-open.silent-msg", "string" ),

    VANISH_EVENTS_INTERACT_CANCEL( "vanish.other-events.interact.cancel", "boolean" ),
    VANISH_EVENTS_INTERACT_CANCEL_MSG( "vanish.other-events.interact.cancel-msg", "string" ),
    VANISH_EVENTS_INTERACT_BYPASS( "vanish.other-events.interact.bypass", "boolean" ),
    VANISH_EVENTS_INTERACT_BYPASS_PERMISSION( "vanish.other-events.interact.bypass-perm", "string" ),

    VANISH_EVENTS_DAMAGE_CANCEL( "vanish.other-events.damage.cancel", "boolean" ),

    VANISH_EVENTS_PVP_CANCEL( "vanish.other-events.pvp.cancel", "boolean" ),
    VANISH_EVENTS_PVP_CANCEL_MSG( "vanish.other-events.pvp.cancel-msg", "string" ),
    VANISH_EVENTS_PVP_BYPASS( "vanish.other-events.pvp.bypass", "boolean" ),
    VANISH_EVENTS_PVP_BYPASS_PERMISSION( "vanish.other-events.pvp.bypass-perm", "string" ),

    VANISH_EVENTS_BUILD_CANCEL( "vanish.other-events.build.cancel", "boolean" ),
    VANISH_EVENTS_BUILD_CANCEL_MSG( "vanish.other-events.build.cancel-msg", "string" ),
    VANISH_EVENTS_BUILD_BYPASS( "vanish.other-events.build.bypass", "boolean" ),
    VANISH_EVENTS_BUILD_BYPASS_PERMISSION( "vanish.other-events.build.bypass-perm", "string" ),

    VANISH_EVENTS_HUNGER_CANCEL( "vanish.other-events.hunger.cancel", "boolean" ),

    ;


    private final String path;
    private SettingsEntry value;
    Settings( String path, String valueType ) {
        this.path = path;
        this.value = new SettingsEntry( path, valueType );
    }

    public void reload() {
        this.value = new SettingsEntry( this.path, this.value.getValueType() );
    }

    public String getPath() { return this.path; }

    public SettingsEntry getValue() { return this.value; }

    public int integer() { return value.integer(); }

    public String string() { return value.string(); }

    public String coloredString() { return CoreUtils.getColored( value.string() ); }

    public float getFloat() { return value.getFloat(); }

    public double getDouble() { return value.getDouble(); }

    public long getLong() { return value.getLong(); }

    public boolean bool() { return value.bool(); }

    public Material material() { return value.material(); }

    public String[] stringlist() { return value.stringlist(); }

    public String[] coloredStringlist() {
        String[] toReturn = new String[ stringlist().length ];
        for ( int i = 0; i < stringlist().length; i++ ) {
            toReturn[i] = CoreUtils.getColored( stringlist()[i] );
        }
        return toReturn;
    }

    public List<String> arraylist() { return new ArrayList<>( Arrays.asList( stringlist() ) ); }

    public List<String> coloredArraylist() { return new ArrayList<>( Arrays.asList( coloredStringlist() ) ); }
}