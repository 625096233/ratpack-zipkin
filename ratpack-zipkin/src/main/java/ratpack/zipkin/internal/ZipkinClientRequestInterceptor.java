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

import com.github.kristofa.brave.ClientRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.http.client.HttpClientRequestInterceptor;
import ratpack.http.client.SentRequest;

import javax.inject.Inject;

public class ZipkinClientRequestInterceptor implements HttpClientRequestInterceptor {
  private final ClientRequestAdapterFactory requestAdapterFactory;
  private final ClientRequestInterceptor requestInterceptor;
  private static final Logger logger = LoggerFactory.getLogger(ZipkinClientRequestInterceptor.class);

  @Inject
  public ZipkinClientRequestInterceptor(final ClientRequestAdapterFactory requestAdapterFactory,
                                        final ClientRequestInterceptor requestInterceptor) {
    this.requestAdapterFactory = requestAdapterFactory;
    this.requestInterceptor = requestInterceptor;
  }

  @Override
  public void intercept(final SentRequest request) {
    requestInterceptor.handle(requestAdapterFactory.createAdaptor(request));
  }
}
