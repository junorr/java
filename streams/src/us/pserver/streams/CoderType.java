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

package us.pserver.streams;

/**
 * Representa um tipo de codificador 
 * disponível para uso pela classe <code>Streams</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 27/06/2014
 */
public enum CoderType {

  /**
   * Representa codificador no formato Base64.
   */
  BASE64, 
  
  /**
   * Representa codificador no formato Lzma.
   */
  LZMA, 
  
  /**
   * Representa codificador no formato GZip.
   */
  GZIP, 
  
  /**
   * Representa codificador no formato Hexadecimal.
   */
  HEX, 
  
  /**
   * Representa codificador de criptografia.
   */
  CRYPT;
  
  
  private CoderType() {
    enabled = false;
  }
  
  
  /**
   * Verifica se o tipo de codificador está
   * marcado como habilitado ou desabilitado.
   * @return <code>true</code> se o tipo de
   * codificador está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isEnabled() {
    return enabled;
  }
  
  
  /**
   * Marca o tipo de codificador como habilitado ou desabilitado.
   * @param bool <code>true</code> para marcar o tipo
   * de codificador como habilitado, <code>false</code> 
   * para marcar como desbilitado.
   */
  public void setEnabled(boolean bool) {
    enabled = bool;
  }
  
  
  private boolean enabled;
  
}
