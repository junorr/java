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

package us.pserver.micro.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import us.pserver.micro.ServerSetup;
import us.pserver.micro.util.JsonElementSerializer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/03/2018
 */
public class _DBConfig {
  
  private final DB db;
  
  private final String user;
  
  private final byte[] pwd;
  
  
  public _DBConfig(DB db, String user, byte[] pwd) {
    this.db = db;
    this.user = user;
    this.pwd = pwd;
  }


  public String getUser() {
    return user;
  }


  public byte[] getPwd() {
    return pwd;
  }
  
  
  public DB getDB() {
    return db;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.db);
    hash = 79 * hash + Objects.hashCode(this.user);
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
    final _DBConfig other = (_DBConfig) obj;
    if (!Objects.equals(this.user, other.user)) {
      return false;
    }
    if (this.db != other.db) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "DBConfig{" + "db=" + db + ", user=" + user + '}';
  }
  
  
  public HTreeMap<byte[],JsonElement> getMap(String name) {
    return db.hashMap(name, Serializer.BYTE_ARRAY, new JsonElementSerializer(ServerSetup.INSTANCE.config().gson())).createOrOpen();
  }
  
  
  public byte[] findObjectKey(HTreeMap<byte[],JsonElement> map, JsonObject job) {
    Stream<Map.Entry<byte[],JsonElement>> str = map.keySet().stream();
    str = str.filter(e->e.getValue().isJsonObject());
    str = str.filter(e->{
      JsonObject mob = e.getValue().getAsJsonObject();
      return job.entrySet().stream().allMatch(je->mob.entrySet().stream().anyMatch(me->je.getKey().equals(me.getKey()) && je.getValue().equals(me.getValue())));
    });
    Optional<Map.Entry<byte[],JsonElement>> opt = str.findAny();
    return opt.isPresent() ? opt.get().getKey() : null;
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  public static class Builder {
    
    public static enum Type {
      FILE, MEMORY, MAPPED_FILE
    }

    private Path file;
    
    private Type type;
    
    private int concurrency;
    
    private String user;
    
    private String pwd;


    public Path getFile() {
      return file;
    }


    public Builder setFile(Path file) {
      this.file = file;
      return this;
    }


    public Type getType() {
      return type;
    }


    public Builder setType(Type type) {
      this.type = type;
      return this;
    }


    public int getConcurrency() {
      return concurrency;
    }


    public Builder setConcurrency(int concurrency) {
      this.concurrency = concurrency;
      return this;
    }


    public String getUser() {
      return user;
    }


    public Builder setUser(String user) {
      this.user = user;
      return this;
    }


    public String getPwd() {
      return pwd;
    }


    public Builder setPwd(String pwd) {
      this.pwd = pwd;
      return this;
    }


    @Override
    public int hashCode() {
      int hash = 5;
      hash = 37 * hash + Objects.hashCode(this.file);
      hash = 37 * hash + Objects.hashCode(this.type);
      hash = 37 * hash + Objects.hashCode(this.user);
      hash = 37 * hash + Objects.hashCode(this.pwd);
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
      final Builder other = (Builder) obj;
      if (!Objects.equals(this.user, other.user)) {
        return false;
      }
      if (!Objects.equals(this.pwd, other.pwd)) {
        return false;
      }
      if (!Objects.equals(this.file, other.file)) {
        return false;
      }
      if (this.type != other.type) {
        return false;
      }
      return true;
    }


    @Override
    public String toString() {
      return "Builder{" + "file=" + file + ", type=" + type + ", concurrency=" + concurrency + ", user=" + user + '}';
    }
    
    
    public DB createDB() {
      DB db = null;
      switch(type) {
        case FILE:
          db = DBMaker.fileDB(file.toFile())
              .concurrencyScale(concurrency)
              .make();
          break;
        case MAPPED_FILE:
          db = DBMaker.fileDB(file.toFile())
              .concurrencyScale(concurrency)
              .fileMmapEnable()
              .make();
          break;
        default:
          db = DBMaker.memoryDirectDB()
              .concurrencyScale(concurrency)
              .make();
          break;
      }
      return db;
    }
    
    
    public _DBConfig create() {
      return new _DBConfig(createDB(), user, pwd.getBytes(StandardCharsets.UTF_8));
    }
    
  }
  
}
