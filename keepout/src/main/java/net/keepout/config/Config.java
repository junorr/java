/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.config;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;


/**
 *
 * @author juno
 */
public class Config {
  
  private final NetService server;
  
  private final NetService client;
  
  private final List<String> tokens;


  public Config(NetService server, NetService client, Collection<String> tokens) {
    this.server = Objects.requireNonNull(server);
    this.client = Objects.requireNonNull(client);
    this.tokens = Collections.unmodifiableList(new ArrayList<>(tokens));
  }


  public NetService getServer() {
    return server;
  }
  
  
  public NetService getClient() {
    return client;
  }
  
  
  public List<String> getTokens() {
    return tokens;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + Objects.hashCode(this.server);
    hash = 59 * hash + Objects.hashCode(this.client);
    hash = 59 * hash + Objects.hashCode(this.tokens);
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
    final Config other = (Config) obj;
    if (!Objects.equals(this.server, other.server)) {
      return false;
    }
    if (!Objects.equals(this.client, other.client)) {
      return false;
    }
    return Objects.equals(this.tokens, other.tokens);
  }


  @Override
  public String toString() {
    return "Config{" + "server=" + server + ", client=" + client + ", tokens=" + tokens + '}';
  }
  
  
  
  public static Config loadClasspath(String resource) throws Exception {
    ConfigFilesProvider cfp = () -> Arrays.asList(Paths.get(resource));
    ClasspathConfigurationSource src = new ClasspathConfigurationSource(cfp);
    ConfigurationProvider prv = new ConfigurationProviderBuilder().withConfigurationSource(src).build();
    return createConfig(prv);
  }
  
  
  public static Config loadPath(String path) throws Exception {
    ConfigFilesProvider cfp = () -> Arrays.asList(Paths.get(path));
    FilesConfigurationSource src = new FilesConfigurationSource(cfp);
    ConfigurationProvider prv = new ConfigurationProviderBuilder().withConfigurationSource(src).build();
    return createConfig(prv);
  }
  
  
  private static Config createConfig(ConfigurationProvider prv) {
    Host bind = new Host(prv.getProperty("server.bind.address", String.class), prv.getProperty("server.bind.port", Integer.class));
    Host target = new Host(prv.getProperty("server.target.address", String.class), prv.getProperty("server.target.port", Integer.class));
    NetService server = new NetService(bind, target);
    bind = new Host(prv.getProperty("client.bind.address", String.class), prv.getProperty("client.bind.port", Integer.class));
    target = new Host(prv.getProperty("client.target.address", String.class), prv.getProperty("client.target.port", Integer.class));
    NetService client = new NetService(bind, target);
    return new Config(server, client, prv.getProperty("authTokens", List.class));
  }
  
}
