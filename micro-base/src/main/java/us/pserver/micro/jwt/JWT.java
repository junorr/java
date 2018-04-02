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

package us.pserver.micro.jwt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class JWT implements JWTElement {

  private final JWTHeader header;

  private final JWTPayload payload;

  private final JWTKey key;


  public JWT(JWTHeader hdr, JWTPayload pld, JWTKey key) {
    Objects.requireNonNull(hdr, "Bad Null JWTHeader");
    Objects.requireNonNull(pld, "Bad Null JWTPayload");
    Objects.requireNonNull(key, "Bad Null JWTKey");
    this.header = hdr;
    this.payload = pld;
    this.key = key;
  }


  public JWTHeader getHeader() {
    return header;
  }


  public JWTPayload getPayload() {
    return payload;
  }
  
  
  public JWTKey getSignature() {
    return key;
  }
  
  
  public String createToken() {
    return encodeBase64();
  }
  
  
  public boolean isExpired() {
    return header.getCreatedInstant()
        .plusSeconds(payload.getExpiration())
        .isBefore(Instant.now());
  }


  @Override
  public String toJson() {
    JsonObject json = new JsonObject();
    json.add("header", header.json());
    json.add("payload", payload.json());
    json.addProperty("key", key.toString());
    return new Gson().toJson(json);
  }
  
  
  @Override
  public String encodeBase64() {
    return new StringBuilder()
        .append(header.encodeBase64())
        .append(".")
        .append(payload.encodeBase64())
        .append(".")
        .append(key.sign(header, payload))
        .toString();
  }
  
  
  public boolean verifySign(JWTKey jsi) {
    return this.key.equals(jsi.sign(header, payload));
  }
  
  
  public static JWT fromBase64(String b64) {
    Objects.requireNonNull(b64, "Bad Null JWT Base64 Encoded");
    if(!b64.contains(".")) {
      throw new IllegalArgumentException("Missing JWT dots separation");
    }
    String[] tkns = b64.split("\\.");
    if(tkns.length < 3) {
      throw new IllegalArgumentException("Incomplete Json Web Token (slices="+ tkns.length+ ")");
    }
    JWTHeader hdr = JWTHeader.fromBase64(tkns[0]);
    JWTPayload pld = new JWTPayload(JWTObject.fromBase64(tkns[1]));
    JWTKey key = JWTKey.fromBase64(tkns[2]);
    return new JWT(hdr, pld, key);
  }
  
  
  public static byte[] str2bytes(String str) {
    Objects.requireNonNull(str, "Bad Null String");
    return str.getBytes(StandardCharsets.UTF_8);
  }
  
  
  public static String bytes2str(byte[] bs) {
    Objects.requireNonNull(bs, "Bad Null Byte Array");
    return new String(bs, StandardCharsets.UTF_8);
  }
    
}
