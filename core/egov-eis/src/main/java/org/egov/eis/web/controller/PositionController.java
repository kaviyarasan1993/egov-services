package org.egov.eis.web.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.persistence.entity.Position;
import org.egov.eis.web.contract.Error;
import org.egov.eis.web.contract.ErrorRes;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.web.contract.PositionRes;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.domain.service.PositionService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {

	@Autowired
	private PositionService positionService;

	@GetMapping(value = "/employee/{code}/positions")
	public PositionRes getPositions(@PathVariable("code") String code,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "departmentCode") String departmentCode,
			@RequestParam(value = "designationCode") String designationCode,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-fromDate, -primary]") List<String> sort,
			@RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
			@RequestParam(value = "isPrimary") Boolean isPrimary) throws Exception {

		PositionRes response = new PositionRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty() && asOnDate != null) {
			response.getPosition()
					.addAll(positionService.getAllPositionsByEmpCode(code, asOnDate.toDateTimeAtStartOfDay().toDate()));
		} else {
			throw new Exception();
		}

		return response;
	}

	@GetMapping(value = "/position")
	public PositionRes getPosition(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "id") String id) throws Exception {

		PositionRes response = new PositionRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (id != null && !id.isEmpty()) {
			response.getPosition().add(positionService.getById(Long.valueOf(id)));
		} else {
			throw new Exception();
		}

		return response;
	}
	
	@GetMapping(value="positions")
	public ResponseEntity<?> search(@ModelAttribute PositionRequest positionRequest) {

		PositionRes response = new PositionRes();
		List<Position> positions = positionService.getPositions(positionRequest);
		response.getPosition().addAll(positions);
		ResponseInfo responseInfo = new ResponseInfo("","",new Date().toString(),"","","");
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		response.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionRes>(response, HttpStatus.OK);
	}




}