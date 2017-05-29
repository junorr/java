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

package us.pserver.job.json.adapter;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/03/2017
 */
public interface JsonValue {

  public JsonToken getType();
  
  public String getValue();
  
  public Number getAsNumber();
  
  public double getAsDouble();
  
  public long getAsLong();
  
  public int getAsInt();
  
  public boolean getAsBoolean();
  
  public boolean isNull();
  
  public <T> T get();
  
  
  public static JsonValue of(JsonToken tkn, String val) {
    return new DefJsonValue(tkn, val);
  }
  
  
  
  
  
  public static final class DefJsonValue implements JsonValue {
    
    private final JsonToken type;
    
    private final String value;
    
    
    public DefJsonValue(JsonToken tkn, String val) {
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
    public JsonToken getType() {
      return type;
    }


    @Override
    public String getValue() {
      return value;
    }


    @Override
    public Number getAsNumber() {
      try {
        if(type != JsonToken.NUMBER) {
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
      if(type != JsonToken.BOOLEAN) {
        throw new IllegalStateException("Bad Json Type Conversion ("+ value+ "): "+ type+ " -> Boolean");
      }
      return Boolean.parseBoolean(value);
    }


    @Override
    public boolean isNull() {
      return type == JsonToken.NULL;
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
      return "JsonValue{" + type + ", value=" + value + '}';
    }
    
  }
  
}
