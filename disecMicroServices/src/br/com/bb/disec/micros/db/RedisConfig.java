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

package br.com.bb.disec.micros.db;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/02/2017
 */
public class RedisConfig {
  
  public static final int DEFAULT_PORT = 6379;
  
  public static final int MIN_CONNECTIONS = 1;
  
  public static final int MAX_CONNECTIONS = 10;
  

  private final String host;
  
  private final int port;
  
  private final int minConnections;
  
  private final int maxConnections;
  
  
  public RedisConfig(
      String host,
      int port,
      int minCon,
      int maxCon) {
    if(host == null) {
      throw new IllegalArgumentException("Bad Null Host");
    }
    this.host = host;
    this.port = (port <= 0 || port > 65535 
        ? DEFAULT_PORT : port);
    this.minConnections = (minCon <= 0 ? MIN_CONNECTIONS : minCon);
    this.maxConnections = (minCon <= 0 ? MAX_CONNECTIONS : maxCon);
  }


  public RedisConfig(
      String host,
      int minCon,
      int maxCon) {
    this(host, 0, minCon, maxCon);
  }


  public String getHost() {
    return host;
  }


  public int getPort() {
    return port;
  }


  public int getMinConnections() {
    return minConnections;
  }


  public int getMaxConnections() {
    return maxConnections;
  }


  @Override
  public String toString() {
    return "RedisConfig{" + "\n  host=" + host + ",\n  port=" + port + ",\n  minCon=" + minConnections + ",\n  maxCon=" + maxConnections + "\n}";
  }
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  public static final class Builder {

    private String host;

    private int port;

    private int minConnections;

    private int maxConnections;


    public Builder() {}


    public String getHost() {
      return host;
    }


    public Builder setHost(String host) {
      this.host = host;
      return this;
    }


    public int getPort() {
      return port;
    }


    public Builder setPort(int port) {
      this.port = port;
      return this;
    }


    public int getMinConnections() {
      return minConnections;
    }


    public Builder setMinConnections(int minCon) {
      this.minConnections = minCon;
      return this;
    }


    public int getMaxConnections() {
      return maxConnections;
    }


    public Builder setMaxConnections(int maxCon) {
      this.maxConnections = maxCon;
      return this;
    }


    @Override
    public String toString() {
      return "Builder{" + "\n  host=" + host + ",\n  port=" + port + ",\n  minCon=" + minConnections + ",\n  maxCon=" + maxConnections + "\n}";
    }
    
    
    public RedisConfig build() {
      return new RedisConfig(
          this.host, this.port, 
          this.minConnections, 
          this.maxConnections
      );
    }
    
    
    public Builder load(InputStream in) {
      if(in == null) {
        throw new IllegalArgumentException("Bad Null InputStream");
      }
      Builder b = new Gson().fromJson(new InputStreamReader(in), Builder.class);
      return this.setHost(b.getHost())
          .setPort(b.getPort())
          .setMinConnections(b.getMinConnections())
          .setMaxConnections(b.getMaxConnections());
    }
    
  }
  
}
