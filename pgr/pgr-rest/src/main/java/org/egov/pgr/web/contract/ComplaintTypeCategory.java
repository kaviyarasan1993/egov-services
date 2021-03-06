package org.egov.pgr.web.contract;

import lombok.Getter;

@Getter
public class ComplaintTypeCategory {
    private Long id;
    private String name;
    private String description;

    public ComplaintTypeCategory(org.egov.pgr.persistence.entity.ComplaintTypeCategory entityComplaintTypeCategory) {
        this.id = entityComplaintTypeCategory.getId();
        this.name = entityComplaintTypeCategory.getName();
        this.description = entityComplaintTypeCategory.getDescription();
    }
}
