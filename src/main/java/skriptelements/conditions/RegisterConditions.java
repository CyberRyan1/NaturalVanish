package skriptelements.conditions;

import ch.njol.skript.Skript;

public class RegisterConditions {

    public static void register() {
        // CondPlayerVanished
        Skript.registerCondition( CondPlayerVanished.class, "%player% (1¦is|2¦is(n't| not)) vanished" );
    }
}
