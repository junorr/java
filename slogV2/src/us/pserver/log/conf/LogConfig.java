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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import us.pserver.log.Log;
import us.pserver.log.LogFactory;
import us.pserver.log.impl.SimpleLog;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class LogConfig {

  private final List<XmlLog> logs;
  
  private final String file;
  
  
  private LogConfig(String file) {
    logs = new ArrayList<>();
    this.file = file;
  }
  
  
  public boolean exists() {
    return file != null && new File(file).exists();
  }
  
  
  public void read() throws IOException {
    if(!exists()) return;
    DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder bld = fact.newDocumentBuilder();
      Document doc = bld.parse(new File(file));
      NodeList nl = doc.getElementsByTagName(XmlLog.LOG);
      for(int i = 0; i < nl.getLength(); i++) {
        Node n = nl.item(i);
        logs.add(XmlLog.from(n));
      }
    } catch(ParserConfigurationException | SAXException | ClassNotFoundException e) {
      throw new IOException(e);
    }
  }
  
  
  public void save() throws IOException {
    DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    try {
      if(file == null)
        throw new FileNotFoundException(file);
      File f = new File(file);
      if(!f.exists()) {
        if(f.getParentFile() != null
            && !f.getParentFile().exists()) {
          f.getParentFile().mkdirs();
        }
        f.createNewFile();
      }
      DocumentBuilder bld = fact.newDocumentBuilder();
      Document doc = bld.newDocument();
      for(XmlLog x : logs) {
        doc.appendChild(x.createElement(doc));
      }
      Transformer tf = TransformerFactory.newInstance().newTransformer();
      tf.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult stream = new StreamResult(f);
      tf.transform(source, stream);
    } catch(Exception e) {
      throw new IOException(e);
    }
  }
  
  
  public List<XmlLog> getXmlLogList() {
    return logs;
  }
  
  
  public List<Log> tryCreateLogs() {
    if(logs.isEmpty()) return Collections.EMPTY_LIST;
    ArrayList<Log> ls = new ArrayList<>();
    for(XmlLog x : logs) {
      try { ls.add(x.create()); }
      catch(InstantiationException e) {}
    }
    return ls;
  }
  
  
  public List<Log> createLogs() throws InstantiationException {
    if(logs.isEmpty()) return Collections.EMPTY_LIST;
    ArrayList<Log> ls = new ArrayList<>();
    for(XmlLog x : logs) {
      ls.add(x.create());
    }
    return ls;
  }
  
  
  public LogConfig put(Log log) {
    if(log != null) {
      logs.add(XmlLog.from(log));
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
  
  
  public static void main(String[] args) throws IOException {
    //SimpleLog log = LogFactory.createDefaultSimpleLog(LogConfig.class, false);
    LogConfig lc = LogConfig.newConfig("/home/juno/log.xml");
    //lc.put(log).save();
    lc.read();
    for(Log l : lc.tryCreateLogs()) {
      l.info("Log test with retrieved Log from log.xml");
      System.out.println(l);
    }
  }
  
}
