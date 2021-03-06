package org.egov.pgr.contracts.grievance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SevaRequest {

    public static final String REQUESTER_ID = "requester_id";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SERVICE_REQUEST = "ServiceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
    }

    @SuppressWarnings("unchecked")
    public ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }

    @SuppressWarnings("unchecked")
    public Long getRequesterId() {
        try {
            HashMap<String, Object> requestInfoMap = (HashMap<String, Object>) sevaRequestMap.get(REQUEST_INFO);
            if (requestInfoMap != null) {
                return Long.parseLong(String.valueOf(requestInfoMap.get(REQUESTER_ID)));
            }
        } catch(NumberFormatException nfe) {
            logger.error("Requester Id is invalid", nfe.getMessage(), nfe);
        }
        return -1L;
    }

}