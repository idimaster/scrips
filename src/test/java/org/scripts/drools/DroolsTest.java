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
        Drools drools = new Drools();
        drools.init(rule);

        List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "tes{t}@gmail.com", "", false));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", "", false));
        EvaluationContext context = new EvaluationContext("test", "p1", items);
        EvaluationContext result = drools.evaluate(context);
        assertEquals("TES@GMAIL.COM", result.getItems().get(0).getTransformed());
        assertEquals("TEST@GMAIL.COM", result.getItems().get(1).getTransformed());
        assertEquals("TET@GMAIL.COM", result.getItems().get(2).getTransformed());
        assertEquals("TE@GMAIL.COM", result.getItems().get(3).getTransformed());
    }
}
