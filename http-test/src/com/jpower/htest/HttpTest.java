/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.htest;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


/**
 *
 * @author juno
 */
public class HttpTest {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException {
    HttpGet get = new HttpGet("http://intranet.bb.com.br");
    HttpClient cli = new DefaultHttpClient();
    cli.getConnectionManager();
    HttpResponse resp = cli.execute(get);
    System.out.println("* "+ resp.getStatusLine());
    System.out.println("* "+ resp.getStatusLine().getProtocolVersion());
    System.out.println("* "+ resp.getStatusLine().getStatusCode());
    System.out.println("* "+ resp.getStatusLine().getReasonPhrase());
    Header[] hds = resp.getAllHeaders();
    for(Header h : hds) {
      System.out.println(" - "+ h.getName()+ ": "+ h.getValue());
    }
    System.out.println("------------------------------");
    System.out.println(EntityUtils.toString(resp.getEntity()));
    System.out.println("------------------------------");
  }
}
