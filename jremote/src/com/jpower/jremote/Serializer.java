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

package com.jpower.jremote;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Serializer {

  
  public Serializer() {}
  
  
  public Object readObject(byte[] bs) {
    if(bs == null || bs.length == 0)
      return null;
    
    try (ObjectInputStream in = 
          new ObjectInputStream(
          new ByteArrayInputStream(bs))) {
      return in.readObject();
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public byte[] writeObject(Object o) {
    if(o == null) return null;
    try(ByteArrayOutputStream bos = 
          new ByteArrayOutputStream();
        ObjectOutputStream out = 
          new ObjectOutputStream(bos)) {
      out.writeObject(o);
      out.flush();
      return bos.toByteArray();
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public BufferedImage readImage(byte[] bs) {
    if(bs == null || bs.length == 0)
      return null;
    
    try {
      return ImageIO.read(new ByteArrayInputStream(bs));
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
