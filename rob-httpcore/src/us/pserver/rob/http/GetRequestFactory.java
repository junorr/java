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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.rob.MethodChain;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.channel.Transport;
import static us.pserver.rob.http.GetConsts.*;
import static us.pserver.rob.http.HttpConsts.*;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2015
 */
public class GetRequestFactory {
  
  private boolean crypt, gzip;
  
  private CryptAlgorithm algo;
  
  private CryptKey key;
  
  private final StringByteConverter scv;
  
  private final String address;
  
  
  public GetRequestFactory(String address) {
    this.address = address;
    key = null;
    crypt = false;
    gzip = false;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    scv = new StringByteConverter();
  }
  
  
  public GetRequestFactory setEncryptionEnabled(boolean enabled) {
    crypt = enabled;
    return this;
  }
  
  
  public boolean isEncryptionEnabled() {
    return crypt;
  }
  
  
  public GetRequestFactory setGZipCompressionEnabled(boolean enabled) {
    gzip = enabled;
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  public GetRequestFactory setCryptAlgorithm(CryptAlgorithm ca) {
    nullarg(CryptAlgorithm.class, ca);
    algo = ca;
    return this;
  }
  
  
  public CryptAlgorithm getCryptAlgorithm() {
    return algo;
  }
  
  
  private void encodeGZip(BinMethodRequest bin) {
    if(bin == null) return;
    GZipByteCoder gbc = new GZipByteCoder();
    bin.setBinObject(gbc.encode(bin.getBinObject()));
    bin.setBinMethod(gbc.encode(bin.getBinMethod()));
    if(bin.getBinAuth() != null)
      bin.setBinAuth(gbc.encode(bin.getBinAuth()));
    if(bin.getBinTypes() == null || bin.getBinTypes().length == 0)
      return;
    for(int i = 0; i < bin.getBinTypes().length; i++) {
      bin.getBinTypes()[i] = gbc.encode(bin.getBinTypes()[i]);
      bin.getBinArgs()[i] = gbc.encode(bin.getBinArgs()[i]);
    }
  }
  
  
  private void encodeCrypt(BinMethodRequest bin) {
    if(bin == null) return;
    key = CryptKey.createRandomKey(algo);
    CryptByteCoder cbc = new CryptByteCoder(key);
    bin.setBinObject(cbc.encode(bin.getBinObject()));
    bin.setBinMethod(cbc.encode(bin.getBinMethod()));
    if(bin.getBinAuth() != null)
      bin.setBinAuth(cbc.encode(bin.getBinAuth()));
    if(bin.getBinTypes() == null || bin.getBinTypes().length == 0)
      return;
    for(int i = 0; i < bin.getBinTypes().length; i++) {
      bin.getBinTypes()[i] = cbc.encode(bin.getBinTypes()[i]);
      bin.getBinArgs()[i] = cbc.encode(bin.getBinArgs()[i]);
    }
  }
  
  
  private void encodeBase64(BinMethodRequest bin) {
    if(bin == null) return;
    Base64ByteCoder bbc = new Base64ByteCoder();
    bin.setBinObject(bbc.encode(bin.getBinObject()));
    bin.setBinMethod(bbc.encode(bin.getBinMethod()));
    if(bin.getBinAuth() != null)
      bin.setBinAuth(bbc.encode(bin.getBinAuth()));
    if(bin.getBinTypes() == null || bin.getBinTypes().length == 0)
      return;
    for(int i = 0; i < bin.getBinTypes().length; i++) {
      bin.getBinTypes()[i] = bbc.encode(bin.getBinTypes()[i]);
      bin.getBinArgs()[i] = bbc.encode(bin.getBinArgs()[i]);
    }
  }
  
  
  public void encode(BinMethodRequest bin) {
    if(bin == null) return;
    if(gzip) encodeGZip(bin);
    if(crypt) encodeCrypt(bin);
    if(gzip || crypt) encodeBase64(bin);
  }
  
  
  private GetRequest toGetRequest(RemoteMethod rm) throws IOException {
    if(rm == null) return null;
    GetRequest get = new GetRequest(address);
    get.query(OBJECT, rm.objectName());
    get.query(METHOD, rm.method());
    if(rm.credentials() != null) {
      get.query(AUTH, GetRequestUtils.encode(rm.credentials()));
    }
    if(!rm.types().isEmpty()) {
      get.queryList(TYPES, GetRequestUtils.encodeArray(rm.typesArray()));
      get.queryList(ARGS, GetRequestUtils.encodeArray(rm.params().toArray()));
    }
    return get;
  }
  
  
  private GetRequest toGetRequest(MethodChain chain) throws IOException {
    if(chain == null || chain.methods().isEmpty()) 
      return null;
    GetRequest get = new GetRequest(address);
    get.query(OBJECT, GetRequestUtils.encode(chain.current().objectName()));
    List<String> meths = new ArrayList<>();
    List<Class> types = new ArrayList<>();
    List args = new ArrayList();
    
    for(RemoteMethod rm : chain.methods()) {
      String meth = rm.method() + DASH + rm.types().size();
      if(!rm.types().isEmpty()) {
        types.addAll(rm.types());
        args.addAll(rm.params());
      }
      meths.add(meth);
    }
    get.queryList(METHOD, GetRequestUtils.encodeArray(meths.toArray()));
    get.queryList(TYPES, GetRequestUtils.encodeArray(types.toArray()));
    get.queryList(ARGS, GetRequestUtils.encodeArray(args.toArray()));
    return get;
  }
  
  
  public GetRequest create(RemoteMethod rmt) throws IOException {
    if(rmt == null) return null;
    GetRequest get = toGetRequest(rmt);
    if(gzip || crypt) {
      BinMethodRequest bin = GetRequestUtils.toBinRequest(get);
      encode(bin);
      get = GetRequestUtils.toGetRequest(bin, address);
      if(gzip) get.query(GZIP, 1);
      if(crypt) get.query(CRYPT_KEY, key);
    }
    return get;
  }

  
  public GetRequest create(MethodChain chain) throws IOException {
    if(chain == null || chain.methods().isEmpty()) 
      return null;
    GetRequest get = toGetRequest(chain);
    if(gzip || crypt) {
      BinMethodRequest bin = GetRequestUtils.toBinRequest(get);
      encode(bin);
      get = GetRequestUtils.toGetRequest(bin, address);
      if(gzip) get.query(GZIP, 1);
      if(crypt) get.query(CRYPT_KEY, key);
    }
    return get;
  }

  
  public static void main(String[] args) throws IOException {
    GetRequestFactory fac = new GetRequestFactory("http://localhost:9255/")
        //.setGZipCompressionEnabled(true)
        .setEncryptionEnabled(true);
    MethodChain chain = new MethodChain();
    chain.add("str", "concat").types(String.class).params("text");
    chain.add("toString");
    GetRequest get = fac.create(chain);
    System.out.println("* MethodChain : "+ chain);
    System.out.println("* GetRequest  : "+ get.buildQueryAddress());
    
    GetRequestParser par = new GetRequestParser();
    Transport trp = par.parse(get);
    System.out.println("* Transport   : "+ trp);
  }
  
}
