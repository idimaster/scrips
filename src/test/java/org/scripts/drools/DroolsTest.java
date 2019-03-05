package org.scripts.drools;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DroolsTest {

    static private String rule;

    @BeforeClass
    public static void init() throws Exception {
        URL url = Resources.getResource("transformation.drl");
        rule = Resources.toString(url, Charsets.UTF_8);
    }

    @Test
    public void evaluate() throws Exception {
        List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "test@gmail.com", ""));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", ""));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", ""));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", ""));
        EvaluationContext context = new EvaluationContext("test", "p1", items);
        Drools.evaluate(rule, context);
    }
}
