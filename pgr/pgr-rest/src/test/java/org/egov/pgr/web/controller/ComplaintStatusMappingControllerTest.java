package org.egov.pgr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.domain.service.ComplaintStatusMappingService;
import org.egov.pgr.persistence.entity.ComplaintStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplaintStatusMappingController.class)
public class ComplaintStatusMappingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ComplaintStatusMappingService mockComplaintStatusMappingService;

	@Test
	public void testComplaintStatusMappingService() throws Exception {
		String tenantId = "ap.public";
		Long userId = 18L;
		String status = "REGISTERED";
		List<ComplaintStatus> complaintStatuses = new ArrayList<>(
				Collections.singletonList(new ComplaintStatus().builder().id(1L).name("REGISTERED").build()));
		when(mockComplaintStatusMappingService.getStatusByRoleAndCurrentStatus(userId, status, tenantId))
				.thenReturn(complaintStatuses);

		mockMvc.perform(
				post("/_getnextstatuses?tenantId=" + tenantId + "&userId=" + userId + "&currentStatus=" + status))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("complaintStatusMappingResponse.json")));
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}