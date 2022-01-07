package com.pingr.conections.Connections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class producerService {
    @Value(value = "${topic.accounts.account-created}")
    private String topic;

    @Value(value = "${topic.connections.new-following}")
    private String topicNewFollowing;

    @Value(value = "${topic.connections.friendship-unmade}")
    private String topicFriendshipUnmade;

    @Autowired
    private KafkaTemplate<String, Object> template;

    public void sendMessage(Account account) {
        this.template.send(this.topic, account);
    }

    public void newFollowing(Account account1, Account account2) {
        this.template.send(this.topicNewFollowing, account1.getUsername() + " e " + account2.getUsername() + " se tornaram amigos!");
    }

    public void friendshipUnmade(Account account1, Account account2) {
        this.template.send(this.topicNewFollowing, account1.getUsername() + " e " + account2.getUsername() + " não são mais amigos!");
    }
}
