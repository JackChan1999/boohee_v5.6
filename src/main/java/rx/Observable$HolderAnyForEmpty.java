package rx;

import rx.internal.operators.OperatorAny;
import rx.internal.util.UtilityFunctions;

class Observable$HolderAnyForEmpty {
    static final OperatorAny<?> INSTANCE = new OperatorAny(UtilityFunctions.alwaysTrue(), true);

    private Observable$HolderAnyForEmpty() {
    }
}
