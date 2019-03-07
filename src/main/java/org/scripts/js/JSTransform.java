package org.scripts.js;

import org.scripts.drools.EvaluationContext;

import javax.script.*;

public class JSTransform {

    private Invocable invocable;

    public JSTransform(String script) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        invocable = (Invocable) engine;
        CompiledScript cs = ((Compilable) engine).compile(script);
        cs.eval();
    }

    public EvaluationContext evaluate(EvaluationContext context) throws Exception {
        return (EvaluationContext) invocable.invokeFunction("transform", context);
    }
}
