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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class AnaliseWriter {
  
  public static final String HEADER = 
      "PREFIXO; OPERACAO; SITUACAO; DATA; MES; "
      + "PRMOD; LINHA; VALOR; INSTANCIA";
  
  private PrintStream ps;
  
  private File file;
  
  
  public AnaliseWriter() {
    ps = null;
    file = null;
  }
  
  
  public AnaliseWriter(File f) {
    if(f == null)
      throw new IllegalArgumentException(
          "Invalid File: "+ f);
    file = f;
  }
  
  
  public AnaliseWriter setFile(File f) {
    file = f;
    return this;
  }
  
  
  public AnaliseWriter write(Analise a) {
    if(a == null) return this;
    if(file == null) throw new IllegalStateException(
        "Invalid File: "+ file);
    
    try {
      if(ps == null) {
        ps = new PrintStream(new FileOutputStream(file, file.exists()));
        ps.println(HEADER);
      }
      ps.println(a);
      ps.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }
  
  
  public AnaliseWriter close() {
    ps.close();
    return this;
  }

}
