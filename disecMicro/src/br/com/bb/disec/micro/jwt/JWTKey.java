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

package br.com.bb.disec.micro.jwt;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class JWTKey implements JWTElement {

  private final byte[] key;
  
  
  public JWTKey(byte[] key) {
    Objects.requireNonNull(key, "Bad Null Key Array");
    this.key = key;
  }
  
  
  public byte[] getKey() {
    return key;
  }

  
  public JWTKey sign(JWTHeader jhd, JWTPayload jpl) {
    Objects.requireNonNull(jhd, "Bad Null JWTHeader");
    Objects.requireNonNull(jpl, "Bad Null JWPayload");
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      sha.update(JWT.str2bytes(
          jhd.encodeBase64() + "." + jpl.encodeBase64())
      );
      return new JWTKey(sha.digest(key));
    }
    catch(Exception e) {
      throw new RuntimeException(
          "["+ e.getClass().getSimpleName()
              + "]-> "+ e.getMessage()
      );
    }
  }
  
  
  @Override
  public String toJson() {
    return "\""+ toString()+ "\"";
  }
  
  
  @Override
  public String toString() {
    return encodeBase64();
  }


  @Override
  public String encodeBase64() {
    return Base64.getEncoder().encodeToString(key);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Arrays.hashCode(this.key);
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
    final JWTKey other = (JWTKey) obj;
    if (!Arrays.equals(this.key, other.key)) {
      return false;
    }
    return true;
  }
  
  
  public static JWTKey generate() {
    ByteBuffer seed = ByteBuffer.allocate(Long.BYTES);
    seed.putLong((long) (Math.random() 
        * Instant.now().getEpochSecond()));
    return new JWTKey(DigestUtils.sha256(seed.array()));
  }
  
  
  public static JWTKey fromBase64(String b64) {
    Objects.requireNonNull(b64, "Bad Null Base64 Key");
    return new JWTKey(Base64.getDecoder().decode(b64));
  }
  
  
  public static JWTKey fromPlainString(String str) {
    Objects.requireNonNull(str, "Bad Null String Key");
    return new JWTKey(JWT.str2bytes(str));
  }

}
