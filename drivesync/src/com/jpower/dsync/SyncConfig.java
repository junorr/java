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

package com.jpower.dsync;

import com.jpower.conf.Config;
import com.jpower.date.SimpleDate;
import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 06/05/2013
 */
public class SyncConfig {

  public static final String
      
      KEY_SOURCE = "SOURCE",
      
      KEY_TARGET = "TARGET",
      
      KEY_UPDATE_TARGET = "UPDATE.SOURCE",
      
      KEY_AUTO_SYNC = "AUTO.SYNC",
      
      KEY_SHOW_GUI = "SHOW.GUI",
      
      KEY_PRINT_CONSOLE = "PRINT.CONSOLE",
      
      KEY_START_EVENT = "START.EVENT",
      
      KEY_START_DATE = "START.DATE",
      
      KEY_REPEAT_INTERVAL = "REPEAT.INTERVAL",
      
      KEY_MOD_FILE = "MOD.FILE";
  
  
  public static final String
      
      COMMENT = "DriveSync Configurations:\n"
      + "  * SOURCE:=/path/to/source/\n"
      + "    Source files path to sync.\n"
      + "  * TARGET:=/path/to/target/\n"
      + "    Destination path of sync.\n"
      + "  * START.EVENT:={date|modfile}\n"
      + "    Start event of synchronization.\n"
      + "  * START.DATE:=[dd/MM/yyyy hh:mm:ss]\n"
      + "    Date to start sync [optional].\n"
      + "  * REPEAT.INTERVAL:=[N {SECOND|MINUTE|HOUR|DAY|MONTH|YEAR}]\n"
      + "    Interval to repeat sync, where 'N' is a valid positive number.\n"
      + "  * MOD.FILE:=[/path/to/some/file]\n"
      + "    A file that when detected, starts the synchronization.\n"
      + "  * UPDATE.SOURCE:={true|false}\n"
      + "    Update the source when target has newer files.\n"
      + "  * AUTO.SYNC:={true|false}\n"
      + "    Dont ask to confirm sync on start.\n"
      + "  * SHOW.GUI:={true|false}\n"
      + "    Show progress window.\n"
      + "  * PRINT.CONSOLE:={true|false}\n"
      + "    Print progress to console standard output.";
  
  
  private Config conf;
  
  private SyncOptions opt;
  
  
  public SyncConfig(File config) {
    if(config == null)
      throw new IllegalArgumentException(
          "Invalid config file: "+ config);
    conf = new Config(config);
    conf.setComment(COMMENT);
    opt = new SyncOptions();
    if(config.exists()) {
      load();
    } else {
      save();
    }
  }
  
  
  public SyncConfig clear() {
    opt = new SyncOptions();
    return this;
  }
  
  
  public SyncOptions getSyncOptions() {
    return opt;
  }
  
  
  public void setSyncOptions(SyncOptions so) {
    if(so != null)
      opt = so;
  }
  
  
  public boolean save() {
    conf.clear();
    conf.setComment(COMMENT);
    conf.put(KEY_SOURCE, opt.getSource());
    conf.put(KEY_TARGET, opt.getTarget());
    conf.put(KEY_UPDATE_TARGET, opt.isUpdateTarget());
    conf.put(KEY_AUTO_SYNC, opt.isAutoSync());
    conf.put(KEY_SHOW_GUI, opt.isShowGui());
    conf.put(KEY_PRINT_CONSOLE, opt.isPrintConsole());
    conf.put(KEY_START_EVENT, opt.getEvent());
    conf.put(KEY_START_DATE, opt.getDate());
    conf.put(KEY_REPEAT_INTERVAL, opt.getRepeatAsString());
    conf.put(KEY_MOD_FILE, opt.getModfile());
    return conf.save();
  }
  
  
  public boolean load() {
    boolean b = conf.load();
    if(!conf.isEmpty()) {
      if(conf.contains(KEY_SOURCE) 
          && conf.get(KEY_SOURCE) != null)
        opt.setSource(Paths.get(conf.get(KEY_SOURCE)));
      
      if(conf.contains(KEY_TARGET) 
          && conf.get(KEY_TARGET) != null)
        opt.setTarget(Paths.get(conf.get(KEY_TARGET)));
      
      if(conf.contains(KEY_UPDATE_TARGET) 
          && conf.get(KEY_UPDATE_TARGET) != null)
        opt.setUpdateTarget(Boolean.parseBoolean(
            conf.get(KEY_UPDATE_TARGET)));
      
      if(conf.contains(KEY_AUTO_SYNC) 
          && conf.get(KEY_AUTO_SYNC) != null)
        opt.setAutoSync(Boolean.parseBoolean(
            conf.get(KEY_AUTO_SYNC)));
      
      if(conf.contains(KEY_SHOW_GUI) 
          && conf.get(KEY_SHOW_GUI) != null)
        opt.setShowGui(Boolean.parseBoolean(
            conf.get(KEY_SHOW_GUI)));
      
      if(conf.contains(KEY_PRINT_CONSOLE) 
          && conf.get(KEY_PRINT_CONSOLE) != null)
        opt.setPrintConsole(Boolean.parseBoolean(
            conf.get(KEY_PRINT_CONSOLE)));
      
      if(conf.contains(KEY_START_EVENT) 
          && conf.get(KEY_START_EVENT) != null)
        opt.setEvent(SyncEvent.parseEvent(
            conf.get(KEY_START_EVENT)));
      
      if(conf.contains(KEY_START_DATE)
          && conf.get(KEY_START_DATE) != null
          && opt.getEvent() == SyncEvent.DATE)
        opt.setDate(SimpleDate.parseDate(
            conf.get(KEY_START_DATE)));
      
      if(conf.contains(KEY_REPEAT_INTERVAL)
          && conf.get(KEY_REPEAT_INTERVAL) != null
          && opt.getEvent() == SyncEvent.DATE)
        opt.setRepeatInterval(
            opt.parseRepeatInterval(
            conf.get(KEY_REPEAT_INTERVAL)), 
            opt.parseTimeUnit(conf.get(KEY_REPEAT_INTERVAL)));
      
      if(conf.contains(KEY_MOD_FILE) 
          && conf.get(KEY_MOD_FILE) != null
          && opt.getEvent() == SyncEvent.MODFILE)
        opt.setModfile(Paths.get(conf.get(KEY_MOD_FILE)));
    }
    return b;
  }
  
  
  public static void main(String[] args) throws InterruptedException {
    SyncConfig sc = new SyncConfig(new File("./sync.conf"));
    sc.clear().save();
    SyncOptions so = new SyncOptions();
    so.setSource(Paths.get("D:/zzz/src"))
        .setTarget(Paths.get("D:/zzz/dst"))
        .setEvent(SyncEvent.MODFILE)
        .setModfile(Paths.get("D:/zzz/src"))
        .setPrintConsole(false)
        .setAutoSync(true)
        .setShowGui(true)
        .setUpdateTarget(true);
    sc.setSyncOptions(so);
    sc.save();
    sc.clear();
    sc.load();
    Iterator<String> keys = sc.conf.keys();
    while(keys.hasNext()) {
      String key = keys.next();
      System.out.println("* "+ key+ " = "+ sc.conf.getObject(key));
    }
  }
  
}
