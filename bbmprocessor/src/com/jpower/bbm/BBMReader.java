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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class BBMReader {
  
  private File file;
  
  private BufferedReader reader;
  
  private DecimalFormat df;
  
  private long total;
  
  private long inc;
  
  
  public BBMReader() {
    file = null;
    reader = null;
    total = inc = 0;
    df = new DecimalFormat("0.0");
  }
  
  
  public BBMReader(File f) {
    if(f == null || !f.exists())
      throw new IllegalArgumentException(
          "Invalid File: "+ f);
    file = f;
    reader = null;
    total = inc = 0;
    df = new DecimalFormat("0.0");
  }


  public File getFile() {
    return file;
  }


  public BBMReader setFile(File file) {
    this.file = file;
    return this;
  }


  public BufferedReader getReader() {
    return reader;
  }
  
  
  public BBMReader setInc(long inc) {
    this.inc = inc;
    return this;
  }
  
  
  public long getInc() {
    return inc;
  }
  
  
  public String read() {
    if(file == null)
      throw new IllegalStateException(
          "Invalid File: "+ file);
    try {
      if(reader == null) {
        reader = new BufferedReader(new FileReader(file));
        if(inc != 0) reader.skip(inc);
        total = file.length();
      }
      String s = reader.readLine();
      if(s != null)
        inc += s.getBytes().length + 1;
      return s;
      
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public String getStringPercent() {
    return df.format(getPercent() * 100.0) + " %";
  }
  
  
  public double getPercent() {
    return ((double)inc) / ((double)total);
  }
  
  
  public BBMReader close() {
    try {
      reader.close();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

}
