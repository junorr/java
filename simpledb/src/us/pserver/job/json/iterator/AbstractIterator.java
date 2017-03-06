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

package us.pserver.job.json.iterator;

import us.pserver.job.json.adapter.ByteIterator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/03/2017
 */
public abstract class AbstractIterator implements ByteIterator {

  protected byte current;

  protected int arraylvl;

  protected int objectlvl;

  protected String curfld;


  protected AbstractIterator() {
    current = Byte.MIN_VALUE;
    arraylvl = 0;
    objectlvl = 0;
    curfld = null;
  }


  @Override
  public byte getCurrentByte() {
    return current;
  }


  @Override
  public boolean isInsideArray() {
    return arraylvl > 0;
  }


  @Override
  public int getObjectLevel() {
    return objectlvl;
  }


  @Override
  public int getArrayLevel() {
    return arraylvl;
  }


  @Override
  public String getCurrentField() {
    return curfld;
  }

}