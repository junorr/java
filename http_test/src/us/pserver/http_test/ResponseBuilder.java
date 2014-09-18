/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.http_test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author juno
 */
public class ResponseBuilder {
  
  public static final String LN = "\n";
  
  public static final String BOUNDARY_START = "\n\r<xml><cnt f='hex'>";
  
  public static final String BOUNDARY_END = "</xml></cnt>";
  
  private List<Header> hds;
  
  
  public ResponseBuilder() {
    hds = new LinkedList<>();
  }
  
  
  public ResponseBuilder add(Header hd) {
    if(hd != null) hds.add(hd);
    return this;
  }
  
  
  public ResponseBuilder add(String name, String value) {
    if(name != null) hds.add(new Header(name, value));
    return this;
  }
  
  
  public ResponseBuilder appendBoundary(String boundary) {
    if(boundary != null) 
      this.add(Header.boundary(boundary));
    return this;
  }
  
  
  public ResponseBuilder appendBoundary() {
    this.add(Header.boundary(BOUNDARY_START));
    return this;
  }
  
  
  public List<Header> headers() {
    return hds;
  }
  
  
  public void writeTo(OutputStream out) throws IOException {
    if(out == null) 
      throw new IllegalArgumentException(
          "Invalid OutputStream ["+ out+ "]");
    if(hds.isEmpty()) return;
    
    StringBuilder resp = new StringBuilder();
    Iterator<Header> it = hds.iterator();
    while(it.hasNext()) {
      resp.append(it.next().toString()).append(LN);
    }
    out.write(resp.toString().getBytes());
    out.flush();
  }
  
}
