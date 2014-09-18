package com.jpower.jremote;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.imageio.ImageIO;

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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Screenshot implements Serializable {
  
  private byte[] bytes;
  
  
  public Screenshot() {
    bytes = null;
  }


  public byte[] getBytes() {
    return bytes;
  }


  public void setBytes(byte[] image) {
    this.bytes = image;
  }
  
  
  public BufferedImage asImage() {
    if(bytes == null || bytes.length == 0)
      return null;
    ByteArrayInputStream bin = 
        new ByteArrayInputStream(bytes);
    try {
      return ImageIO.read(bin);
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Arrays.hashCode(this.bytes);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Screenshot other = (Screenshot) obj;
    if (!Arrays.equals(this.bytes, other.bytes)) {
      return false;
    }
    return true;
  }
  
}
