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

import br.com.bb.disec.micro.refl.ObjectVolume;
import br.com.bb.disec.micro.refl.Operation;
import br.com.bb.disec.micro.refl.OperationBuilder;
import br.com.bb.disec.micro.refl.OperationType;
import br.com.bb.disec.micro.refl.impl.ObjectVolumeImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/07/2017
 */
public class TestObjectBox {

  private String field;
  
  public TestObjectBox(String str) {
    field = str;
  }
  
  public String getField() {
    return field;
  }
  
  public void say() {
    System.out.println("* The field is "+ (field == null ? "empty" : "filled") + " (" + field + ").");
  }
  
  public static void sayHello() {
    new TestObjectBox("Hello").say();
  }
  
  
  public static void main(String[] args) {
    TestObjectBox.sayHello();
    TestObjectBox tox = new TestObjectBox(null);
    tox.say();
    Operation op = new OperationBuilder(OperationType.SET_FIELD)
        .name("field")
        .addArg("World")
        .build();
    ObjectVolume vol = new ObjectVolumeImpl(tox);
    vol.execute(op);
    op = new OperationBuilder(OperationType.INVOKE_METHOD)
        .name("say")
        .build();
    vol.execute(op);
  }
  
}
