/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.server;

import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.LocalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.client.HttpClient;
import ratpack.http.client.HttpClientRequestInterceptor;
import ratpack.http.client.HttpClientResponseInterceptor;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import java.net.URI;

public class HelloWorldHandler implements Handler {
  private final Logger logger = LoggerFactory.getLogger(HelloWorldHandler.class);
  @Inject
  private LocalTracer tracer;
  @Inject
  private ZipkinHttpClient client;

  @Override
  public void handle(final Context ctx) throws Exception {
    ctx.getExecution().add(KeyValueAnnotation.create("some-key", "some-value"));
    tracer.startNewSpan("HelloWorldHandler", "handle");
    client.get(new URI("http://localhost:" + (ctx.get(ServerConfig.class).getPort() + 1) + "/"))
          .map(ReceivedResponse::getStatusCode)
          .then(a -> {
            ctx.getResponse().send("Yo dawg.");
            tracer.finishSpan();
          });

  }
}
