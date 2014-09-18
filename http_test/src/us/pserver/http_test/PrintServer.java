/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.http_test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;


/**
 *
 * @author juno
 */
public class PrintServer {
  
  public static final int DEF_PORT = 9099;
  
  private String address;
  
  private int port;
  
  private ServerSocket ssock;
  
  
  public PrintServer() {
    address = null;
    port = DEF_PORT;
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public int getPort() {
    return port;
  }


  public void setPort(int port) {
    this.port = port;
  }
  
  
  public void start() throws IOException {
    ssock = new ServerSocket();
    SocketAddress sad;
    if(address == null)
      sad = new InetSocketAddress(port);
    else
      sad = new InetSocketAddress(address, port);
    
    ssock.bind(sad);
    System.out.println("* server started: "+ sad);
    
    this.run();
  }
  
  
  public void run() throws IOException {
    Socket sc = ssock.accept();
    this.handle(sc);
  }
  
  
  private void handle(Socket sc) throws IOException {
    if(sc == null || sc.isClosed()) return;
    System.out.println("* connected: "+ sc);
    this.transf(sc.getInputStream(), System.out);
    this.writeResponse(sc);
  }
  
  
  private void transf(InputStream in, OutputStream out) throws IOException {
    if(in == null || out == null) return;
    byte[] buf = new byte[512];
    int read = 1;
    
    while((read = in.read(buf)) > 0) {
      out.write(buf, 0, read);
      if(new String(buf, 0, read).contains("EOF")
          && read < buf.length) break;
    }
  }
  
  
  private void writeResponse(Socket sc) throws IOException {
    if(sc == null || sc.isClosed())
      return;
    ResponseBuilder rb = new ResponseBuilder();
    rb.add(new ResponseLine(200, "OK"));
    rb.add("Content-Type", "text/xml")
        .add("Content-Encoding", "deflate")
        .add("Content-Length", "176")
        .add("Server", "PrintServer")
        .add("Date", new Date().toString())
        .appendBoundary();
    
    String content = "98a76b543d2165c49f87"
        + "98a76b543d2165c49f87"
        + "98a76b543d2165c49f87"
        + "98a76b543d2165c49f87"
        + "98a76b543d2165c49f87"
        + "98a76b543d2165c49f87";
    
    OutputStream out = sc.getOutputStream();
    rb.writeTo(out);
    out.write(content.getBytes());
    out.write(ResponseBuilder.BOUNDARY_END.getBytes());
    out.write(10);
    out.write(13);
    out.write(10);
    out.write(13);
    out.flush();
    sc.shutdownOutput();
    sc.close();
    this.run();
  }
  
  
  public static void main(String[] args) throws IOException {
    PrintServer ps = new PrintServer();
    ps.start();
  }
  
}
