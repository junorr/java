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

package br.com.bb.disec.micro.jiterator;

import com.mongodb.client.MongoCursor;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2016
 */
public class MongoJsonIterator extends AbstractJsonIterator {
  
  private final MongoCursor<Document> cursor;
  
  
  public MongoJsonIterator(MongoCursor<Document> cur, long total) {
    super(total);
    if(cur == null) {
      throw new IllegalArgumentException("Bad Null MongoCursor");
    }
    this.cursor = cur;
  }
  
  
  public MongoCursor<Document> getMongoCursor() {
    return cursor;
  }
  

  @Override
  public Stream<Document> stream() {
    return StreamSupport.stream(this.spliterator(), false);
  }


  @Override
  public Document next() {
    return cursor.tryNext();
  }


  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }


  @Override
  public Iterator<Document> iterator() {
    return this;
  }

}
