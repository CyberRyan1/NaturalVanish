package com.github.cyberryan1.skriptelements.conditions;

import ch.njol.skript.Skript;
import com.github.cyberryan1.skriptelements.conditions.CondPlayerVanished;

public class RegisterConditions {

    public static void register() {
        // CondPlayerVanished
        Skript.registerCondition( CondPlayerVanished.class, "%player% (1¦is|2¦is(n't| not)) vanished" );
    }
}
