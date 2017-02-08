package org.egov.pgr.model;

import org.egov.pgr.contracts.grievance.ServiceRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

import static org.egov.pgr.contracts.grievance.ServiceRequest.*;

@Component
public class ComplaintBuilder {

    private final ComplaintTypeService complaintTypeService;
    private final ServiceRequest serviceRequest;
    private final ComplaintStatusService complaintStatusService;
    private final EscalationService escalationService;
    private final Complaint complaint;
    private final PositionRepository positionRepository;

    @Autowired
    public ComplaintBuilder(ServiceRequest serviceRequest, ComplaintTypeService complaintTypeService, ComplaintStatusService complaintStatusService, EscalationService escalationService, PositionRepository positionRepository) {
        this.serviceRequest = serviceRequest;
        this.complaintStatusService = complaintStatusService;
        this.escalationService = escalationService;
        this.positionRepository = positionRepository;
        this.complaint = new Complaint();
        this.complaintTypeService = complaintTypeService;
    }

    public Complaint build() {
        setBasicInfo();
        setComplainant();
        setReceivingMode();
        setComplaintType();
        setComplaintStatus();
        setAssigneeId();
        setLocationDetails();
        setEscalationDate();
        return this.complaint;
    }

    private void setBasicInfo() {
        this.complaint.setCrn(this.serviceRequest.getCrn());
        this.complaint.setLat(this.serviceRequest.getLat());
        this.complaint.setLng(this.serviceRequest.getLng());
        this.complaint.setDetails(this.serviceRequest.getDetails());
        this.complaint.setLandmarkDetails(this.serviceRequest.getLandmarkDetails());
    }

    private void setComplainant() {
        String userId;
        if ((userId = this.serviceRequest.getValues().get(USER_ID)) != null) {
            this.complaint.getComplainant().setUserDetail(Long.valueOf(userId));
        } else {
            this.complaint.getComplainant().setName(this.serviceRequest.getFirstName());
            this.complaint.getComplainant().setMobile(this.serviceRequest.getPhone());
            this.complaint.getComplainant().setEmail(this.serviceRequest.getEmail());
        }
    }

    private void setEscalationDate() {
        //TODO - pass in tenant id correctly
        this.complaint.setEscalationDate(new Date());
        Long designationId = positionRepository.designationIdForAssignee("", this.complaint.getAssignee());
        this.complaint.setEscalationDate(escalationService.getExpiryDate(this.complaint, designationId));
    }

    private void setLocationDetails() {
        String locationId = this.serviceRequest.getValues().get(LOCATION_ID);
        String childLocationId = this.serviceRequest.getValues().get(CHILD_LOCATION_ID);
        String locationName = this.serviceRequest.getValues().get(LOCATION_NAME);
        if (Objects.nonNull(locationId)) this.complaint.setLocation(Long.parseLong(locationId));
        if (Objects.nonNull(childLocationId)) this.complaint.setChildLocation(Long.parseLong(childLocationId));
        if (Objects.nonNull(locationName)) this.complaint.setLocationName(locationName);
    }

    private void setAssigneeId() {
        String assigneeId = this.serviceRequest.getValues().get(ASSIGNEE_ID);
        if (Objects.nonNull(assigneeId)) this.complaint.setAssignee(Long.parseLong(assigneeId));
    }

    private void setComplaintStatus() {
        String statusName = org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED.name();
        ComplaintStatus complainStatus = complaintStatusService.getByName(statusName);
        this.complaint.setStatus(complainStatus);
    }

    private void setReceivingMode() {
        //TODO - Set this correctly
        complaint.setReceivingMode(ReceivingMode.MOBILE);
    }

    private void setComplaintType() {
        if (Objects.isNull(serviceRequest.getComplaintTypeCode())) return;
        //TODO - Satyam what will come from UI? Code or Id?
        ComplaintType complaintType = this.complaintTypeService.findByCode(serviceRequest.getComplaintTypeCode());
        complaint.setComplaintType(complaintType);
    }


}