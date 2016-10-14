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

package br.com.bb.disec.microb.test;

import br.com.bb.disec.micro.jwt.JWT;
import br.com.bb.disec.micro.jwt.JWTHeader;
import br.com.bb.disec.micro.jwt.JWTKey;
import br.com.bb.disec.micro.jwt.JWTPayload;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class TestJWT {

  
  public static void main(String[] args) {
    JWTKey key = JWTKey.fromPlainString("juno");
    System.out.println("* key: "+ key.toString());
    JWTHeader hdr = new JWTHeader();
    System.out.println("* header: "+ hdr.toJson());
    System.out.println("* parsed: "+ JWTHeader.fromBase64(hdr.encodeBase64()));
    System.out.println();
    
    JWTPayload pld = new JWTPayload();
    pld.put("name", "Juno")
        .put("chave", "F6036477")
        .put("uor", 347172);
    System.out.println("* payload: "+ pld.toJson());
    System.out.println();
    
    JWT jwt = new JWT(hdr, pld, key);
    String token = jwt.createToken();
    System.out.println("* jwt.json: "+ jwt.toJson());
    System.out.println("* jwt.sign: "+ jwt.encodeBase64());
    System.out.println();
    
    jwt = JWT.fromBase64(token);
    System.out.println("* jwt.json: "+ jwt.toJson());
    System.out.println("* jwt.tokn: "+ jwt.encodeBase64());
    System.out.println("* jwt.vsig: "+ jwt.verifySign(key));
  }
  
}
