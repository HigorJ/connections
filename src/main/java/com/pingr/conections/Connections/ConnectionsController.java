package com.pingr.conections.Connections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "/connections")
public class ConnectionsController {
    private final producerService producerService;
    private final AccountService accountService;

    @Autowired
    public ConnectionsController(producerService producerService, AccountService accountService) {
        this.producerService = producerService;
        this.accountService = accountService;
    }

    @PostMapping(path = "/test")
    public Account fakeCreate() {
        Account fake = new Account(12L, "fake username", new HashSet<>());
        this.producerService.sendMessage(fake);
        return fake;
    }

    @GetMapping(path = "/{userId}")
    public Set<Account> getAllFriends(@PathVariable("userId") Long userId) {
        return this.accountService.getAllFriends(userId);
    }

    @GetMapping(path = "/{userId}/count")
    public int countAllFriends(@PathVariable("userId") Long userId) {
        return this.accountService.countAllFriends(userId);
    }

    @PostMapping(path = "/{aId}/{bId}")
    public boolean addFriends(@PathVariable("aId") Long aId, @PathVariable("bId") Long bId) {
        return this.accountService.stablishFriendshipBetween(aId, bId);
    }

    @DeleteMapping(path = "/{aId}/{bId}")
    public boolean removeFriend(@PathVariable("aId") Long aId, @PathVariable("bId") Long bId) {
        return this.accountService.unmadeFriendshipBetweeb(aId, bId);
    }
}
