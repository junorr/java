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

package us.pserver.jose.query;

import java.util.List;
import java.util.Objects;
import us.pserver.jose.driver.ByteReader;
import us.pserver.jose.driver.StringByteReader;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.query.op.ObjectOperation;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2017
 */
public interface QueryResolver {

  public boolean resolve(Query query, ByteReader<byte[]> reader);
  
  
  public static QueryResolver instance() {
    return new DefQueryJson();
  }
  
  
  public static QueryResolver debug() {
    return new DefQueryJson(true);
  }
  
  
  public static boolean resolved(Query query, ByteReader<byte[]> reader) {
    return new DefQueryJson().resolve(query, reader);
  }
  
  
  
  
  
  static final class DefQueryJson implements QueryResolver {
    
    private final boolean debug;
    
    public DefQueryJson() {
      this(false);
    }
    
    public DefQueryJson(boolean debug) {
      this.debug = debug;
    }
    
    /*
    private boolean resolveArray(Query query, StringByteReader sdr) {
      boolean result = false;
      ByteIterator bi = sdr.iterator();
      JsonType type = bi.nextValueType();
      while(!result && type != null && type != JsonType.END_ARRAY) {
        System.out.println("* resolveArray: "+ type+ " - "+ query);
        result = result || resolveOne(query, sdr);
        type = bi.nextValueType();
      }
      return result;
    }

    private boolean resolveBoolean(Query query, StringByteReader sdr) {
      boolean val = sdr.iterator().readBoolean();
      boolean result = query.operation().apply(val);
      debug(query, val, result);
      return result;
    }

    private boolean resolveNumber(Query query, StringByteReader sdr) {
      JsonValue val = sdr.iterator().readNext();
      boolean result;
      if(val.getNumberType() == JsonValue.NumberType.DOUBLE) {
        result = query.operation().apply(val.getAsDouble());
      } else {
        result = query.operation().apply(val.getAsLong());
      }
      System.out.println("resolveNumber: "+ query.operation()+ "-> "+ val+ ": "+ result);
      debug(query, val.getAsNumber(), result);
      return result;
    }

    private boolean resolveString(Query query, StringByteReader sdr) {
      String val = sdr.iterator().readString();
      boolean result = query.operation().apply(val);
      debug(query, val, result);
      return result;
    }

    private boolean resolveList(List<Query> qs, StringByteReader sdr, boolean and) {
      if(qs.isEmpty()) return false;
      System.out.println("resolveList: "+ qs);
      boolean bool = and;
      for(Query q : qs) {
        System.out.println("resolveItem: "+ q);
        boolean resolve = resolve(q, sdr);
        bool = and ? bool && resolve : bool || resolve;
      }//for
      return bool;
    }

    private boolean resolveOne(Query query, StringByteReader sdr) {
      System.out.println("ResolveOne: "+ query);
      boolean bool = false;
      // skip field name and get next value
      JsonType type = JsonType.of(sdr.iterator().getCurrentByte());
      switch(type) {
        case START_ARRAY:
          bool = resolveArray(query, sdr);
          break;
        case BOOLEAN:
          bool = resolveBoolean(query, sdr);
          break;
        case NUMBER:
          bool = resolveNumber(query, sdr);
          break;
        case STRING:
          bool = resolveString(query, sdr);
          break;
        case START_OBJECT:
          bool = resolve(query, sdr);
          break;
      }
      return bool;
    }
    
    
    private boolean resolve(Query query, StringByteReader sdr) {
      System.out.println("=> resolve: "+ query);
      boolean result;
      // no childs is a value
      if(query.ands().isEmpty() 
          && query.childs().isEmpty() 
          && query.ors().isEmpty()) {
        sdr.reset();
        if(sdr.indexOf(query.name()) < 0) {
          result = false;
        } else {
          sdr.iterator().skip().nextValueType();
          result = resolveOne(query, sdr);
        }
      }
      else {
        boolean ands = resolveList(query.ands(), sdr, true);
        boolean childs = resolveList(query.childs(), sdr, true);
        boolean ors = resolveList(query.ors(), sdr, false);
        result = ands && childs || ors;
      }
      System.out.println("<= resolve: "+ query+ ": "+ result);
      return result;
    }
    */
    
    
    private boolean resolveArray(Query query, StringByteReader sbr) {
      JsonType type;
      boolean res = false;
      while(!res && (type = JsonType.of(sbr.iterator().getCurrentByte())) != JsonType.END_ARRAY) {
        if(ObjectOperation.class.isAssignableFrom(query.operation().getClass())) {
          System.out.println("ObjectOperation");
          ObjectOperation op = (ObjectOperation) query.operation();
          res = res || op.apply(sbr.getByteReader());
        } else {
          res = res || resolveType(query, sbr);
        }
        System.out.println(" - resolveArrayItem: "+ query+ ": "+ res);
      }
      return res;
    }
    
    
    private boolean resolveType(Query query, StringByteReader sbr) {
      System.out.println("* resolveType");
      JsonType type = sbr.iterator().nextValueType();
      Object val = null;
      boolean bool = false;
      switch(type) {
        case STRING:
          val = sbr.iterator().readString();
          System.out.println("* resolveString");
          bool = query.operation().apply(val);
          debug(query, val, bool);
          return bool;
        case BOOLEAN:
          val = sbr.iterator().readBoolean();
          System.out.println("* resolveBoolean");
          bool = query.operation().apply(val);
          debug(query, val, bool);
          return bool;
        case NUMBER:
          val = sbr.iterator().readNumber();
          System.out.println("* resolveNumber");
          bool = query.operation().apply(val);
          debug(query, val, bool);
          return bool;
        case START_OBJECT:
          System.out.println("* resolveObject");
          return resolve(query, sbr);
        case START_ARRAY:
          System.out.println("* resolveArray");
          return resolveArray(query, sbr);
        default:
          return false;
      }
    }
    
    
    private boolean resolveList(List<Query> lst, StringByteReader sbr, boolean and) {
      if(lst == null || lst.isEmpty()) return false;
      System.out.println("* resolveList: "+ lst);
      int pos = sbr.getBuffer().position();
      System.out.println("* resolveList.position: "+ pos);
      boolean res = and;
      for(Query q : lst) {
        boolean bool = resolve(q, sbr);
        System.out.println(" - resolveItem: "+ q+ ": "+ bool);
        res = and ? res && bool : res || bool;
        sbr.getBuffer().position(pos);
      }
      return res;
    }
    
    
    private boolean resolve(Query query, StringByteReader sbr) {
      System.out.println("* resolve: "+ query);
      if(!query.ands().isEmpty()
          || !query.childs().isEmpty()
          || !query.ors().isEmpty()) {
        int pos = sbr.getBuffer().position();
        if(sbr.indexOf(query.name()) < 0) {
          sbr.getBuffer().position(pos);
        } else {
          pos = sbr.getBuffer().position();
        }
        System.out.println("* resolve.position: "+ pos);
        boolean ands = query.ands().isEmpty() 
            || resolveList(query.ands(), sbr, true);
        sbr.getBuffer().position(pos);
        boolean childs = query.childs().isEmpty() 
            || resolveList(query.childs(), sbr, true);
        sbr.getBuffer().position(pos);
        boolean ors = resolveList(query.ors(), sbr, false);
        System.out.println("* resolve: ands("+ ands+ ") && childs("+ childs+ ") || ors("+ ors+ ") = "+ ((ands && childs) || ors));
        return (ands && childs) || ors;
      }
      else {
        if(sbr.indexOf(query.name()) < 0) return false;
        // skip field name
        sbr.iterator().skip();
        return resolveType(query, sbr);
      }
    }
    

    @Override
    public boolean resolve(Query query, ByteReader<byte[]> rdr) {
      if(query == null || query.name() == null || rdr == null) {
        return false;
      }
      return resolve(query, StringByteReader.of(rdr));
    }
    
    
    private void debug(Query query, Object val, Object result) {
      if(debug) {
        System.err.println(query.operation()
            + ".apply( "
            + Objects.toString(val).trim()
            + " ): "
            + result
        );
      }
    }
  
  }
  
}
