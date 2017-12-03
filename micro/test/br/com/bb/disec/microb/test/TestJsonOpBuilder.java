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

import br.com.bb.disec.micro.box.def.ChainOp;
import br.com.bb.disec.micro.box.json.JsonOpBuilder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class TestJsonOpBuilder {
  
  private String msg;
  
  public TestJsonOpBuilder(String message) {
    this.msg = message;
  }
  
  public TestJsonOpBuilder setMessage(String message) {
    this.msg = message;
    return this;
  }
  
  public TestJsonOpBuilder say() {
    System.out.println("--> "+ msg+ "!");
    return this;
  }

  
  public static void main(String[] args) {
    String json = new JsonOpBuilder()
        .onClass(TestJsonOpBuilder.class.getName())
        .withArgs("hello").constructor()
        .method("say")
        .set("msg", "world")
        .method("say")
        .method("setMessage", "oh, boy")
        .method("say")
        .get("msg")
        .buildJson();
    System.out.println(json);
    System.out.println(new ChainOp(JsonOpBuilder.fromJson(json).build())
        .execute(TestJsonOpBuilder.class));
  }
  
}
