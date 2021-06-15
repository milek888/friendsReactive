package org.milosz.friendsreactive;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/*
* https://www.baeldung.com/reactor-core
*   .subscribe(numbers::add)
*
* */
public class FluxMonoTests {

    @Test
    public void test() {
        Flux.just("milosz", "mazurek", "kieubasa", "sznurek")
                .map(String::toUpperCase)
                .concatWith(Flux.error(new RuntimeException("Exception Milosza")))
                .concatWithValues("jeden", "dwa")
                .subscribe(System.out::println, e -> System.out.println("this is an exception"), () -> System.out.println("Completed"));

 /*       Flux.just("milosz", "mazurek", "kieubasa", "sznurek")
                .map(String::toUpperCase)
                .concatWith(Flux.error(new RuntimeException("Exception Milosza")))
                .concatWithValues("jeden", "dwa")
                .log("szedl Grzes przez wies")
                .subscribe(System.out::println, e -> System.out.println("this is an exception"));*/

        List<Integer> numbers = new ArrayList<>();
        Flux.just(11, 12,13,14).subscribe(numbers::add);
        numbers.forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<String> words = new ArrayList<>();

        Flux.just("milosz", "mazurek", "kieubasa", "sznurek")
                .subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("111111111111111111 onSubscribe(Subscription s) ");
                s.request(10);
            }

            @Override
            public void onNext(String word) {
                System.out.println("222222222222222222222222 onNext(String word)");
                words.add(word);
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onComplete() {
                System.out.println("4444444444444444444 onComplete()");
            }
        });
    }


}
