package org.scripts.mvel;

import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;
import org.scripts.drools.EvaluationContext;

import java.util.HashMap;
import java.util.Map;

public class Transform {
    private final CompiledExpression expression;

    public Transform(String script) {
        expression = new ExpressionCompiler(script).compile();
    }

    public EvaluationContext evaluate(EvaluationContext context) {
        Map vars = new HashMap();
        vars.put("context", context);
        return (EvaluationContext) MVEL.executeExpression(expression, vars);
    }
}
