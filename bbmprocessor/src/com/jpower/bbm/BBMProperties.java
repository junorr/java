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
 * @version 0.0 - 15/04/2013
 */
public class BBMProperties {
  
  public static final String 
      
      KEY_BBM_FILE = "BBM_FILE",
      
      KEY_PRODUTOS_FILE = "PRODUTOS_FILE",
      
      KEY_START_DATE = "START_DATE",
      
      KEY_END_DATE = "END_DATE",
      
      KEY_LOG_FILE = "LOG_FILE",
      
      KEY_PROGRESS_FILE = "PROGRESS_FILE",
      
      KEY_PREFIX_FILE = "PREFIX_FILE";
  
  
  private String bbmfile,
      prodfile,
      startdate,
      enddate,
      logfile,
      progfile,
      prefixfile;
  
  private File file;
  
  private Properties prop;
  
  
  public BBMProperties(File f) {
    if(f == null)
      throw new IllegalArgumentException(
          "Invalid File: "+ f);
    
    bbmfile = null;
    prodfile = null;
    startdate = null;
    enddate = null;
    logfile = null;
    progfile = null;
    prefixfile = null;
    file = f;
    prop = new Properties();
    if(file.exists())
      this.load();
  }


  public String getBbmfile() {
    return bbmfile;
  }


  public BBMProperties setBbmfile(String bbmfile) {
    this.bbmfile = bbmfile;
    return this;
  }


  public String getProdfile() {
    return prodfile;
  }


  public BBMProperties setProdfile(String prodfile) {
    this.prodfile = prodfile;
    return this;
  }


  public String getStartdate() {
    return startdate;
  }


  public BBMProperties setStartdate(String startdate) {
    this.startdate = startdate;
    return this;
  }


  public String getEnddate() {
    return enddate;
  }


  public BBMProperties setEnddate(String enddate) {
    this.enddate = enddate;
    return this;
  }


  public String getLogfile() {
    return logfile;
  }


  public BBMProperties setLogfile(String logfile) {
    this.logfile = logfile;
    return this;
  }


  public String getProgfile() {
    return progfile;
  }


  public BBMProperties setProgfile(String progfile) {
    this.progfile = progfile;
    return this;
  }


  public String getPrefixfile() {
    return prefixfile;
  }


  public BBMProperties setPrefixfile(String prefixfile) {
    this.prefixfile = prefixfile;
    return this;
  }
  
  
  public BBMProperties save() {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      prop.setProperty(KEY_BBM_FILE, bbmfile);
      prop.setProperty(KEY_END_DATE, enddate);
      prop.setProperty(KEY_LOG_FILE, logfile);
      prop.setProperty(KEY_PRODUTOS_FILE, prodfile);
      prop.setProperty(KEY_PROGRESS_FILE, progfile);
      prop.setProperty(KEY_START_DATE, startdate);
      prop.setProperty(KEY_PREFIX_FILE, prefixfile);
      prop.store(fos, "BBMProperties");
      return this;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public BBMProperties load() {
    try (FileInputStream fis = new FileInputStream(file)) {
      prop.load(fis);
      bbmfile = prop.getProperty(KEY_BBM_FILE);
      enddate = prop.getProperty(KEY_END_DATE);
      logfile = prop.getProperty(KEY_LOG_FILE);
      prodfile = prop.getProperty(KEY_PRODUTOS_FILE);
      progfile = prop.getProperty(KEY_PROGRESS_FILE);
      startdate = prop.getProperty(KEY_START_DATE);
      prefixfile = prop.getProperty(KEY_PREFIX_FILE);
      return this;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }

}
