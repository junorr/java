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

package com.jpower.sys;

import java.io.Serializable;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/08/2012
 */
public class MemoryParser implements Serializable, Parser<Memory> {
  
  private String line;
  
  private Memory mem;
  
  private TextFieldParser tp;
  
  
  public MemoryParser() {
    line = "";
    tp = new TextFieldParser();
    mem = new Memory();
  }
  
  
  @Override
  public Memory get() {
    return mem;
  }
  

  @Override
  public Memory parse(String ln) {
    if(ln == null || ln.trim().isEmpty())
      return null;
    
    tp.setLine(ln).parse(" ", "\n");
    while(tp.peak() != null && tp.doubleField(0) < 0) tp.pop();
    mem.setTotal((int) tp.doublePop());
    mem.setUsed((int) tp.doublePop());
    mem.setFree((int) tp.doublePop());
    tp.discard(2);
    mem.setCached((int) tp.doublePop());
    
    return mem;
  }
  
  
  public static void main(String[] args) {
    MemoryParser mp = new MemoryParser();
    String ln = "             total       used       free     shared    buffers     cached\n"
              + "Mem:          5880       2343       3536          0         89        911\n"
              + "-/+ buffers/cache:       1342       4538\n"
              + "Swap:          242          0        242\n";
    System.out.println(mp.parse(ln));
  }
  
}
