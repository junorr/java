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

package us.pserver.ironbit.serial;

import java.util.ArrayList;
import java.util.List;
import us.pserver.ironbit.record.DefaultSerialRecord;
import us.pserver.ironbit.record.SerialRecord;
import us.pserver.ironbit.Serializer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/10/2017
 */
public class ListSerializer implements Serializer<List<?>> {

  @Override
  public byte[] serialize(List<?> t) {
    List<SerialRecord<?>> recs = new ArrayList<>();
    for(Object o : t) {
      recs.add(new DefaultSerialRecord(null, o));
    }
    int size = recs.stream().map(SerialRecord::length).reduce(0, (l,s)->l+s);
    byte[] bs = new byte[size];
    int idx = 0;
    for(SerialRecord<?> s : recs) {
      byte[] br = s.toByteArray();
      System.arraycopy(br, 0, bs, idx, br.length);
      idx += br.length;
    }
    return bs;
  }

  @Override
  public List<?> deserialize(byte[] bs) {
    List<?> list = new ArrayList<>();
    List<SerialRecord<?>> recs = new ArrayList<>();
    int idx = 0;
    byte[] blen = new byte[Integer.BYTES];
    int len;
    while((idx + 1) < bytes.length) {
      System.arraycopy(bytes, idx + Integer.BYTES, blen, 0, blen.length);
      len = ints.deserialize(blen);
      byte[] bsr = new byte[len];
      System.arraycopy(bytes, idx, bsr, 0, bsr.length);
      recs.add(SerialRecord.of(bsr));
      idx += len;
    }
    return recs;
  }

}
