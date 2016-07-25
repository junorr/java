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

package us.pserver.rob.http;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.util.Objects;
import us.pserver.cdr.StringByteConverter;
import static us.pserver.rob.http.GetConsts.ARGS;
import static us.pserver.rob.http.GetConsts.AUTH;
import static us.pserver.rob.http.GetConsts.METHOD;
import static us.pserver.rob.http.GetConsts.OBJECT;
import static us.pserver.rob.http.GetConsts.TYPES;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2015
 */
public abstract class GetRequestUtils {
  
  
  public static GetRequest toGetRequest(BinMethodRequest bin, String address) {
    if(bin == null) return null;
    StringByteConverter scv = new StringByteConverter();
    GetRequest get = new GetRequest(address);
    get.query(OBJECT, scv.reverse(bin.getBinObject()));
    get.query(METHOD, scv.reverse(bin.getBinMethod()));
    if(bin.getBinAuth() != null) {
      get.query(AUTH, scv.reverse(bin.getBinAuth()));
    }
    if(bin.getBinTypes() != null && bin.getBinTypes().length > 0) {
      String[] st = new String[bin.getBinTypes().length];
      String[] sa = new String[bin.getBinTypes().length];
      for(int i = 0; i < bin.getBinTypes().length; i++) {
        st[i] = scv.reverse(bin.getBinTypes()[i]);
        sa[i] = scv.reverse(bin.getBinArgs()[i]);
      }
      get.queryList(TYPES, st).queryList(ARGS, sa);
    }
    return get;
  }
  
  
  public static BinMethodRequest toBinRequest(GetRequest get) {
    if(get == null) return null;
    BinMethodRequest bin = new BinMethodRequest();
    StringByteConverter scv = new StringByteConverter();
    bin.setBinObject(scv.convert(get.queryGet(OBJECT)));
    bin.setBinMethod(scv.convert(get.queryGet(METHOD)));
    if(get.queryContains(AUTH)) {
      bin.setBinAuth(scv.convert(get.queryGet(AUTH)));
    }
    if(get.queryContains(TYPES)) {
      int ts = get.queryKeySize(TYPES);
      bin.setBinTypes(new byte[ts][1]);
      bin.setBinArgs(new byte[ts][1]);
      for(int i = 0; i < ts; i++) {
        bin.getBinTypes()[i] = scv.convert(get.queryGet(TYPES, i));
        bin.getBinArgs()[i] = scv.convert(get.queryGet(ARGS, i));
      }
    }
    return bin;
  }
  
  
  public static String encode(Object obj) throws IOException {
    if(obj == null) return null;
    StringByteConverter sbc = new StringByteConverter();
    
    String cont;
    if(obj instanceof CharSequence || obj instanceof Number)
      cont = Objects.toString(obj);
    else if(obj instanceof Class)
      cont = ((Class)obj).getName();
    else
      cont = JsonWriter.objectToJson(obj);
    
    return cont;
  }
  
  
  public static String[] encodeArray(Object[] objs) throws IOException {
    if(objs == null || objs.length < 1) 
      return null;
    
    String[] ss = new String[objs.length];
    for(int i = 0; i < objs.length; i++) {
      if(objs[i] == null) continue;
      ss[i] = encode(objs[i]);
    }
    return ss;
  }
  
  
}
