package org.milosz.friendsreactive.repository;

import org.junit.jupiter.api.Test;
import org.milosz.friendsreactive.model.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
class FriendsRepositoryTest {

    @Autowired
    FriendsRepository friendsRepository;

    @Test
    void getAllTest() {
       Flux<Friend> friends = friendsRepository.findAll();
        StepVerifier.create(friends)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getFriendTest() {
        Mono<Friend> friend = friendsRepository.findById("6055ecf32f8430a8ae221fff");
        StepVerifier.create(friend)
                .expectSubscription()
                .expectNextMatches(friend1 -> friend1.getName().equals("Agata"))
                .verifyComplete();
    }

    @Test
    void saveFriendTest() {
        Friend friend = Friend.builder()
                .name("Maciek")
                .secondName("Swiezy")
                .age(40)
                .build();

        Mono<Friend> savedFriend = friendsRepository.save(friend);

        StepVerifier.create(savedFriend)
                .expectSubscription()
                .expectNextMatches(friend1 -> friend1.getName().equals("Maciek"))
                .verifyComplete();
    }

}