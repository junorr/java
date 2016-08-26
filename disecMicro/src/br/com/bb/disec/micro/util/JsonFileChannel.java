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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
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
public class JsonFileChannel {
  
  public static final String LINE_BREAK = "\n\r";

  private final Path path;
  
  private SeekableByteChannel channel;
  
  private ByteBuffer buffer;
  
  private final Charset charset;
  
  
  public JsonFileChannel(Path path) {
    if(path == null) {
      throw new IllegalArgumentException("Bad Null Path");
    }
    this.path = path;
    charset = Charset.forName("UTF-8");
  }
  
  
  private String bufferString() {
    String str = "";
    if(buffer.remaining() > 0) {
      byte[] bs = new byte[buffer.remaining()];
      buffer.get(bs);
      str = new String(bs, charset);
    }
    return str;
  }
  
  
  public JsonObject readJson() throws IOException {
    long pos = channel.position();
    flush();
    channel.position(0);
    int read = 0;
    StringBuilder sb = new StringBuilder();
    while((read = channel.read(buffer)) > 0) {
      buffer.flip();
      sb.append(bufferString());
      buffer.clear();
    }
    return new JsonParser().parse(sb.toString()).getAsJsonObject();
  }
  
  
  public static JsonFileChannel of(Path path) {
    return new JsonFileChannel(path);
  }
  
  
  public static JsonFileChannel temp() throws IOException {
    return new JsonFileChannel(Files.createTempFile(null, ".json"));
  }
  
  
  public Path getPath() {
    return path;
  }
  
  
  public SeekableByteChannel getFileChannel() {
    return channel;
  }
  
  
  public JsonFileChannel open() throws IOException {
    if(channel == null) {
      buffer = ByteBuffer.allocateDirect(4096);
      channel = Files.newByteChannel(
          path, 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE
      );
    }
    return this;
  }
  
  
  public JsonFileChannel close() throws IOException {
    if(channel != null) {
      channel.close();
      channel = null;
      buffer = null;
    }
    return this;
  }
  
  
  private JsonFileChannel flush() throws IOException {
    if(buffer.position() > 0) {
      buffer.flip();
      channel.write(buffer);
      buffer.clear();
    }
    return this;
  }
  
  
  private JsonFileChannel write(String s) throws IOException {
    if(s == null || s.isEmpty()) return this;
    if(channel == null) {
      this.open();
    }
    return this.write(s.getBytes(charset));
  }
  
  
  private JsonFileChannel write(byte[] bs) throws IOException {
    int start = 0;
    int count = 0;
    while(start < bs.length) {
      count = Math.min(
          (bs.length-start), 
          buffer.remaining()
      );
      buffer.put(bs, start, count);
      flush();
      start += count;
    }
    return this;
  }
  
  
  public JsonFileChannel lineBreak() throws IOException {
    return this.write(LINE_BREAK);
  }
  
  
  public JsonFileChannel put(String s) throws IOException {
    if(s == null || s.isEmpty()) return this;
    return this.write("\"").write(s).write("\"");
  }
  
  
  public JsonFileChannel put(Number n) throws IOException {
    if(n == null) return this;
    return this.write(Objects.toString(n));
  }
  
  
  public JsonFileChannel put(Boolean b) throws IOException {
    if(b == null) return this;
    return this.write(Objects.toString(b));
  }
  
  
  public JsonFileChannel put(Date d) throws IOException {
    if(d == null) return this;
    return this.put(Objects.toString(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(d.getTime()), 
            ZoneId.systemDefault()))
    );
  }
  
  
  public JsonFileChannel startObject() throws IOException {
    return this.write("{");
  }
  
  
  public JsonFileChannel startObject(String key) throws IOException {
    return this.put(key).write(":{");
  }
  
  
  public JsonFileChannel endObject() throws IOException {
    return this.write("}");
  }
  
  
  public JsonFileChannel startArray(String key) throws IOException {
    return this.put(key).write(":[");
  }
  
  
  public JsonFileChannel startArray() throws IOException {
    return this.write("[");
  }
  
  
  public JsonFileChannel endArray() throws IOException {
    return this.write("]");
  }
  
  
  public JsonFileChannel nextElement() throws IOException {
    return this.write(",");
  }
  
  
  public JsonFileChannel put(String key, String value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null 
        || value.isEmpty()) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonFileChannel put(String key, Number value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonFileChannel put(String key, Boolean value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
  
  public JsonFileChannel put(String key, Date value) throws IOException {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
}
