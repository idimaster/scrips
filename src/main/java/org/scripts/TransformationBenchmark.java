package org.scripts;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.scripts.drools.Drools;
import org.scripts.drools.EvaluationContext;
import org.scripts.drools.EvaluationItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@State(Scope.Thread)
public class TransformationBenchmark {

    private Drools drools;

    {
        try {
            URL url = Resources.getResource("transformation.drl");
            String rule = Resources.toString(url, Charsets.UTF_8);
            drools = new Drools();
            drools.init(rule);
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Benchmark
    public void DroolsTrasform() throws Exception {
        List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "tes{t}@gmail.com", "", false));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", "", false));
        EvaluationContext context = new EvaluationContext("test", "p1", items);
        EvaluationContext result = drools.evaluate(context);
    }
}
