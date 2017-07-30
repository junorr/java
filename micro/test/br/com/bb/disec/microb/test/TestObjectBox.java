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

import br.com.bb.disec.micro.box.ObjectBox;
import br.com.bb.disec.micro.box.OpBuilder;
import br.com.bb.disec.micro.box.Operation;
import br.com.bb.disec.micro.box.def.ChainOp;
import java.nio.file.Paths;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class TestObjectBox {

  
  public static void main(String[] args) {
    ObjectBox box = ObjectBox.of(Paths.get("D:/java/testObjectBox/dist/"));
    Operation op = new OpBuilder()
        .withArgs("Hello World").constructor()
        .method("say")
        .build();
    Operation op2 = new OpBuilder()
        .method("setMessage", "Oh, Boy")
        .method("say")
        .build();
    System.out.println(box.execute(new ChainOp("testobjectbox.Message", op)));
    System.out.println(box.execute(new ChainOp("testobjectbox.Message", op2)));
  }
  
}
