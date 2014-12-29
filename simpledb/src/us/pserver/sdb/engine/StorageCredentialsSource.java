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

import java.util.LinkedList;
import java.util.List;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.container.CredentialsSource;
import us.pserver.sdb.Document;
import us.pserver.sdb.SimpleDB;
import us.pserver.sdb.query.Query;
import us.pserver.sdb.query.QueryBuilder;
import us.pserver.sdb.query.Result;
import us.pserver.sdb.util.ObjectUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/12/2014
 */
public class StorageCredentialsSource implements CredentialsSource {
  
  public static final String CRED_LABEL = "us.pserver.rob.container.Credentials";
  
  private SimpleDB sdb;
  
  
  public StorageCredentialsSource(StorageEngine eng) {
    if(eng == null)
      throw new IllegalArgumentException("Invalid StorageEngine: "+ eng);
    sdb = new SimpleDB(eng);
  }
  
  
  public StorageCredentialsSource(SimpleDB sdb) {
    if(sdb == null)
      throw new IllegalArgumentException("Invalid SimpleDB instance: "+ sdb);
    this.sdb = sdb;
  }
  
  
  public StorageEngine getStorageEngine() {
    return sdb.getEngine();
  }
  
  
  public void put(Credentials crd) {
    sdb.put(ObjectUtils.toDocument(crd, true));
  }
  
  
  public boolean authenticate(Credentials crd) {
    if(crd == null || crd.getUser() == null) 
      return false;
    
    Credentials cd = getCredentialsByUser(crd.getUser());
    if(cd == null) return false;
    return cd.authenticate(crd);
  }
  
  
  public Credentials getCredentialsByUser(String user) {
    if(user == null) return null;
    Query q = QueryBuilder.builder(CRED_LABEL)
        .field("user").equal(user).create();
    Document d = sdb.getOne(q);
    if(d == null) return null;
    return (Credentials) ObjectUtils.fromDocument(d);
  }


  @Override
  public List<Credentials> getCredentials() {
    Result res = sdb.get(CRED_LABEL, -1);
    List<Credentials> creds = new LinkedList<>();
    for(Document d : res) {
      if(d != null)
        creds.add((Credentials) ObjectUtils.fromDocument(d));
    }
    return creds;
  }

}
