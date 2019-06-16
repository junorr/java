/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import net.keepout.client.Unchecked;
import org.junit.jupiter.api.Test;


/**
 *
 * @author juno
 */
public class TestJava11HttpClient {
  
  private static final HttpClient http = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();
  
  @Test
  public void send_and_read_webhook_request() {
    ByteBuffer body = StandardCharsets.UTF_8.encode("--->> Hello World from java.net.http 11!! <<---");
    ByteBufferBodyPublisher pub = new ByteBufferBodyPublisher(body);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(Unchecked.call(()->new URI("https://webhook.site/318ea209-1d5d-4a3f-81eb-fcf9b17b133d?issued_by=keepout")))
        .setHeader("x-cliend-id", "A1B2C3D4E5F6789")
        .setHeader("Content-Type", "text; charset=utf-8")
        .POST(HttpRequest.BodyPublishers.fromPublisher(pub, body.remaining()))
        .build();
    System.out.println(body);
    HttpResponse<String> res = Unchecked.call(()->http.send(req, 
        HttpResponse.BodyHandlers.fromSubscriber(new BodyToStringSubscriber(), BodyToStringSubscriber::toString))
    );
    System.out.println(res);
    System.out.println("------  HEADERS  ------");
    res.headers().map().forEach((k,v)->System.out.printf("%s: %s%n", k, v));
  }
  
  
  @Test
  public void get_from_google() {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(Unchecked.call(()->new URI("https://google.com")))
        .setHeader("x-cliend-id", "A1B2C3D4E5F6789")
        .GET()
        .build();
    HttpResponse<String> res = Unchecked.call(()->http.send(req, 
        HttpResponse.BodyHandlers.fromSubscriber(new BodyToStringSubscriber(), BodyToStringSubscriber::toString))
    );
    System.out.println(res);
    System.out.println("------  HEADERS  ------");
    res.headers().map().forEach((k,v)->System.out.printf("%s: %s%n", k, v));
    System.out.println("------   BODY    ------");
    System.out.println(res.body());
  }
  
  
  
  public static class BodyToStringSubscriber implements Flow.Subscriber<List<ByteBuffer>> {
    
    private final List<ByteBuffer> buffers;
    
    private Flow.Subscription subs;
    
    private final StringBuilder content;
    
    public BodyToStringSubscriber() {
      this.buffers = new LinkedList<>();
      this.content = new StringBuilder();
      subs = null;
    }
    
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subs = subscription;
      subs.request(1);
    }
    
    @Override
    public void onNext(List<ByteBuffer> items) {
      System.out.printf("[%s] onNext: %s%n", getClass().getSimpleName(), items);
      buffers.addAll(items);
      subs.request(1);
    }
    
    @Override
    public void onError(Throwable throwable) {
      throw Unchecked.unchecked(throwable);
    }
    
    @Override
    public String toString() {
      return content.toString();
    }
    
    @Override
    public void onComplete() {
      buffers.stream()
          .map(b -> StandardCharsets.UTF_8.decode(b).toString())
          .forEach(content::append);
    }
    
  }
  
  
  
  public static class ByteBufferBodyPublisher implements Flow.Publisher<ByteBuffer> {
    
    private final List<ByteBuffer> buffers;
    
    public ByteBufferBodyPublisher(Collection<ByteBuffer> bufs) {
      this.buffers = new ArrayList<>(bufs);
    }
    
    public ByteBufferBodyPublisher(ByteBuffer buf) {
      this(Arrays.asList(buf));
    }
    
    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
      Flow.Subscription subs = new Flow.Subscription() {
        private final Iterator<ByteBuffer> it = buffers.stream().map(ByteBuffer::duplicate).iterator();
        private volatile boolean canceled = false;
        @Override
        public void request(long n) {
          for(int i = 0; i < n && it.hasNext() && !canceled; i++) {
            ByteBuffer b = it.next();
            System.out.printf("[%s] publishing: %s%n", this.getClass().getSimpleName(), b);
            subscriber.onNext(b);
          }
          if(!it.hasNext()) subscriber.onComplete();
        }
        @Override
        public void cancel() {
          canceled = true;
        }
      };
      subscriber.onSubscribe(subs);
    }
    
  }
  
}
