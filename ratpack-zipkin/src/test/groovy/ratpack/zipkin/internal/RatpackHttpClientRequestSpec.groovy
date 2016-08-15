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
package ratpack.zipkin.internal

import com.github.kristofa.brave.http.HttpClientRequest
import io.netty.handler.codec.http.HttpHeaders
import ratpack.http.MutableHeaders
import ratpack.http.client.SentRequest
import spock.lang.Specification

class RatpackHttpClientRequestSpec extends Specification {
    def SentRequest sentRequest = Stub(SentRequest)
    def HttpHeaders headers = Mock(HttpHeaders)
    def HttpClientRequest request

    def void setup() {
        request = new RatpackHttpClientRequest(sentRequest)
        sentRequest.requestHeaders() >> headers
    }

    def 'Should get uri from spec'() {
        given:
            def expected = new URI("some-uri")
            sentRequest.uri() >> expected
        expect:
            request.getUri() == expected
    }

    def 'Should get method from spec'() {
        given:
            def expected = "POST"
            sentRequest.method() >> expected
            request = new RatpackHttpClientRequest(sentRequest)
        expect:
            request.getHttpMethod() == expected
    }

    def 'Should add headers to request spec'() {
        given:
            def key = "some-key"
            def value = "some-value"
        when:
            request.addHeader(key, value)
        then:
            1 * headers.add(key, value)
    }
}
