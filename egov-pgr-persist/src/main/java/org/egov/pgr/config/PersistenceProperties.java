package org.egov.pgr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PersistenceProperties {

    @Autowired
    private Environment environment;

    @Value("${services.workflow.hostname}")
    private String workflowServiceHostname;

    @Value("${services.position.hostname}")
    private String positionServiceHostname;

    @Value("${service.workflow.create.context}")
    private String createWorkflowContext;

    @Value("${service.position.fetch_assignee.context}")
    private String fetchAssigneeContext;

    private String getProperty(String propKey) {
        return this.environment.getProperty(propKey, "");
    }

    public String createWorkflowEndpoint() {
        return workflowServiceHostname + createWorkflowContext;
    }

    public String getPositionServiceEndpoint() {
        return positionServiceHostname + fetchAssigneeContext;
    }
}