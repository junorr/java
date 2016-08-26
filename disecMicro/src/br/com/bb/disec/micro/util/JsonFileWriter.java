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

package br.com.bb.disec.micro.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/08/2016
 */
public class JsonFileWriter {

  private final Path path;
  
  private BufferedWriter writer;
  
  
  public JsonFileWriter(Path path) {
    if(path == null) {
      throw new IllegalArgumentException("Bad Null Path");
    }
    this.path = path;
  }
  
  
  public static JsonFileWriter of(Path path) {
    return new JsonFileWriter(path);
  }
  
  
  public static JsonFileWriter temp() throws IOException {
    return new JsonFileWriter(Files.createTempFile(null, ".json"));
  }
  
  
  public Path getPath() {
    return path;
  }
  
  
  public JsonFileWriter open() throws IOException {
    if(writer == null) {
      writer = Files.newBufferedWriter(
          path, 
          Charset.forName("UTF-8"), 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE
      );
    }
    return this;
  }
  
  
  public JsonFileWriter close() throws IOException {
    if(writer != null) {
      writer.close();
      writer = null;
    }
    return this;
  }
  
  
  private JsonFileWriter write(String s) throws IOException {
    if(s == null || s.isEmpty()) return this;
    if(writer == null) {
      this.open();
    }
    writer.write(s);
    return this;
  }
  
  
  public JsonFileWriter put(String s) throws IOException {
    if(s == null || s.isEmpty()) return this;
    return this.write("\"").write(s).write("\"");
  }
  
  
  public JsonFileWriter put(Number n) throws IOException {
    if(n == null) return this;
    return this.write(Objects.toString(n));
  }
  
  
  public JsonFileWriter put(Boolean b) throws IOException {
    if(b == null) return this;
    return this.write(Objects.toString(b));
  }
  
  
  public JsonFileWriter put(Date d) throws IOException {
    if(d == null) return this;
    return this.put(Objects.toString(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(d.getTime()), 
            ZoneId.systemDefault()))
    );
  }
  
  
  public JsonFileWriter startObject() throws IOException {
    return this.write("{");
  }
  
  
  public JsonFileWriter startObject(String key) throws IOException {
    return this.put(key).write(":{");
  }
  
  
  public JsonFileWriter endObject() throws IOException {
    return this.write("}");
  }
  
  
  public JsonFileWriter startArray(String key) throws IOException {
    return this.put(key).write(":[");
  }
  
  
  public JsonFileWriter startArray() throws IOException {
    return this.write("[");
  }
  
  
  public JsonFileWriter endArray() throws IOException {
    return this.write("]");
  }
  
  
  public JsonFileWriter nextElement() throws IOException {
    return this.write(",");
  }
  
  
  public JsonFileWriter put(String key, String value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null 
        || value.isEmpty()) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonFileWriter put(String key, Number value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonFileWriter put(String key, Boolean value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
  
  public JsonFileWriter put(String key, Date value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
}
