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

import com.google.gson.JsonObject;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class JWTPayload extends JWTObject {

  public static final String ISS = "iss";
  
  public static final String ISS_VAL = "br.com.bb.disec.micro";
  
  public static final String EXP = "exp";
  
  public static final String SUB = "sub";
  
  public static final String SUB_VAL = "disecMicroAuth";
  
  public static final long DEFAULT_EXP = 600;
  
  
  public JWTPayload() {
    super();
    put(ISS, ISS_VAL);
    put(EXP, DEFAULT_EXP);
    put(SUB, SUB_VAL);
  }
  
  
  public JWTPayload(JWTObject obj) {
    super((obj == null ? new JsonObject() : obj.json()));
    Objects.requireNonNull(get(ISS), "Bad Null Issuer");
    Objects.requireNonNull(get(EXP), "Bad Null Expiration");
    Objects.requireNonNull(get(SUB), "Bad Null Subject");
  }
  
  
  public JWTPayload setIssuer(String iss) {
    Objects.requireNonNull(iss, "Bad Null Issuer");
    put(ISS, iss);
    return this;
  }
  
  
  public JWTPayload setExpiration(long exp) {
    put(EXP, exp);
    return this;
  }
  
  
  public JWTPayload setSubject(String sub) {
    Objects.requireNonNull(sub, "Bad Null Subject");
    put(SUB, sub);
    return this;
  }
  
  
  public String getIssuer() {
    return get(ISS).getAsString();
  }
  
  
  public long getExpiration() {
    return get(EXP).getAsLong();
  }
  
  
  public String getSubject() {
    return get(SUB).getAsString();
  }
  
  
  @Override
  public String toString() {
    return toJson();
  }
  
}
