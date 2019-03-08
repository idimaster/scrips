package org.scripts.mvel;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scripts.drools.EvaluationContext;
import org.scripts.drools.EvaluationItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransformTest {
    static private String script;

    @BeforeClass
    public static void init() throws Exception {
        URL url = Resources.getResource("transform.mvl");
        script = Resources.toString(url, Charsets.UTF_8);
    }

    @Test
    public void evaluate() {
        Transform transform = new Transform(script);

        List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "tes{t}@gmail.com", "", false));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", "", false));

        EvaluationContext context = new EvaluationContext("test", "p1", items);
        EvaluationContext result = transform.evaluate(context);

        assertEquals("TES@GMAIL.COM", result.getItems().get(0).getTransformed());
        assertTrue(result.getItems().get(0).getValid());
        assertEquals("TEST@GMAIL.COM", result.getItems().get(1).getTransformed());
        assertTrue(result.getItems().get(1).getValid());
        assertEquals("TET@GMAIL.COM", result.getItems().get(2).getTransformed());
        assertTrue(result.getItems().get(2).getValid());
        assertEquals("TE@GMAIL.COM", result.getItems().get(3).getTransformed());
        assertTrue(result.getItems().get(3).getValid());
    }
}
