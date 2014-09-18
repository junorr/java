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

package com.jpower.bbm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 15/04/2013
 */
public class ProdutoCountPool {

  private LinkedList<ProdutoCount> pool;
  
  
  public ProdutoCountPool() {
    pool = new LinkedList<>();
  }
  
  
  public ProdutoCountPool inc(Analise a) {
    if(a == null || a.getProduto() == null
        || a.getProduto().getPrmod() == null
        || a.getInstancia() == null
        || a.getValor() <= 0) return this;
    
    ProdutoCount pc = this.find(a);
    if(pc == null) {
      pc = new ProdutoCount(a);
      pool.add(pc);
    }
    else pc.inc(a.getValor());
    return this;
  }
  
  
  public List<ProdutoCount> getPool() {
    return pool;
  }
  
  
  public int size() {
    return pool.size();
  }
  
  
  public boolean isEmpty() {
    return pool.isEmpty();
  }
  
  
  public ProdutoCount find(Analise a) {
    if(a == null || a.getProduto() == null
        || a.getProduto().getPrmod() == null
        || a.getInstancia() == null)
      return null;
    for(ProdutoCount pc : pool) {
      if(pc.equals(a))
        return pc;
    }
    return null;
  }
  
  
  public ProdutoCountPool write(ProdutoCountWriter pw) {
    if(pw != null && !pool.isEmpty()) {
      for(ProdutoCount pc : pool) {
        pw.write(pc);
      }
    }
    return this;
  }
  
  
  public boolean load(File f) {
    if(f == null || !f.exists())
      return false;
    
    pool.clear();
    BBMReader reader = new BBMReader(f);
    String line = reader.read();
    while(line != null) {
      ProdutoCount pc = new ProdutoCount();
      if(pc.fromString(line))
        pool.add(pc);
      line = reader.read();
    }
    return !pool.isEmpty();
  }
  
}
