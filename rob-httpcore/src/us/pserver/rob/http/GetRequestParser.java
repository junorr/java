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

import com.cedarsoftware.util.io.JsonReader;
import us.pserver.rob.channel.*;
import java.io.IOException;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import us.pserver.rob.MethodChain;
import us.pserver.rob.RemoteMethod;
import static us.pserver.rob.http.HttpConsts.*;
import static us.pserver.rob.http.GetConsts.*;
import us.pserver.rob.container.Credentials;


/**
 * Canal de transmissão de objetos através do
 * protocolo HTTP. Implementa o lado servidor
 * da comunicação. O objeto é codificado
 * em hexadecimal e transmitido no corpo
 * da resposta utilizando delimitadores no formato
 * XML.<br/><br/>
 * Canais de comunicação no protocolo HTTP
 * permanecem válidos para apenas um ciclo
 * de leitura e escrita. Após um ciclo o canal 
 * se torna inválido e deve ser fechado.
 * Novas requisições deverão ser efetuadas em
 * novas instâncias de <code>HttpResponseChannel</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class GetRequestParser {
  
  private CryptKey key;
  
  private boolean gzip;
  
  
  public GetRequestParser() {
    gzip = false;
    key = null;
  }
  
  
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  public boolean isGZipCompressed() {
    return gzip;
  }
  
  
  private int extractNumArgs(String meth) {
    if(meth == null || !meth.contains("-"))
      return -1;
    String si = meth.substring(meth.indexOf("-") +1);
    try {
      return Integer.parseInt(si);
    } catch(NumberFormatException e) {
      return -1;
    }
  }
  
  
  private MethodChain createChain(GetRequest get) throws IOException {
    if(get == null) return null;
    
    MethodChain chain = new MethodChain();
    int ims = get.queryKeySize(METHOD);
    
    for(int i = 0; i < ims; i++) {
      String smth = get.queryGet(METHOD, i);
      if(!smth.contains(DASH))
        throw new IOException("[GetRequestParser.createChain( GetRequest )] "
            + "Invalid Method Format. Missing Number of Arguments ["+ smth+ "]");
      
      RemoteMethod rm = new RemoteMethod();
      if(i == 0) {
        rm.forObject(get.queryGet(OBJECT));
      }
      
      int numArgs = extractNumArgs(smth);
      smth = smth.substring(0, smth.indexOf("-"));
      rm.method(smth);
      parseMethodParams(rm, get, numArgs);
      chain.add(rm);
    }//for i
    
    return chain;
  }
  
  
  private void parseMethodParams(RemoteMethod rmt, GetRequest get, int numArgs) throws IOException {
    if(rmt == null || rmt.method() == null 
        || get == null || numArgs < 1)
      return;
    for(int i = 0; i < numArgs; i++) {
      String sarg = get.queryGet(ARGS, i);
      String styp = get.queryGet(TYPES, i);
      Class cls;
        
      try {
        cls = ClassHelper.getClass(styp);
      } catch(ClassNotFoundException e) {
        throw new IOException(e);
      }
        
      rmt.addType(cls);
      rmt.addParam(convertArgument(sarg, cls));
    }//for
  }
  
  
  private void decodeBase64(BinMethodRequest bin, GetRequest get) {
    if(bin == null || get == null) return;
    if(get.queryContains(CRYPT_KEY)
        || (get.queryContains(GZIP)
        && "1".equals(get.queryGet(GZIP)))) {
      Base64ByteCoder bbc = new Base64ByteCoder();
      bin.setBinObject(bbc.decode(bin.getBinObject()));
      bin.setBinMethod(bbc.decode(bin.getBinMethod()));
      if(bin.getBinAuth() != null)
        bin.setBinAuth(bbc.decode(bin.getBinAuth()));
      for(int i = 0; i < bin.getBinTypes().length; i++) {
        bin.getBinTypes()[i] = bbc.decode(bin.getBinTypes()[i]);
        bin.getBinArgs()[i] = bbc.decode(bin.getBinArgs()[i]);
      }
    }
  }
  
  
  private void decodeCrypt(BinMethodRequest bin, GetRequest get) {
    if(bin == null || get == null) return;
    if(get.queryContains(CRYPT_KEY)) {
      key = CryptKey.fromString(get.queryGet(CRYPT_KEY));
      CryptByteCoder cbc = new CryptByteCoder(key);
      bin.setBinObject(cbc.decode(bin.getBinObject()));
      bin.setBinMethod(cbc.decode(bin.getBinMethod()));
      if(bin.getBinAuth() != null)
        bin.setBinAuth(cbc.decode(bin.getBinAuth()));
      for(int i = 0; i < bin.getBinTypes().length; i++) {
        bin.getBinTypes()[i] = cbc.decode(bin.getBinTypes()[i]);
        bin.getBinArgs()[i] = cbc.decode(bin.getBinArgs()[i]);
      }
    }
  }
  
  
  private void decodeGZip(BinMethodRequest bin, GetRequest get) {
    if(bin == null || get == null) return;
    if(get.queryMap().containsKey(GZIP)
        && get.queryGet(GZIP).equals("1")) {
      gzip = true;
      GZipByteCoder gbc = new GZipByteCoder();
      bin.setBinObject(gbc.decode(bin.getBinObject()));
      bin.setBinMethod(gbc.decode(bin.getBinMethod()));
      if(bin.getBinAuth() != null)
        bin.setBinAuth(gbc.decode(bin.getBinAuth()));
      for(int i = 0; i < bin.getBinTypes().length; i++) {
        bin.getBinTypes()[i] = gbc.decode(bin.getBinTypes()[i]);
        bin.getBinArgs()[i] = gbc.decode(bin.getBinArgs()[i]);
      }
    }
  }
  
  
  private RemoteMethod createRemote(GetRequest get) throws IOException {
    if(get == null) return null;
    
    RemoteMethod rmt = new RemoteMethod()
        .forObject(get.queryGet(OBJECT));
    
    String smth = get.queryGet(METHOD);
    if(smth.contains(DASH))
      smth = smth.substring(0, smth.indexOf("-"));
    rmt.method(smth);
    
    if(get.queryContains(AUTH)) {
      Credentials cr = (Credentials) 
          JsonReader.jsonToJava(get.queryGet(AUTH));
      rmt.credentials(cr);
    }
    
    addArgsTypes(rmt, get);
    return rmt;
  }
  
  
  private void addArgsTypes(RemoteMethod rm, GetRequest get) throws IOException {
    if(rm == null || get == null)
      return;
    
    for(int i = 0; i < get.queryKeySize(TYPES); i++) {
      Class cls = null;
      try {
        cls = ClassHelper.getClass(get.queryGet(TYPES, i));
      } catch(ClassNotFoundException e) {
        throw new IOException(e);
      }
      rm.addType(cls);
      rm.addParam(convertArgument(get.queryGet(ARGS, i), cls));
    }
  }
  
  
  private Object convertArgument(String sarg, Class cls) throws IOException {
    if(sarg == null || cls == null)
      return null;
      if(ClassHelper.isNumber(cls)) {
        return convertNumber(Double.parseDouble(sarg), cls);
      }
      else if(String.class.isAssignableFrom(cls)) {
        return sarg;
      }
      else {
        return JsonReader.jsonToJava(sarg);
      }
  }
  
  
  private Object convertNumber(Double n, Class cls) {
    if(n == null || cls == null) return n;
    if(int.class.isAssignableFrom(cls)
        || Integer.class.isAssignableFrom(cls)) {
      return n.intValue();
    }
    else if(short.class.isAssignableFrom(cls)
        || Short.class.isAssignableFrom(cls)) {
      return n.shortValue();
    }
    else if(byte.class.isAssignableFrom(cls) 
        || Byte.class.isAssignableFrom(cls)) {
      return n.byteValue();
    }
    else if(long.class.isAssignableFrom(cls)
        || Long.class.isAssignableFrom(cls)) {
      return n.longValue();
    }
    else if(float.class.isAssignableFrom(cls)
        || Float.class.isAssignableFrom(cls)) {
      return n.floatValue();
    }
    else return n;
  }
  
  
  private GetRequest decodeRequest(GetRequest get) {
    if(get == null) return null;
    StringByteConverter scv = new StringByteConverter();
    
    BinMethodRequest bin = GetRequestUtils.toBinRequest(get);
    decodeBase64(bin, get);
    decodeCrypt(bin, get);
    decodeGZip(bin, get);
    get = GetRequestUtils.toGetRequest(bin, get.getFullAddress());
    return GetRequest.parse(get.buildRequest());
  }
  
  
  public Transport parse(GetRequest request) throws IOException {
    if(request == null) return null;

    request = decodeRequest(request);
    Transport trp = null;
    
    if(request.queryKeySize(METHOD) > 1)
      trp = new Transport(createChain(request));
    else
      trp = new Transport(createRemote(request));
    return trp;
  }
  
}
