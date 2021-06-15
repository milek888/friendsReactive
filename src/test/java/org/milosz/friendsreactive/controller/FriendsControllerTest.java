package org.milosz.friendsreactive.controller;

import org.junit.jupiter.api.Test;
import org.milosz.friendsreactive.model.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@WebFluxTest
class FriendsControllerTest {

    @Autowired
    WebTestClient webTestClient;

/*    @Test
    void getStringsTest(){
        Flux<String> result = webTestClient.get().uri("/friends/getStrings")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();*/

/*           result.subscribe(System.out::println);*//*

        StepVerifier.create(result)
                .expectSubscription()
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();
    }

    @Test
    public void flux_approach1(){

        Flux<Integer> intergerFlux = webTestClient.get().uri("/friends/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(intergerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }


    @Test
    public void flux_approach2(){

        webTestClient.get().uri("/friends/getStrings")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(String.class)
                .hasSize(3);
    }

    @Test
    public void flux_approach3(){

        List<String> expectedIntegerList = Arrays.asList("1","2","3");

        EntityExchangeResult<List<String>> entityExchangeResult = webTestClient
                .get().uri("/friends/getStrings")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .returnResult();

        assertEquals(expectedIntegerList,entityExchangeResult.getResponseBody());
    }*/


    @Test
    void getFriendsTest() {
        Flux<Friend> result = webTestClient.get()
                .uri("/friends")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Friend.class)
                .getResponseBody();

        /*   result.subscribe(System.out::println);*/

   /*     StepVerifier.create(result)
                .exp
                .verifyComplete();*/

    }

    @Test
    void testMap() {
        List<String> names = List.of("Milosz", "Kasia", "Darek");
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                /*.subscribe(System.out::println)*/;

        StepVerifier.create(namesFlux)
                .expectNext("MILOSZ", "KASIA", "DAREK")
                .verifyComplete();
    }

    @Test
    void testFlatMap() {
        Flux.just("A", "B", "C")
                .flatMap(this::getNames)
                .subscribe(System.out::println);
    }

    Flux<String> getNames(String letter) {
        switch (letter) {
            case "A":
                return Flux.just("Adam", "Anna", "Agata");
            case "B":
                return Flux.just("Bartek", "Barbara", "Beata");
            case "C":
                return Flux.just("Cezary", "Cecylia");
            default:
                return Flux.empty();
        }
    }

}