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

import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;
import io.netty.handler.codec.http.HttpHeaders;
import ratpack.http.client.SentRequest;

import java.util.Collection;
import java.util.Collections;

class RatpackClientRequestAdapter implements ClientRequestAdapter {
  private final SentRequest sentRequest;
  private final SpanNameProvider spanNameProvider;

  RatpackClientRequestAdapter(final SentRequest sentRequest,
                              final SpanNameProvider spanNameProvider) {
    this.sentRequest = sentRequest;
    this.spanNameProvider = spanNameProvider;
  }

  @Override
  public String getSpanName() {
    return spanNameProvider.spanName(new RatpackHttpClientRequest(sentRequest));
  }

  @Override
  public void addSpanIdToRequest(@Nullable final SpanId spanId) {
    HttpHeaders headers = sentRequest.requestHeaders();
    if (spanId == null) {

      headers.add(BraveHttpHeaders.Sampled.getName(), "0");
    } else {
      headers.add(BraveHttpHeaders.Sampled.getName(), "1");
      headers.add(BraveHttpHeaders.TraceId.getName(),
          IdConversion.convertToString(spanId.traceId));
      headers.add(BraveHttpHeaders.SpanId.getName(),
          IdConversion.convertToString(spanId.spanId));
      if (spanId.nullableParentId() != null) {
        headers.add(BraveHttpHeaders.ParentSpanId.getName(),
            IdConversion.convertToString(spanId.parentId));
      }
    }
  }

  @Override
  public Collection<KeyValueAnnotation> requestAnnotations() {
    return Collections.singletonList(KeyValueAnnotation.create("http.uri", sentRequest.uri()));
  }

  @Override
  public Endpoint serverAddress() {
    return null;
  }
}
