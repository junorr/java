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

import br.com.bb.disec.micro.json.JsonDouble;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/07/2016
 */
public class FileSize {

  public static enum Unit {
    B, KB, MB, GB, TB;
    
    public static Unit from(double size) {
      Unit unit = B;
      while(true) {
        if(size / 1024 > 1) {
          size = size / 1024;
          switch(unit) {
            case B:
              unit = KB;
              break;
            case KB:
              unit = MB;
              break;
            case MB:
              unit = GB;
              break;
            case GB:
              unit = TB;
              break;
            default: break;
          }//switch
        } else {
          break;
        }
      }//while
      return unit;
    }
  }
  
  
  private double size;
  
  
  public FileSize(long size) {
    this.size = size;
  }
  
  
  public FileSize(double size, Unit unit) {
    this.setSize(size, unit);
  }


  public long getSize() {
    return Double.valueOf(size).longValue();
  }


  public FileSize setSize(long size) {
    this.size = size;
    return this;
  }
  
  
  public double getAs(Unit unit) {
    if(unit == null) return size;
    double sz = this.size;
    switch(unit) {
      case KB:
        sz = this.size / 1024.0;
        break;
      case MB:
        sz = this.size / (1024.0 * 1024.0);
        break;
      case GB:
        sz = this.size / (1024.0 * 1024.0 * 1024.0);
        break;
      case TB:
        sz = this.size / (1024.0 * 1024.0 * 1024.0 * 1024.0);
        break;
      default:
        sz = this.size;
    }
    return sz;
  }
  
  
  public FileSize setSize(double size, Unit unit) {
    if(unit == null) unit = Unit.B;
    switch(unit) {
      case KB:
        this.size = size * 1024;
        break;
      case MB:
        this.size = size * 1024 * 1024;
        break;
      case GB:
        this.size = size * 1024 * 1024 * 1024;
        break;
      case TB:
        this.size = size * 1024 * 1024 * 1024 * 1024;
        break;
      default:
        this.size = size;
    }
    return this;
  }
  
  
  public static double round(double num, int dec) {
    double pow = Math.pow(10, dec);
    //5.1234
    long l = (long) (num * pow);
    return l / pow;
  }
  
  
  public static Converter converter() {
    return new Converter();
  }
  
  
  public String toString() {
    double sz = this.size;
    while(true) {
      if(sz / 1024 < 1) {
        break;
      }
      sz = sz / 1024.0;
    }
    return String.valueOf(round(sz, 2)) + " " + Unit.from(size);
  }
  
  
  
  
  
  
  public static class Converter implements JsonSerializer<FileSize>, JsonDeserializer<FileSize> {

    @Override
    public JsonElement serialize(FileSize t, Type type, JsonSerializationContext jsc) {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(Double.class, new JsonDouble())
          .create();
      JsonObject obj = new JsonObject();
      Unit unit = Unit.from(t.getSize());
      obj.addProperty("size", FileSize.round(t.getAs(unit), 2));
      obj.addProperty("unit", unit.name());
      return gson.toJsonTree(obj);
    }


    @Override
    public FileSize deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
      JsonObject obj = je.getAsJsonObject();
      return new FileSize(
          obj.get("size").getAsDouble(), 
          Unit.valueOf(obj.get("unit").getAsString())
      );
    }
  
  }
  
}
