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

package br.com.bb.disec.micros.test;

import br.com.bb.disec.micros.util.Buffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/03/2017
 */
public class TestProcess {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    //ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "ls -lh /storage");
    //ProcessBuilder pb = new ProcessBuilder("ls", "-lh", "/storage");
    ProcessBuilder pb = new ProcessBuilder("ss", "-tl", "-np");
    System.out.println("* execute: "+ pb.command());
    pb.redirectError(pb.redirectOutput());
    Process p = pb.start();
    Buffer b = Buffer.create();
    InputStream in = p.getInputStream();
    int i  = 0;
    while((i = in.read()) != -1) {
      b.write((byte)i);
    }
    p.waitFor();
    System.out.println("* process output:");
    System.out.println("-------------------------");
    System.out.println(new String(b.toBytes(), StandardCharsets.UTF_8));
    System.out.println("-------------------------");
  }
  
}
