package com.lightspeed.webfluxlearning;

import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class LearningTests {

  @Test
  public void basicMonoTest() {
    Mono<String> stringMono = Mono.just("something");

    stringMono.publish(System.out::println).block();
  }

}
