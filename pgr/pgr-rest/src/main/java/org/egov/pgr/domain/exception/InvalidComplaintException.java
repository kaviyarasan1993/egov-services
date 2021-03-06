package org.egov.pgr.domain.exception;

import lombok.Getter;
import org.egov.pgr.domain.model.Complaint;

@Getter
public class InvalidComplaintException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private Complaint complaint;

    public InvalidComplaintException(Complaint complaint) {
        this.complaint = complaint;
    }
}
