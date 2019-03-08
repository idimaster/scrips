package org.scripts.js;

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

public class JSTransformTest {
    static private String script;
    static private String script2;

    @BeforeClass
    public static void init() throws Exception {
        URL url = Resources.getResource("transform.js");
        script = Resources.toString(url, Charsets.UTF_8);
        url = Resources.getResource("transform_l.js");
        script2 = Resources.toString(url, Charsets.UTF_8);
    }

    @Test
    public void evaluate() throws Exception {
        JSTransform transform = new JSTransform(script);

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

    @Test
    public void two_instances() throws Exception {
        JSTransform transform = new JSTransform(script);

        List<EvaluationItem> items = new ArrayList<>();
        items.add(new EvaluationItem("email", "tes{t}@gmail.com", "", false));
        items.add(new EvaluationItem("email", "tes.t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te(s).t@gmail.com", "", false));
        items.add(new EvaluationItem("email", "te+s.t@gmail.com", "", false));

        EvaluationContext context = new EvaluationContext("test", "p1", items);
        EvaluationContext result = transform.evaluate(context);

        assertEquals("TES@GMAIL.COM", result.getItems().get(0).getTransformed());
        assertEquals("TEST@GMAIL.COM", result.getItems().get(1).getTransformed());
        assertEquals("TET@GMAIL.COM", result.getItems().get(2).getTransformed());
        assertEquals("TE@GMAIL.COM", result.getItems().get(3).getTransformed());

        JSTransform transform_l = new JSTransform(script2);

        result = transform_l.evaluate(context);

        assertEquals("tes@gmail.com", result.getItems().get(0).getTransformed());
        assertEquals("test@gmail.com", result.getItems().get(1).getTransformed());
        assertEquals("tet@gmail.com", result.getItems().get(2).getTransformed());
        assertEquals("te@gmail.com", result.getItems().get(3).getTransformed());

        result = transform.evaluate(context);

        assertEquals("TES@GMAIL.COM", result.getItems().get(0).getTransformed());
        assertEquals("TEST@GMAIL.COM", result.getItems().get(1).getTransformed());
        assertEquals("TET@GMAIL.COM", result.getItems().get(2).getTransformed());
        assertEquals("TE@GMAIL.COM", result.getItems().get(3).getTransformed());
    }
}
