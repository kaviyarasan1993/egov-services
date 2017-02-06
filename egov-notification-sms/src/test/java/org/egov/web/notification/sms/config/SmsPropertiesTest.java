package org.egov.web.notification.sms.config;

import org.egov.web.notification.sms.models.Priority;
import org.egov.web.notification.sms.models.Sms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class SmsPropertiesTest {

    @Autowired
    private SmsProperties smsProperties;

    @Test
    public void test_should_create_property_class_with_values_injected() {
        assertEquals("http://sms/esms/sendsmsrequest", smsProperties.getSmsProviderURL());
        assertEquals(Arrays.asList("401", "404"), smsProperties.getSmsErrorCodes());
    }

    @Test
    public void test_should_return_sms_request_body() {
        final Sms sms = new Sms("mobileNumber", "testMessage", Priority.MEDIUM);
        final MultiValueMap<String, String> smsRequestBody =
                smsProperties.getSmsRequestBody(sms);

        assertEquals(8, smsRequestBody.keySet().size());
        assertEquals("user123", smsRequestBody.getFirst("username"));
        assertEquals("myPassword", smsRequestBody.getFirst("password"));
        assertEquals("mySender", smsRequestBody.getFirst("senderId"));
        assertEquals("mobileNumber", smsRequestBody.getFirst("mobile"));
        assertEquals("testMessage", smsRequestBody.getFirst("content"));
        assertEquals("medium", smsRequestBody.getFirst("priority"));
        assertEquals("value1", smsRequestBody.getFirst("param1"));
        assertEquals("value2", smsRequestBody.getFirst("param2"));
    }

}