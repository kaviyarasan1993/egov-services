package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.egov.pgr.domain.exception.InvalidComplaintException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
public class Complaint {
	private static final String MANUAL_RECEIVING_MODE = "MANUAL";
	@NonNull
	private AuthenticatedUser authenticatedUser;
	@NonNull
	private ComplaintLocation complaintLocation;
	@NonNull
	private Complainant complainant;
	private String crn;
	private Map<String, String> additionalValues;
	@NonNull
	private ComplaintType complaintType;
	private String complaintStatus;
	private String address;
	private List<String> mediaUrls;
	private String tenantId;
	private String description;
	private Date createdDate;
	private Date lastModifiedDate;
	private Date escalationDate;
	private boolean closed;
	private boolean modifyComplaint;
	private String receivingMode;
	private String receivingCenter;
	private Long department;

	public boolean isComplainantAbsent() {
		if (authenticatedUser.isAnonymousUser() || authenticatedUser.isEmployee()) {
			return complainant.isAbsent();
		}
		return complainant.isUserIdAbsent();
	}

	public boolean isComplainantEmailAbsent() {
		return complainant.isEmailAbsent();
	}

	public boolean isComplainantPhoneAbsent() {
		return complainant.isMobileAbsent();
	}

	public boolean isComplainantFirstNameAbsent() {
		return complainant.isFirstNameAbsent();
	}

	public boolean isComplainantIdAbsent() {
		return complainant.isUserIdAbsent();
	}

	public boolean isLocationAbsent() {
		if (isModifyComplaint()) {
			return complaintLocation.isLocationIdAbsent();
		}
		return complaintLocation.isRawLocationAbsent();
	}

	public boolean isLocationIdAbsent() {
		return complaintLocation.isLocationIdAbsent();
	}

	public boolean isRawLocationAbsent() {
		return complaintLocation.isRawLocationAbsent();
	}

	public void validate() {
		if (isLocationAbsent() || isComplainantAbsent() || isTenantIdAbsent() || isComplaintTypeAbsent()
				|| isDescriptionAbsent() || isCrnAbsent() || isReceivingCenterAbsent() || isReceivingModeAbsent()) {
			throw new InvalidComplaintException(this);
		}
	}

	public boolean isTenantIdAbsent() {
		return isEmpty(tenantId);
	}

	public boolean isDescriptionAbsent() {
		return isEmpty(description);
	}

	public boolean isComplaintTypeAbsent() {
		return complaintType.isAbsent();
	}

	public boolean isCrnAbsent() {
		return isModifyComplaint() && isEmpty(crn);
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public boolean isReceivingModeAbsent() {
		return authenticatedUser.isEmployee() && isEmpty(receivingMode);
	}

	public boolean isReceivingCenterAbsent() {
		return authenticatedUser.isEmployee() && MANUAL_RECEIVING_MODE.equalsIgnoreCase(receivingMode)
				&& isEmpty(receivingCenter);
	}

	public void setAdditionalValues(Map<String, String> additionalValues) {
		this.additionalValues = additionalValues;
	}

}
