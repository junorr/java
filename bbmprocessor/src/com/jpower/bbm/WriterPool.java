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

import com.jpower.date.SimpleDate;
import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class WriterPool {
  
  private class WriterInfo {
    protected int mes;
    protected int ano;
    protected AnaliseWriter wrt;
    public boolean equals(SimpleDate d) {
      if(d == null) return false;
      if(d.month() != mes)
        return false;
      if(d.year() != ano)
        return false;
      return true;
    }
  }
  
  private LinkedList<WriterInfo> pool;
  
  private File dir;
  
  private String filepref;
  
  
  public WriterPool(File directory, String filePrefix) {
    if(directory == null || !directory.exists() 
        || !directory.isDirectory())
      throw new IllegalArgumentException(
          "Invalid directory: "+ directory);
    
    if(filePrefix == null || filePrefix.trim().isEmpty())
      throw new IllegalArgumentException(
          "Invalid file prefix: "+ filePrefix);
    
    pool = new LinkedList<>();
    dir = directory;
    filepref = filePrefix;
  }
  
  
  private AnaliseWriter createWriter(SimpleDate date) {
    if(date == null) return null;
    WriterInfo i = new WriterInfo();
    i.ano = date.year();
    i.mes = date.month();
    String sufix = "_"
        + String.valueOf(i.ano) + "-"
        + String.valueOf(i.mes) + ".csv";
    i.wrt = new AnaliseWriter(
        new File(dir, filepref + sufix));
    pool.add(i);
    return i.wrt;
  }
  
  
  private AnaliseWriter find(SimpleDate date) {
    if(date == null || pool.isEmpty())
      return null;
    for(WriterInfo i : pool) {
      if(i.equals(date))
        return i.wrt;
    }
    return null;
  }
  
  
  public AnaliseWriter getWriter(SimpleDate date) {
    if(date == null) return null;
    AnaliseWriter w = this.find(date);
    if(w == null)
      w = this.createWriter(date);
    return w;
  }
  
  
  public WriterPool close() {
    if(!pool.isEmpty()) {
      for(WriterInfo i : pool) {
        i.wrt.close();
      }
      pool.clear();
    }
    return this;
  }

}
