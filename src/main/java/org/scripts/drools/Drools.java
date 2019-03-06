package org.scripts.drools;

import org.drools.core.ClassObjectFilter;
import org.drools.core.impl.StatelessKnowledgeSessionImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

import java.util.ArrayList;
import java.util.List;

public class Drools {

    private KieServices kieServices;
    private KieFileSystem kieFileSystem;
    private KieContainer kieContainer;

    public void init(String drl) {
        kieServices = KieServices.Factory.get();
        kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rule.drl", drl);
        kieServices.newKieBuilder(kieFileSystem).buildAll();
        kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }

    public EvaluationContext evaluate(EvaluationContext context) throws Exception {

        StatelessKieSession statelessKieSession = kieContainer.getKieBase().newStatelessKieSession();

        List<Command> cmds = new ArrayList<>();
        cmds.add(CommandFactory.newInsert(context, "context"));
        cmds.add(CommandFactory.newInsertElements(context.getItems()));
        cmds.add(CommandFactory.newFireAllRules());
        cmds.add(CommandFactory.newGetObjects(new ClassObjectFilter(EvaluationContext.class)));
        ExecutionResults results = statelessKieSession.execute(CommandFactory.newBatchExecution(cmds));
        return (EvaluationContext)results.getValue("context");
    }
}
