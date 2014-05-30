/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.http_test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import us.pserver.cdr.b64.Base64StringCoder;


/**
 *
 * @author juno
 */
public class TestHttpClient {
  
  
  public static void main(String[] args) throws IOException {
    URL url = new URL("http://172.24.75.2:9099");
    HttpURLConnection conn = (HttpURLConnection) 
        url.openConnection(new Proxy(Proxy.Type.HTTP, 
            new InetSocketAddress("172.24.75.19", 6060)));
    conn.setRequestMethod("POST");
    conn.setDefaultUseCaches(false);
    conn.setUseCaches(false);
    conn.setRequestProperty("Content-Type", "multipart/form-data");
    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
    conn.setRequestProperty("Accept-Encoding", "deflate");
    conn.setRequestProperty("Accept", "text/html, text/xml, application/xml");
    Base64StringCoder b64 = new Base64StringCoder();
    conn.setRequestProperty("Proxy-Authorization", "Basic " + b64.encode("f6036477:32132100"));
    
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.connect();
    OutputStream out = conn.getOutputStream();
    FileInputStream fis = new FileInputStream("f:/CCV.pdf");
    transf(fis, out);
    fis.close();

    out.write(10);
    out.write(13);
    out.write("EOF".getBytes());
    out.write(10);
    out.write(13);
    out.write(10);
    out.write(13);
    out.flush();
    out.close();
    //conn.setDoOutput(false);
    
    System.out.println(conn.getResponseCode()+ " - "
        + conn.getResponseMessage());
    
    transf(conn.getInputStream(), System.out);
  }
  
  
  public static void transf(InputStream in, OutputStream out)
      throws IOException {
    if(in == null || out == null) return;
    byte[] buf = new byte[512];
    int read = 1;
    while((read = in.read(buf)) > 0) {
      out.write(buf, 0, read);
      out.flush();
    }
  }
  
}
