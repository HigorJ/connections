package com.pingr.conections.Connections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final producerService producerService;

    @Autowired
    public AccountService(AccountRepository accountRepository, producerService producerService) {
        this.accountRepository = accountRepository;
        this.producerService = producerService;
    }

    public void storeAccount(Account account) {
        this.accountRepository.save(account);
        System.out.println("salvei a conta:");
        System.out.println(account);
    }

    public void updateAccount(Account account) {
        Optional<Account> optionalAccount = this.accountRepository.findById(account.getId());

        if(!optionalAccount.isPresent()) throw new Error("Conta não existe!");

        account.setFriends(optionalAccount.get().getFriends());

        this.accountRepository.save(account);

        System.out.println("Atualizei a conta: ");
        System.out.println(account);
    }

    public void deleteAccount(Account account) {
        Optional<Account> optionalAccount = this.accountRepository.findById(account.getId());

        if(!optionalAccount.isPresent()) throw new Error("Conta não existe!");

        Account userAccount = optionalAccount.get();
        Set<Account> userFriends = userAccount.getFriends();
        userAccount.setFriends(new HashSet<>());

        this.accountRepository.save(userAccount);

        for(Account userFriend: userFriends) {
            Optional<Account> friendAccountOptional = this.accountRepository.findById(userFriend.getId());

            if(friendAccountOptional.isPresent()) {
                Account friendAccount = friendAccountOptional.get();
                Set<Account> bFriends = friendAccount.getFriends();

                bFriends = bFriends
                        .stream()
                        .filter(f -> f.getId() != userAccount.getId())
                        .collect(Collectors.toSet());

                friendAccount.setFriends(bFriends);

                this.accountRepository.save(friendAccount);
            }
        }

        this.accountRepository.deleteById(account.getId());

        System.out.println("Deletei a conta: ");
        System.out.println(account);
    }

    public Set<Account> getAllFriends(Long userId) {
        Optional<Account> optionalAccount = this.accountRepository.findById(userId);

        if(!optionalAccount.isPresent()) throw new Error("Conta não existe!");

        return optionalAccount.get().getFriends();
    }

    public int countAllFriends(Long userId) {
        Optional<Account> optionalAccount = this.accountRepository.findById(userId);

        if(!optionalAccount.isPresent()) throw new Error("Conta não existe!");

        return optionalAccount.get().getFriends().size();
    }

    public boolean stablishFriendshipBetween(Long aId, Long bId) {
        Optional<Account> aOptional = this.accountRepository.findById(aId);
        Optional<Account> bOptional = this.accountRepository.findById(bId);

        if (!aOptional.isPresent() || !bOptional.isPresent()) return false;

        Account a = aOptional.get();
        Account b = bOptional.get();

        Set<Account> aFriends = a.getFriends();
        aFriends.add(b);
        a.setFriends(aFriends);

        Set<Account> bFriends = b.getFriends();
        bFriends.add(a);
        b.setFriends(bFriends);

        this.accountRepository.saveAll(List.of(a, b));
        this.producerService.newFollowing(a, b);

        return true;
    }

    public boolean unmadeFriendshipBetweeb(Long aId, Long bId) {
        Optional<Account> aOptional = this.accountRepository.findById(aId);
        Optional<Account> bOptional = this.accountRepository.findById(bId);

        if (!aOptional.isPresent() || !bOptional.isPresent()) return false;

        Account a = aOptional.get();
        Account b = bOptional.get();

        Set<Account> aFriends = a.getFriends();
        aFriends = aFriends.stream().filter(friend -> friend.getId() != b.getId()).collect(Collectors.toSet());
        a.setFriends(aFriends);

        Set<Account> bFriends = b.getFriends();
        bFriends = bFriends.stream().filter(friend -> friend.getId() != a.getId()).collect(Collectors.toSet());
        b.setFriends(bFriends);

        this.accountRepository.saveAll(List.of(a, b));
        this.producerService.friendshipUnmade(a, b);

        return true;
    }
}
