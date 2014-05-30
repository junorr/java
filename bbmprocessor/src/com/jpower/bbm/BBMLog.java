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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class BBMLog {
  
  private File file;
  
  private PrintStream ps;
  
  private SimpleDate now;
  
  
  public BBMLog(File f) {
    if(f == null)
      throw new IllegalArgumentException("Invalid log file: "+ f);
    file = f;
    now = new SimpleDate();
  }
  
  
  public BBMLog log(String s) {
    if(ps == null)
      try {
        ps = new PrintStream(new FileOutputStream(file, file.exists()));
      } catch(FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    ps.print("* (");
    ps.print(now.setNow());
    ps.print("): ");
    ps.println(s);
    ps.flush();
    return this;
  }
  
  
  public BBMLog close() {
    ps.close();
    return this;
  }

}
