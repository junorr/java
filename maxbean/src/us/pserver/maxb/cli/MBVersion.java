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

package us.pserver.maxb.cli;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/02/2016
 */
public class MBVersion {

  private final StringBuilder version;
  
  
  public MBVersion() {
    version = new StringBuilder()
        .append("|             MaxBean V1             |\n")
        .append("|  Copyright (c) 2016 Juno Roesler   |\n")
        .append("|          <juno@pserver.us>         |\n")
        .append("|   Distributed under GNU/LGPL V3    |\n")
        .append("+------------------------------------+\n")
        .append(" Notice:\n")
        .append("  MaxBean uses Args4j for arguments parsing (MIT License)\n")
        .append(" Links:\n")
        .append("  MaxBean     [http://github.com/junorr/maxbean]\n")
        .append("  GNU/LGPL V3 [http://www.gnu.org/licenses/lgpl-3.0.en.html]\n")
        .append("  Args4j      [http://args4j.kohsuke.org]\n")
        .append("  MIT License [http://www.opensource.org/licenses/mit-license.php]\n")
        .append("\n");
  }
  
  
  public static MBVersion v() {
    return new MBVersion();
  }
  
  
  @Override
  public String toString() {
    return version.toString();
  }
  
}
