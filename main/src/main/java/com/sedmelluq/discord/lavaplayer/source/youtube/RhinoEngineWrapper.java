package com.sedmelluq.discord.lavaplayer.source.youtube;

import org.mozilla.javascript.*;

public class RhinoEngineWrapper {

    public Object invokeFunction(String name, Object... args) {
        Context context = enterContext();
        Scriptable scope = context.initStandardObjects(new ImporterTopLevel(context));
        Object function = scope.get(name, scope);
        if (function == Scriptable.NOT_FOUND || !(function instanceof Function))
            return null;

        Object result = ((Function) function).call(context, scope, scope, args);
        Context.exit();
        return result;
    }

    public Object eval(String script) {
        Context context = enterContext();
        Scriptable scope = context.initStandardObjects(new ImporterTopLevel(context));
        Object result = context.evaluateString(scope, script, "eval", 0, null);
        Context.exit();
        return result;
    }

    private Context enterContext() {
        Context cx = ContextFactory.getGlobal().enterContext();
        cx.setOptimizationLevel(-1);
        return cx;
    }
}
