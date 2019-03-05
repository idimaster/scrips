package org.scripts.drools;

import org.drools.core.ClassObjectFilter;
import org.drools.core.impl.StatelessKnowledgeSessionImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;

public class Drools {
    public static EvaluationContext evaluate(String drl, EvaluationContext context) throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rule.drl", drl);
        kieServices.newKieBuilder(kieFileSystem).buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        StatelessKnowledgeSessionImpl statelessKieSession = (StatelessKnowledgeSessionImpl) kieContainer.getKieBase().newStatelessKieSession();

        //AlertDecision alertDecision = new AlertDecision();
        //statelessKieSession.getGlobals().set("alertDecision", alertDecision);
        List<EvaluationContext> result = statelessKieSession.executeWithResults(context.getItems(), new ClassObjectFilter(EvaluationContext.class));

        return result.size() == 1 ? result.get(0) : null;
    }
}
