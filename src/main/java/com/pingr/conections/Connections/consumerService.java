package com.pingr.conections.Connections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component  // prepara para injeção de dependência do @Autowired
public class consumerService {
    private final AccountService service;

    @Autowired
    public consumerService(AccountService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = "${topic.accounts.account-created}",
            groupId = "connection_new_account"
    )
    public void newAccountConsumer(Account account) throws IOException {
        this.service.storeAccount(account);
    }

    @KafkaListener(
            topics = "${topic.accounts.account-updated}",
            groupId = "connection_updated_account"
    )
    public void updatedAccountConsumer(Account account) throws IOException {
        this.service.updateAccount(account);
    }

    @KafkaListener(
            topics = "${topic.accounts.account-deleted}",
            groupId = "connection_deleted_account"
    )
    public void deletedAccountConsumer(Account account) throws IOException {
        this.service.deleteAccount(account);
    }
}
