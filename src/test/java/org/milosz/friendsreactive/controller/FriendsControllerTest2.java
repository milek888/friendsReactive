package org.milosz.friendsreactive.controller;

import org.junit.jupiter.api.Test;
import org.milosz.friendsreactive.model.Friend;
import org.milosz.friendsreactive.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class FriendsControllerTest2 {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    FriendsRepository friendsRepository;

    @Test
    void getFriendsTest() {
    webTestClient.get()
                .uri("/friends")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(Friend.class)
                .hasSize(5);
    }

/*    @Test
    void getFriendTest() {
        webTestClient.get()
                .uri("/friends/6055ecf32f8430a8ae222001")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Friend.class)
                .value(Friend::getName, equalTo("Alojzy"));
    }*/

    @Test
    void getFriendTest2() {
        webTestClient.get()
                .uri("/friends/6055ecf32f8430a8ae222001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Alojzy");
    }

    @Test
    void getFriendNotFoundTest() {
        webTestClient.get()
                .uri("/friends/badId")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void saveFriendTest() {
        Friend friend = Friend.builder()
                .name("Olek")
                .secondName("Lewicki")
                .age(35)
                .build();

        webTestClient.post().uri("/friends")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(friend), Friend.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Olek");

    }

    @Test
    void deleteFriendTest() {
        webTestClient.delete().uri("/friends/6055ecf32f8430a8ae221fff")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

}