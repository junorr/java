/*
 * Direitos Autorais Reservados (c) 2016 Banco do Brasil S.A.
 * Contato: f6036477@bb.com.br
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

package br.com.bb.disec.micros.db.mongo;

import br.com.bb.disec.micros.util.JsonHash;
import java.util.List;
import java.util.Objects;


/**
 * Encapsula Metadados sobre uma coleção de cache
 * de registros armazenados no MongoDB.
 * @author Juno Roesler - f6036477@bb.com.br
 * @version 0.0 - 23/09/2016
 */
public class MongoMetaData {

  private final String colname;
  
  private String fhash;
  
  private List<String> columns;
  
  private long total;
  
  private boolean filterChanged;
  
  
  public MongoMetaData(String colname) {
    this(colname, null, false, null, 0);
  }
  
  
  public MongoMetaData(JsonHash hash) {
    if(hash == null) {
      throw new IllegalArgumentException("Bad Null JsonHash");
    }
    this.colname = hash.collectionHash();
    this.fhash = hash.filterHash();
    this.columns = null;
    this.total = 0;
    this.filterChanged = false;
  }


  /**
   * Construtor padrão.
   * @param colname Nome da coleção
   * @param fhash Hash dos parâmetros de filtro
   * @param filterChanged Verificador de alterção do filtro
   * @param columns Colunas dos registros
   * @param total Total de registros
   */
  protected MongoMetaData(
      String colname, 
      String fhash, 
      boolean filterChanged,
      List<String> columns, 
      long total
  ) {
    if(colname == null) {
      throw new IllegalArgumentException("Bad Null Column Name");
    }
    this.colname = colname;
    this.fhash = fhash;
    this.columns = columns;
    this.total = total;
    this.filterChanged = filterChanged;
  }
  
  
  /**
   * Verifica se o hash de filtro sofreu
   * alteração em relação à informação
   * armazenada no MongoDB.
   * @return <code>true</code> se o hash 
   * de filtro sofreu alteração em relação 
   * à informação armazenada no MongoDB,
   * <code>false</code> caso contrário.
   */
  public boolean isFilterChanged() {
    return this.filterChanged;
  }
  
  
  /**
   * Cria outra instância de MongoMetaData 
   * com as mesmas propriedades.
   * @return 
   */
  @Override
  public MongoMetaData clone() {
    return new MongoMetaData(colname, fhash, 
        filterChanged, columns, total
    );
  }
  
  
  /**
   * Nome da coleção.
   * @return 
   */
  public String collectionName() {
    return colname;
  }
  
  
  /**
   * Hash dos parâmetros de filtro.
   * @return 
   */
  public String filterHash() {
    return fhash;
  }
  
  
  /**
   * Colunas dos registros da coleção.
   * @return 
   */
  public List<String> columns() {
    return columns;
  }
  
  
  /**
   * Quantidade total de registros na coleção.
   * @return 
   */
  public long total() {
    return total;
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o hash dos parâmetros de filtro informado e
   * demais propriedades inalteradas.
   * @param fhash Hash dos parâmetros de filtro.
   * @return Outra instância de MongoMetaData
   */
  public synchronized MongoMetaData filterHash(String fhash) {
    if(fhash != null) {
      this.filterChanged = this.fhash != null 
          && !this.fhash.equals(fhash);
      this.fhash = fhash;
    }
    return this;
  }
  
  
  public synchronized MongoMetaData columns(List<String> cols) {
    this.columns = cols;
    return this;
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o total de registros informado e
   * demais propriedades inalteradas.
   * @param total Total de registros na coleção.
   * @return Outra instância de MongoMetaData
   */
  public synchronized MongoMetaData total(long total) {
    this.total = total;
    return this;
  }
  
  
  public long incrementTotal() {
    return (total += 1);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.colname);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MongoMetaData other = (MongoMetaData) obj;
    if (!Objects.equals(this.colname, other.colname)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "MongoMetaData{\n" 
        + "  - colname: " + colname + ",\n"
        + "  - fhash..: " + fhash + ",\n"
        + "  - columns: " + columns + ",\n"
        + "  - total..: " + total 
        + "\n}";
  }
  
}
