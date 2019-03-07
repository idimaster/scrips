package org.scripts;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.*;
import org.scripts.drools.Drools;
import org.scripts.drools.EvaluationContext;
import org.scripts.drools.EvaluationItem;
import org.scripts.mvel.Transform;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class TransformationBenchmark {

    Drools drools;
    Transform transform;

    @Setup
    public void prepare() {
        try {
            URL url = Resources.getResource("transformation.drl");
            String rule = Resources.toString(url, Charsets.UTF_8);
            drools = new Drools(rule);
            url = Resources.getResource("transform.mvl");
            String script = Resources.toString(url, Charsets.UTF_8);
            transform = new Transform(script);
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static EvaluationContext createTestContext() {
        final List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "tes{t}@gmail.com", "", false));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", "", false));
        EvaluationContext context = new EvaluationContext("test", "p1", items);
        return context;
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void DroolsTransform() throws Exception {
        EvaluationContext result = drools.evaluate(createTestContext());
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void MVELTransform() {
        EvaluationContext result = transform.evaluate(createTestContext());
    }
}
