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

package com.jpower.net;

/**
 * Filtro de fim de transmissão para controle de janelas
 * de dados. Envia o sinal de fim de transmissão <code>(0xFFFFFFFF)</code>
 * seguido pelos bytes de quebra de linha e retorno de carro <code>(0x0A0D0A0D)</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class HttpFilter extends ByteFilter {
  
  public static final int CLR = 0x0A0D0A0D;
  
  
  public HttpFilter() {
    super(createFilter());
  }
  
  
  private static byte[] createFilter() {
    byte[] filter = new byte[4];
    filter[0] = CLR >> 24;
    filter[1] = (byte) ((CLR >> 16) & 0x00FF);
    filter[2] = (byte) ((CLR >> 8) & 0x0000FF);
    filter[3] = (byte) (CLR & 0x000000FF);
    return filter;
  }
  
  
  public static void main(String[] args) {
    createFilter();
  }
  
}
