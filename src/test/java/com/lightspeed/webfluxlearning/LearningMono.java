package com.lightspeed.webfluxlearning;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * Introduction learning tests. Getting familiar with the webflux api, more specific: Mono
 *
 * So the entire idea behind reactive programming is actuallly non-blocking programming, based on
 * lazy loading. In other words, you can look at the reactive publishers as futures, they will
 * only execute when called upon.
 */
public class LearningMono {

  @Test
  public void basicMonoTest() {
    /**
     * A mono is a reactive publisher / a deferred emitter (deferred meaning that it does not emit
     * instantly). It emits at most 1 item at the time instead of 'a collection'
     * of items. You could see a collection as a single item as well, it depends on how you look
     * at it.
     */
    Mono<String> stringMono = Mono.just("something");
    // -- the item will only be returned when you perform an operation that requires the item, in
    // -- this case we use the block operation

    String item = stringMono.block();
    System.out.println(item);
  }

  @Test
  public void basicMonoTrasformation() {
    Mono<String> s = Mono.just("hello world");

    //Here we ask the producer of the String value to transform the original value to an integer
    Mono<Integer> i = s.map(String::length);

    //Beware that the transformation is only being executed when the block operation is invoked.
    System.out.println("Length of the string: " + i.block());

    //This is basically the same for java streams.
    List<String> words = Arrays.asList("Hello", "world", "this", "is", "a", "test");

    Stream<String> sortedWordStream = words.stream().sorted();
    IntStream lengthStream = sortedWordStream.mapToInt(String::length);

    System.out.println("sum of all these = " + lengthStream.sum());
  }

  /**
   * No idea yet what exactly the 'publish' method is doing.
   */
  @Test
  public void monoPublish() {
    Mono<String> s = Mono.just("something");

    Mono<Integer> i = s.publish(mono -> mono.log().map(String::length));

    System.out.println(i.block());
  }

  /**
   * The subscribe method 'subscribes' a listener or an actor function if you want to the mono.
   * The doOnSuccess call is done before the onNext call handling. So basically, this seems to be
   * a check before running any computations
   */
  @Test
  public void monoSubscribe() {
    Mono.just("something")
        .log()
        .doOnSuccess(System.out::println)
        .subscribe(item -> System.out.println("lenght: " + item.length()));
  }

  /**
   * The doOnError call is only triggered if the consumer handling had an error. So in this case,
   * nothing will happen
   */
  @Test
  public void monoSubscribeWithOnError() {
    Mono.just("somehting")
        .doOnError(System.out::println)
        .subscribe(s -> System.out.println("length: " + s.length()));
  }

  @Test
  public void monoSubscribeDoOnErrorWithError() {
    Mono.just(9)
        .doOnError(item -> System.out.println("couldn't divide by 0 " + item))
        .subscribe(s -> System.out.println(s / 0));
  }

  /**
   * The subscribe method of a mono can also contain functionality to respond to errors. In this
   * case, i want to divide by zero, it goes wrong, the exception is passed to the exception
   * consumer which just prints out the exception message.
   */
  @Test
  public void subscribeWithMoreOptions() {
    Mono.just(9)
        .subscribe(s -> System.out.println("division: " + s/0),
            error -> System.out.println(error.getMessage()));
  }
}
