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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class JWTHeader extends JWTObject {

  public static final String ALG = "alg";
  
  public static final String TYPE = "typ";
  
  public static final String CREATED = "cre";
  
  public static final String HS256 = "HS256";
  
  public static final String JWT = "JWT";
  
  
  public JWTHeader() {
    put(ALG, HS256);
    put(TYPE, JWT);
    put(CREATED, Instant.now().getEpochSecond());
  }


  public JWTHeader(String alg, String type, long created) {
    this.setAlgorithm(alg)
        .setType(type)
        .setCreated(created);
  }
  
  
  public String getAlgorithm() {
    return get(ALG).getAsString();
  }
  
  
  public String getType() {
    return get(TYPE).getAsString();
  }
  
  
  public Instant getCreatedInstant() {
    return Instant.ofEpochSecond(this.getCreated());
  }
  
  
  public long getCreated() {
    return get(CREATED).getAsLong();
  }
  
  
  public JWTHeader setAlgorithm(String alg) {
    Objects.requireNonNull(alg, "Bad Null Algorithm");
    put(ALG, alg);
    return this;
  }
  
  
  public JWTHeader setType(String typ) {
    Objects.requireNonNull(typ, "Bad Null Type");
    put(TYPE, typ);
    return this;
  }
  
  
  public JWTHeader setCreated(long cre) {
    put(CREATED, cre);
    return this;
  }
  
  
  public JWTHeader setCreated(Instant ins) {
    Objects.requireNonNull(ins, "Bad Null Creation Instant");
    put(CREATED, ins.getEpochSecond());
    return this;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(getAlgorithm());
    hash = 97 * hash + Objects.hashCode(getType());
    long created = this.getCreated();
    hash = 97 * hash + (int) (created ^ (created >>> 32));
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final JWTHeader other = (JWTHeader) obj;
    if(getCreated() != other.getCreated()) {
      return false;
    }
    if(!Objects.equals(getAlgorithm(), other.getAlgorithm())) {
      return false;
    }
    if(!Objects.equals(getType(), other.getType())) {
      return false;
    }
    return true;
  }
  
  
  @Override
  public String toString() {
    return "JWTHeader" + toJson();
  }
  
  
  @Override
  public String encodeBase64() {
    return Base64.getEncoder().encodeToString(
        toJson().getBytes(StandardCharsets.UTF_8)
    );
  }
  
  
  public static JWTHeader fromBase64(String b64) {
    JWTObject obj = JWTObject.fromBase64(b64);
    Objects.requireNonNull(obj.get(ALG), "Bad Null Algorithm");
    Objects.requireNonNull(obj.get(TYPE), "Bad Null Type");
    Objects.requireNonNull(obj.get(CREATED), "Bad Null Creation Time");
    return new JWTHeader(
        obj.get(ALG).getAsString(), 
        obj.get(TYPE).getAsString(), 
        obj.get(CREATED).getAsLong()
    );
  }
  
}
