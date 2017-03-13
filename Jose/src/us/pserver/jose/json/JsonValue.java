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

package us.pserver.jose.json;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/03/2017
 */
public interface JsonValue {

  public JsonType getJsonType();
  
  public String getValue();
  
  public Number getAsNumber();
  
  public NumberType getNumberType();
  
  public double getAsDouble();
  
  public long getAsLong();
  
  public int getAsInt();
  
  public boolean getAsBoolean();
  
  public boolean isNull();
  
  public <T> T get();
  

  
  public static JsonValue of(JsonType tkn, String val) {
    return new DefJsonValue(tkn, val);
  }

  
  public static JsonValue of(String val) {
    if(val == null || val.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Null String value");
    }
    String str = val.trim();
    byte f = (byte) str.charAt(0);
    return new DefJsonValue(JsonType.of(f), str);
  }
  
  
  
  
  
  public static enum NumberType {
    LONG, DOUBLE, NaN;
  }
  
  
  
  
  
  
  public static final class DefJsonValue implements JsonValue {
    
    private final JsonType type;
    
    private final String value;
    
    
    public DefJsonValue(JsonType tkn, String val) {
      if(tkn == null) {
        throw new IllegalArgumentException("Bad Null JsonToken");
      }
      if(val == null) {
        throw new IllegalArgumentException("Bad Null String Value");
      }
      this.type = tkn;
      this.value = val;
    }
    

    @Override
    public JsonType getJsonType() {
      return type;
    }


    @Override
    public String getValue() {
      return value;
    }


    @Override
    public Number getAsNumber() {
      try {
        if(type != JsonType.NUMBER) {
          throw new NumberFormatException();
        }
        if(value.contains(".")) {
          return Double.parseDouble(value);
        }
        else {
          return Long.parseLong(value);
        }
      } catch(NumberFormatException e) {
        throw new IllegalStateException("Bad Json Type Conversion ("+ value+ "): "+ type+ " -> Number");
      }
    }
    
    
    @Override
    public NumberType getNumberType() {
      if(type != JsonType.NUMBER) {
        return NumberType.NaN;
      }
      if(value.contains(".")) {
        return NumberType.DOUBLE;
      }
      return NumberType.LONG;
    }


    @Override
    public double getAsDouble() {
      return getAsNumber().doubleValue();
    }


    @Override
    public long getAsLong() {
      return getAsNumber().longValue();
    }


    @Override
    public int getAsInt() {
      return getAsNumber().intValue();
    }


    @Override
    public boolean getAsBoolean() {
      if(type != JsonType.BOOLEAN) {
        throw new IllegalStateException("Bad Json Type Conversion ("+ value+ "): "+ type+ " -> Boolean");
      }
      return Boolean.parseBoolean(value);
    }


    @Override
    public boolean isNull() {
      return type == JsonType.NULL;
    }


    @Override
    public <T> T get() {
      switch(type) {
        case BOOLEAN:
          return (T) Boolean.valueOf(getAsBoolean());
        case NULL:
          return null;
        case NUMBER:
          return (T) getAsNumber();
        case STRING:
        case UNKNOWN:
          return (T) getValue();
        default:
          throw new IllegalStateException("Bad Json Type Conversion ("+ value+ ") -> "+ type);
      }
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 47 * hash + Objects.hashCode(this.type);
      hash = 47 * hash + Objects.hashCode(this.value);
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
      final DefJsonValue other = (DefJsonValue) obj;
      if (!Objects.equals(this.value, other.value)) {
        return false;
      }
      return this.type == other.type;
    }


    @Override
    public String toString() {
      String stp = type.toString();
      if(type == JsonType.NUMBER) {
        stp += "." + this.getNumberType().toString();
      }
      return "JsonValue{" + stp + ", \"" + value + "\"}";
    }
    
  }
  
}
