package org.egov.boundary.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.egov.boundary.persistence.entity.City;
import org.egov.boundary.persistence.repository.CityRepository;
import org.egov.boundary.web.contract.CityRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

	@Mock
	private CityRepository cityRepository;

	@InjectMocks
	private CityService cityService;

	@Test
	public void test_should_fetch_city_for_given_id() {
		final City cityReq = City.builder().id(1L).build();
		final CityRequest cityRequestForCityId = CityRequest.builder().city(cityReq).build();

		when(cityRepository.findOne(cityRequestForCityId.getCity().getId())).thenReturn(getExpectedCityDetails());

		City city = cityService.getCityByCityReq(cityRequestForCityId);

		assertEquals("0001", city.getCode());

	}

	@Test
	public void test_should_fetch_city_for_given_code() {

		final City cityReq = City.builder().code("0001").build();
		final CityRequest cityRequestForCityCode = CityRequest.builder().city(cityReq).build();
		when(cityRepository.findByCode(cityRequestForCityCode.getCity().getCode()))
				.thenReturn(getExpectedCityDetails());

		City city = cityService.getCityByCityReq(cityRequestForCityCode);

		assertEquals("0001", city.getCode());

	}

	private City getExpectedCityDetails() {
		final City city = City.builder().code("0001").name("Kurnool Municipal Corporation").domainURL("kurnool")
				.districtName("Kurnool").districtCode("KMC").localName("Kurnool").build();
		return city;
	}
}