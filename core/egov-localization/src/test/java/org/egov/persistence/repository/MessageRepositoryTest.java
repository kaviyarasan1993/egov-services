package org.egov.persistence.repository;


import org.egov.persistence.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @Sql(scripts = {"/sql/clearMessages.sql", "/sql/createMessages.sql"})
    public void shouldFetchMessagesForGivenTenantAndLocale() {
        final List<Message> actualMessages = messageRepository
                .findByTenantIdAndLocale("tenant1", "en_US");
        assertEquals(2, actualMessages.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearMessages.sql", "/sql/createMessages.sql"})
    public void shouldSaveMessages() {
        final String locale = "newLocale";
        final String tenant = "newTenant";
        final Message message1 = Message.builder()
                .tenantId(tenant)
                .code("code1")
                .locale(locale)
                .message("New message1")
                .build();
        final Message message2 = Message.builder()
                .tenantId(tenant)
                .code("code2")
                .locale(locale)
                .message("New message2")
                .build();
        final List<Message> actualMessages = messageRepository.save(Arrays.asList(message1, message2));
        assertTrue("Id generated for message1", message1.getId() > 0);
        assertTrue("Id generated for message2", message2.getId() > 0);
        assertEquals(2, messageRepository.findByTenantIdAndLocale(tenant, locale).size());
    }
}