package org.milosz.friendsreactive;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

/*
* https://www.baeldung.com/reactor-core
*   .subscribe(numbers::add)
*   .subscribe(new Subscriber<String>() {
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

    @Test
    public void test3() {
        Flux.just("milosz", "mazurek", "kieubasa", "sznurek")
                .log()
                .subscribeOn(parallel())
                .subscribe(System.out::println);
    }


    @Test
    public void testFluxWithRangeAndSheduller() {//TODO jesli nie zrobimy Thread.sleep() to nic nie wypisze bo  z powodu .subscribeOn(parallel())
        //TODO strumien bedzie wyonywany w innym watku niz cala metoda. Watek metody (main) sie konczy i flux nie zdazy wypisac
        Flux<Long> rangeFlux = Flux.range(1, 10)
                .log()
                .map(Long::new)
       /*         .interval(Duration.ofMillis(200))*/
                .subscribeOn(parallel());
        rangeFlux.subscribe(System.out::println, System.out::println, () -> System.out.println("completed flow"), sub -> sub.request(5));
    }

    @Test
    public void testFluxWithRangeAndSheduller2() throws InterruptedException{//TODO wersja ze sleep - tu wypisze wszystko oprawnie
        Flux<Long> rangeFlux = Flux.range(1, 10)
                .map(Long::new)
                .interval(Duration.ofMillis(200))
                .subscribeOn(parallel())//TODO praca zastowowac to i dac sleepa
                .log();
      /*  rangeFlux.subscribe(System.out::println, System.out::println, () -> System.out.println("completed flow"), sub -> sub.request(5));*/
        rangeFlux.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void testFluxWithRange() {//TODO tu nie potrzeba sleepa bo wszystko idzie po klei w jednym watku
        Flux<Integer> rangeFlux = Flux.range(1, 5);
        rangeFlux.subscribe(System.out::println, System.out::println, () -> System.out.println("completed flow"), sub -> sub.request(3));
    }

}
