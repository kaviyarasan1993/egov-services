package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    private WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public SevaRequest enrichWorkflow(SevaRequest sevaRequest) {
        WorkflowRequest request = sevaRequest.getWorkFlowRequest();
        WorkflowResponse workflowResponse = null;
        if (WorkflowRequest.Action.isCreate(request.getAction())) {
            workflowResponse = workflowRepository.create(request);
        } else if (WorkflowRequest.Action.isEnd(request.getAction())) {
            workflowResponse = workflowRepository.close(request);
        }
        sevaRequest.update(workflowResponse);
        return sevaRequest;
    }

}
