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

package us.pserver.bitbox.util;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2018
 */
public interface Region extends Comparable<Region> {

  public int offset();
  
  public int length();
  
  public int end();
  
  public boolean isValid();
  
  public boolean contains(Region r);
    
  @Override
  public default int compareTo(Region o) {
    return Integer.valueOf(end()).compareTo(o.end());
  }
  
  
  
  public static Region of(int offset, int length) {
    return new RegionImpl(offset, length);
  }
  
  public static Region invalid() {
    return of(-1, -1);
  }
  
  
  
  
  
  static class RegionImpl implements Region {
    
    private final int offset, length;
    
    public RegionImpl(int offset, int length) {
      this.offset = offset;
      this.length = length;
    }

    @Override
    public int offset() {
      return offset;
    }
    
    @Override
    public int length() {
      return length;
    }
    
    @Override
    public int end() {
      return offset + length;
    }
    
    @Override
    public boolean isValid() {
      return offset >= 0 && length >= 0;
    }
    
    @Override
    public boolean contains(Region r) {
      return offset <= r.offset() && end() >= r.end();
    }
    
  }
  
}
