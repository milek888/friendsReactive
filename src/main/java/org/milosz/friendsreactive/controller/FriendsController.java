package org.milosz.friendsreactive.controller;

import lombok.AllArgsConstructor;
import org.milosz.friendsreactive.model.Course;
import org.milosz.friendsreactive.model.Friend;
import org.milosz.friendsreactive.repository.FriendsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/friends")
//https://piotrminkowski.com/2018/05/04/reactive-microservices-with-spring-webflux-and-spring-cloud/
//https://ordina-jworks.github.io/rest/2020/10/12/RestTemplate-vs-WebClient.html
//https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/

public class FriendsController {

    private final FriendsRepository friendsRepository;
    private final WebClient webClient = WebClient.create("http://localhost:8083");//nie wykorzystuje Eureki
    private final WebClient.Builder webClientBuilder;//ten jest wstrzykiwany i wykorzystuje Eureke

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<Friend> getFriends() {
        return friendsRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<Friend>> getFriend(@PathVariable String id) {
        return friendsRepository.findById(id)
                .map(friend -> new ResponseEntity<>(friend, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    Mono<ResponseEntity<Friend>> saveFriend(@RequestBody Friend friend) {
        return friendsRepository.save(friend)
                .map(friend1 -> new ResponseEntity<>(friend1, HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    Mono<Void> deleteFriend(@PathVariable String id) {
        Mono<Void> result = friendsRepository.deleteById(id);
        return result;
    }

    //---------requests to courses by WebClient

    @GetMapping(value = "/courses", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<Course> getCourses() {
/*        Flux.just("milosz", "mazurek", "kieubasa", "sznurek")
                .map(String::toUpperCase)
                .subscribe(System.out::println);

        Iterable<Integer> result = Flux.just(11,22,33,44,55,66).toIterable(3);

        result.forEach(System.out::println);*/


        return webClient.get()
                .uri("/courses")
                .retrieve()
                .bodyToFlux(Course.class).log();
    }

    @GetMapping(value = "/courses/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Mono<ResponseEntity<Course>> getCoursesById(@PathVariable String id) {
        Mono<ResponseEntity<Course>> result = webClient.get()
                .uri("/courses/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Course.class))
                .map(course -> new ResponseEntity<>(course, HttpStatus.OK));
        return result;
    }



    //-------------requests to courses by WebClient z wykorzystaniem Eureki

    @GetMapping(value = "/courses2", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<Course> getCourses2() {
        return webClientBuilder.build().get()
                .uri("http://courses/courses")
                .retrieve()
                .bodyToFlux(Course.class);
    }

    @GetMapping(value = "/courses2/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Mono<Course> getCoursesById2(@PathVariable String id) {
        Mono<Course> courseMono = webClientBuilder.build().get()
                .uri("http://courses/courses/{id}", id)
                .retrieve()
                .bodyToMono(Course.class);
        return courseMono;
    }


//-------------------------------------------------------tests-------------------------------------

//Uzasadnienie dlaczego Flux<String> tak sie dziwnie zachowuje
//https://stackoverflow.com/questions/54856858/why-is-flux-fromiterable-return-in-restcontroller-coming-back-as-one-concatena

    @GetMapping(value = "/getStrings", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<String> getStrings() {

        return Flux.just("1", "2", "3")
                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }
}
