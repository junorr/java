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

package com.jpower.tn3270.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/11/2013
 */
public class TestFile {

  
  public static void main(String[] args) throws FileNotFoundException, IOException {
    File f = new File("f:/BASEIDA.txt");
    FileReader fr = new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    File f2 = new File("f:/BASEIDA2.txt");
    FileWriter fw = new FileWriter(f2);
    BufferedWriter bw = new BufferedWriter(fw);
    String str;
    while((str = br.readLine()) != null) {
      if(str.endsWith(";"))
        str = str.substring(0, str.length() -1);
      bw.write(str);
      bw.newLine();
      bw.flush();
    }
    br.close();
    bw.close();
  }
  
}
