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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/04/2013
 */
public class PrefixPool {
  
  private BBMReader reader;
  
  private LinkedList<Integer> pool;
  
  
  public PrefixPool() {
    reader = new BBMReader();
    pool = new LinkedList<>();
  }
  
  
  public PrefixPool(File f) {
    this();
    if(f == null || !f.exists() || !f.isFile())
      throw new IllegalArgumentException(
          "Invalid prefix file: "+ f);
    reader.setFile(f);
  }
  
  
  public PrefixPool setFile(File f) {
    if(f != null && f.exists() && f.isFile())
      reader.setFile(f);
    return this;
  }
  
  
  public boolean isEmpty() {
    return pool.isEmpty();
  }
  
  
  public PrefixPool load() {
    String line = reader.read();
    while(line != null) {
      try {
        pool.add(Integer.parseInt(line));
      } catch(NumberFormatException e) {}
      line = reader.read();
    }
    return this;
  }
  
  
  public boolean contains(int pref) {
    if(pool.isEmpty()) return false;
    for(int p : pool) {
      if(p == pref) return true;
    }
    return false;
  }

}
