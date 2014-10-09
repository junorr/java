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

package us.pserver.sdb.engine;

import java.util.Objects;
import us.pserver.sdb.Document;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/10/2014
 */
public class DocHits {

  private Document doc;
  
  private int hits;
  
  
  public DocHits() {
    doc = null;
    hits = 0;
  }
  
  
  public DocHits(Document doc, int hits) {
    this.doc = doc;
    this.hits = hits;
  }
  
  
  public Document document() {
    return doc;
  }
  
  
  public DocHits document(Document d) {
    doc = d;
    return this;
  }
  
  
  public int hits() {
    return hits;
  }
  
  
  public DocHits hits(int hts) {
    hits = hts;
    return this;
  }
  
  
  public DocHits incHits() {
    hits++;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.doc);
    hash = 67 * hash + this.hits;
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
    final DocHits other = (DocHits) obj;
    if (!Objects.equals(this.doc, other.doc)) {
      return false;
    }
    if (this.hits != other.hits) {
      return false;
    }
    return true;
  }
  
}
