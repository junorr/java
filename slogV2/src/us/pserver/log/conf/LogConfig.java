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

package us.pserver.log.conf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import us.pserver.log.Log;
import us.pserver.log.LogFactory;
import us.pserver.log.impl.SimpleLog;
import us.pserver.tools.Valid;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XFile;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class LogConfig {

  private List<XLog> logs;
  
  private final File file;
  
  
  public LogConfig(String file) {
    logs = new ArrayList<XLog>();
    this.file = new File(
        Valid.off(file).getOrFail("Invalid File Name: ")
    );
  }
  
  
  public LogConfig(File file) {
    logs = new ArrayList<XLog>();
    this.file = Valid.off(file).getOrFail("Invalid File: ");
  }
  
  
  public boolean exists() {
    return file != null && file.exists();
  }
  
  
  public void read() throws IOException {
    XFile xf = new XFile(file);
    LogConfig conf = new LogConfig(file);
    XBean bean = new XBean(xf.read(), conf);
    logs.clear();
    conf = (LogConfig) bean.bindAll().scanXml();
    logs.addAll(conf.getXLogList());
  }
  
  
  public void save() throws IOException {
    XBean bean = new XBean(this);
    XFile xf = new XFile(file, 
        bean.bindAll().scanObject().setXmlIdentation("  ", 0)
    );
    xf.save();
  }
  
  
  public List<XLog> getXLogList() {
    return logs;
  }
  
  
  public List<Log> createLogs() throws Exception {
    if(logs.isEmpty()) return Collections.EMPTY_LIST;
    ArrayList<Log> ls = new ArrayList<Log>();
    for(XLog x : logs) {
      ls.add(x.create());
    }
    return ls;
  }
  
  
  public LogConfig put(Log log) {
    if(log != null) {
      logs.add(XLog.from(log));
    }
    return this;
  }
  
  
  public static LogConfig newConfig(String file) {
    if(file == null) return null;
    LogConfig lc = new LogConfig(file);
    return lc;
  }
  
  
  public static LogConfig newConfig(File file) {
    if(file == null) return null;
    return newConfig(file.getAbsolutePath());
  }
  
}
