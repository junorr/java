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

package com.jpower.lcdpaper;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/12/2012
 */
public class ObjectIO {

  private static ObjectOutputStream oos;
  
  private static ObjectInputStream ois;
  
  
  public static IOException save(Object o, String filename) {
    try {
      oos = new ObjectOutputStream(new FileOutputStream(filename));
      oos.writeObject(o);
      oos.flush();
      oos.close();
      return null;
    } catch(IOException ex) {
      return ex;
    }
  }
  
  
  public static Object load(String filename) {
    try {
      ois = new ObjectInputStream(new FileInputStream(filename));
      Object o = ois.readObject();
      ois.close();
      return o;
    } catch(IOException | ClassNotFoundException ex) {
      return ex;
    }
  }
  
  
  public static boolean saveXML(Object o, String filename) {
    XStream xs = new XStream();
    try {
      xs.toXML(o, new FileOutputStream(filename));
      return true;
    } catch(FileNotFoundException ex) {
      return false;
    }
  }
  
  
  public static Object loadXML(Object o, String filename) {
    XStream xs = new XStream();
    try {
      return xs.fromXML(new FileInputStream(filename));
    } catch(FileNotFoundException ex) {
      return null;
    }
  }
  
}
