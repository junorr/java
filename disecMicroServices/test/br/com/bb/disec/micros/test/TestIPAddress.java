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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/02/2017
 */
public class TestIPAddress {

  
  public static void main(String[] args) throws SocketException {
    Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
    while(ifs.hasMoreElements()) {
      NetworkInterface ni = ifs.nextElement();
      System.out.println("* "+ ni);
      System.out.println("  ni.getDisplayName(): "+ ni.getDisplayName());
      System.out.println("  ni.getName().......: "+ ni.getName());
      System.out.println("  ni.isLoopback()....: "+ ni.isLoopback());
      System.out.println("  ni.isVirtual().....: "+ ni.isVirtual());
      System.out.println("  ni.getIndex()......: "+ ni.getIndex());
      Enumeration<InetAddress> ads = ni.getInetAddresses();
      while(ads.hasMoreElements()) {
        InetAddress adr = ads.nextElement();
        System.out.println("   ip="+ adr.getHostAddress()+ " - "+ adr.getClass().getSimpleName());
      }
      System.out.println("---------------------------");
    }
  }
  
}
