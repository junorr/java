/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.undertow.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class ServerConfig {

  private final String address;
  
  private final int port;
  
  private final Map<String,Class> handlers;
  
  
  public ServerConfig(String address, int port, Map<String,Class> map) {
    if(address == null || address.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid Address: "+ address);
    }
    if(port <= 0 || port > 65535) {
      throw new IllegalArgumentException("Invalid Port: "+ port);
    }
    this.address = address;
    this.port = port;
    if(map != null) {
      this.handlers = Collections.unmodifiableMap(map);
    }
    else {
      this.handlers = Collections.EMPTY_MAP;
    }
  }


  public String getAddress() {
    return address;
  }


  public int getPort() {
    return port;
  }


  public Map<String, Class> handlers() {
    return handlers;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.address);
    hash = 97 * hash + this.port;
    hash = 97 * hash + Objects.hashCode(this.handlers);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ServerConfig other = (ServerConfig) obj;
    if (this.port != other.port) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.handlers, other.handlers)) {
      return false;
    }
    return true;
  }
  
  
  public ServerConfig save(Path path) throws IOException {
    new Builder()
        .setServerAddress(address)
        .setServerPort(port)
        .setHandlers(handlers)
        .save(path);
    return this;
  }


  @Override
  public String toString() {
    return "ServerConfig{" + "address=" + address + ", port=" + port + ", handlers=" + handlers + '}';
  }
  
  
  
  
  
  
  public static class Builder {
    
    private String serverAddress;
    
    private int serverPort;
    
    private Map<String,Class> handlers;
    
    
    public Builder() {
      handlers = new HashMap<>();
    }
    
    
    public Builder put(String path, Class cls) {
      if(path != null && cls != null) {
        handlers.put(path, cls);
      }
      return this;
    }


    public String getServerAddress() {
      return serverAddress;
    }


    public Builder setServerAddress(String serverAddress) {
      this.serverAddress = serverAddress;
      return this;
    }


    public int getServerPort() {
      return serverPort;
    }


    public Builder setServerPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }


    public Map<String, Class> getHandlers() {
      return handlers;
    }


    public Builder setHandlers(Map<String, Class> handlers) {
      this.handlers = handlers;
      return this;
    }
    
    
    public ServerConfig build() {
      return new ServerConfig(serverAddress, serverPort, handlers);
    }
    
    
    public Builder save(Path path) throws IOException {
      try (FileWriter fw = new FileWriter(path.toFile())) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new ClassConverter())
            .setPrettyPrinting().create();
        fw.write(gson.toJson(this));
      }
      return this;
    }
    
    
    public Builder load(Path path) throws IOException {
      try (FileReader fr = new FileReader(path.toFile())) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new ClassConverter())
            .create();
        Builder b = gson.fromJson(fr, Builder.class);
        this.setServerAddress(b.getServerAddress())
            .setServerPort(b.getServerPort())
            .setHandlers(b.getHandlers());
        return this;
      }
    }


    @Override
    public String toString() {
      return "ServerConfig.Builder{" + "address=" + serverAddress + ", port=" + serverPort + ", handlers=" + handlers + '}';
    }
    
  }
  
}
