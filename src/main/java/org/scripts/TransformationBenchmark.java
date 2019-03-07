package org.scripts;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.*;
import org.scripts.drools.Drools;
import org.scripts.drools.EvaluationContext;
import org.scripts.drools.EvaluationItem;
import org.scripts.js.JSTransform;
import org.scripts.mvel.Transform;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TransformationBenchmark {

    Drools drools;
    Transform transform;
    JSTransform jsTransform;

    @Setup
    public void prepare() throws Exception {
        try {
            URL url = Resources.getResource("transformation.drl");
            String rule = Resources.toString(url, Charsets.UTF_8);
            drools = new Drools(rule);

            url = Resources.getResource("transform.mvl");
            String script = Resources.toString(url, Charsets.UTF_8);
            transform = new Transform(script);

            url = Resources.getResource("transform.js");
            script = Resources.toString(url, Charsets.UTF_8);
            jsTransform = new JSTransform(script);
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
    public void DroolsTransform() throws Exception {
        EvaluationContext result = drools.evaluate(createTestContext());
    }

    @Benchmark
    public void MVELTransform() {
        EvaluationContext result = transform.evaluate(createTestContext());
    }

    @Benchmark
    public void JSTransform() throws Exception {
        EvaluationContext result = jsTransform.evaluate(createTestContext());
    }

    @Benchmark
    public void JavaTransform() {
        EvaluationContext context = createTestContext();
        for(EvaluationItem item: context.getItems()) {
            if ("email".equals(item.getKey())) {
                // remove comments
                String result = item.getValue().toUpperCase();
                String[] parts = result.split("@");
                String local = parts[0];
                result = local.replaceAll("\\(.*\\)", "")
                        .replaceAll("\\{.*\\}", "")
                        .replaceAll("\\+.*$", "")
                        .replace(".", "");
                item.setTransformed(result + "@" + parts[1]);
            }
        }
    }
}
