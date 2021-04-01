package com.lightspeed.webfluxlearning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * Introduction learning tests. Getting familiar with the webflux api, more specific: Flux
 *
 * So the entire idea behind reactive programming is actually non-blocking programming, based on
 * lazy loading. In other words, you can look at the reactive publishers as futures, they will
 * only execute when called upon.
 */
public class LearningFlux {

  /**
   * Same basic api, you can log to see all the internal calls happening, every onNext method call,
   * the onCompleted call, etc etc.
   */
  @Test
  public void SimpleFlux() {
    Flux.just(1, 2, 3, 4, 5, 6)
        .log()
        .subscribe();
  }

  /**
   * Just like with a mono (but not demonstrated), you can do something when the flux completes. This
   * is a runnable however, and not a consumer. This makes sense because consumers are supposed to
   * be working with the data passed through the flux.
   */
  @Test
  public void SimpleFluxWithOnComplete() {
    Flux.just(1,2, 3,4 ,5 ,6, 7,8)
        .log()
        .subscribe(item -> System.out.println(item/2),
            null,
            () -> System.out.println("done"));
  }

  /**
   * Another interesting concept is the backpressure concept. So with mono and flux you can subscribe
   * a process. This process might or might not be able to deal with all the incoming data. So when
   * subscribing, you can specify the rate at with the data comes through. WHen subscribing, you can
   * pass some sort of subscription.
   *
   * This is the basic form of backpressure: The flux is a stream of 8 numbers, but we only request
   * 2. The onComplete runnable isn't even run, because the flux didn't complete.
   */
  @Test
  public void fluxWithBackPressure() {
    Flux.just(1,2, 3,4 ,5 ,6, 7,8)
        .log()
        .subscribe(item -> System.out.println(item/2),
            null,
            () -> System.out.println("This will never complete"),
            context -> context.request(2));
  }

  /**
   * Same basic backpressure, however, the flux will finish this time, taking 2 elements at a time.
   */
  @Test
  public void fluxWithConstantBackpressureUntilFinish() {
    Flux.just(1,2, 3,4 ,5 ,6, 7,8)
        .log()
        .limitRate(2)
        .subscribe(item -> System.out.println(item/2),
            null,
            () -> System.out.println("Done, we saw the end"));
  }
}
