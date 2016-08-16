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
package ratpack.zipkin.internal;

import com.github.kristofa.brave.ClientResponseInterceptor;
import ratpack.http.client.HttpClientResponseInterceptor;
import ratpack.http.client.ReceivedResponse;

import javax.inject.Inject;

public class ZipkinClientResponseInterceptor implements HttpClientResponseInterceptor {
  private final ClientResponseAdapterFactory responseAdapterFactory;
  private final ClientResponseInterceptor responseInterceptor;

  @Inject
  public ZipkinClientResponseInterceptor(final ClientResponseAdapterFactory
                                             responseAdapterFactory, final ClientResponseInterceptor responseInterceptor) {
    this.responseAdapterFactory = responseAdapterFactory;
    this.responseInterceptor = responseInterceptor;
  }

  @Override
  public void intercept(final ReceivedResponse receivedResponse) {
    responseInterceptor.handle(responseAdapterFactory.createAdapter(receivedResponse));
  }
}
