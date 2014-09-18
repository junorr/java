/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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


package com.jpower.sys.security;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @version 0.0 - 05/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class AccessIO {

  private XStream xs;
  
  
  public AccessIO() {
    xs = new XStream();
  }
  
  
  public String write(Access a) {
    if(a == null) return null;
    return xs.toXML(a);
  }
  
  
  public boolean write(File f, Access a) {
    if(f == null || a == null || !f.canWrite())
      return false;
    
    try {
      xs.toXML(a, new FileWriter(f));
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  public Object read(File f) {
    if(f == null || !f.exists()
        || !f.canRead()) return null;
    
    return xs.fromXML(f);
  }
  
  
  public Object read(String xml) {
    if(xml == null || xml.isEmpty())
      return null;
    return xs.fromXML(xml);
  }
  
}
