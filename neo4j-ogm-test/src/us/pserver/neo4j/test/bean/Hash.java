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

package us.pserver.neo4j.test.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Hash {

  private final String hash;
  
  private Hash() {
    hash = null;
  }
  
  private Hash(String hash) {
    this.hash = NotNull.of(hash).getOrFail("Bad null hash");
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(this.hash);
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
    final Hash other = (Hash) obj;
    return Objects.equals(this.hash, other.hash);
  }


  @Override
  public String toString() {
    return hash;
  }
  
  
  public static Hash of(String str) {
    return Builder.createHash(str);
  }
  
  
  
  static class Builder {
  
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static MessageDigest getSHA1Digest() {
      try {
        return MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException(ex.toString(), ex);
      }
    }

    private static String bytesToHex(byte[] bytes) {
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
          int v = bytes[j] & 0xFF;
          hexChars[j * 2] = hexArray[v >>> 4];
          hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
    }

    public static Hash createHash(String str) {
      return new Hash(bytesToHex(getSHA1Digest().digest(new UTF8String(str).getBytes())));
    }
    
  }
  
}
