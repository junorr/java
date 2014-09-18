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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class BBMProgress {

  public static final String 
      KEY_BYTES_PROCESSED = "BYTES_PROCESSED",
      KEY_PERCENT_STRING = "PERCENT_STRING",
      KEY_PERCENT = "PERCENT";
  
  private String percentString;
  
  private long bytes;
  
  private double percent;
  
  private Properties p;
  
  private File file;
  
  
  public BBMProgress(File f) {
    if(f == null)
      throw new IllegalArgumentException(
          "Invalid file: "+ f);
    p = new Properties();
    percentString = "0.0 %";
    percent = 0;
    file = f;
  }
  
  
  public BBMProgress setProgress(long bytes, double percent, String percentString) {
    this.percentString = percentString;
    this.percent = percent;
    this.bytes = bytes;
    return this;
  }


  public String getStringPercent() {
    return percentString;
  }


  public BBMProgress setStringPercent(String percent) {
    this.percentString = percent;
    return this;
  }


  public double getPercent() {
    return percent;
  }


  public BBMProgress setPercent(double percent) {
    this.percent = percent;
    return this;
  }


  public long getBytes() {
    return bytes;
  }


  public BBMProgress setBytes(long bytes) {
    this.bytes = bytes;
    return this;
  }


  public File getFile() {
    return file;
  }


  public BBMProgress setFile(File file) {
    this.file = file;
    return this;
  }
  
  
  public BBMProgress save() {
    p.setProperty(KEY_BYTES_PROCESSED, String.valueOf(bytes));
    p.setProperty(KEY_PERCENT_STRING, percentString);
    p.setProperty(KEY_PERCENT, String.valueOf(percent));
    
    try (FileOutputStream fos = 
        new FileOutputStream(file)) {
      
      p.store(fos, this.getClass().getSimpleName());
      fos.flush();
      return this;
      
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public BBMProgress load() {
    try (FileInputStream fis = 
        new FileInputStream(file)) {
      
      p.load(fis);
      try { percent = Double.parseDouble(p.getProperty(KEY_PERCENT)); }
      catch(NullPointerException | NumberFormatException e) {}
      percentString = p.getProperty(KEY_PERCENT_STRING);
      try { bytes = Long.parseLong(p.getProperty(KEY_BYTES_PROCESSED)); }
      catch(NullPointerException | NumberFormatException e) {}
      return this;
      
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
}
