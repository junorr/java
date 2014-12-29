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

package us.pserver.sdb;

import java.util.List;
import us.pserver.rob.MethodChain;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.server.NetworkServer;
import us.pserver.sdb.query.Query;
import us.pserver.sdb.query.Result;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/12/2014
 */
public class SimpleDBClient extends SimpleDB {

  private RemoteObject rob;
  
  private NetConnector connector;
  
  private Credentials cred;
  
  
  public SimpleDBClient(NetConnector conn, Credentials crd) throws IllegalArgumentException, SDBException {
    if(conn == null || conn.getAddress() == null
        || conn.getPort() <= 0)
      throw new IllegalArgumentException(
          "Invalid NetConnector: "+ conn);
    this.connector = conn;
    cred = crd;
    init();
  }
  
  
  private void init() throws SDBException {
    rob = new RemoteObject(connector, DefaultFactoryProvider
        .getHttpRequestChannelFactory());
    MethodChain chain = new MethodChain();
    RemoteMethod meth = chain.add(NetworkServer.class.getName(), "container");
    if(cred != null) meth.credentials(cred);
    meth = chain.add("contains").types(String.class)
        .params(SDBServer.class.getName());
    if(cred != null) meth.credentials(cred);
    try {
      boolean contains = (boolean) rob.invoke(chain);
      if(!contains)
        throw new SDBException(
            "Server Error! SDBServer instance is not present.");
    }
    catch(MethodInvocationException e) {
      rob.close();
      throw new SDBException("Connection Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public void setRemoveOnCascade(boolean rm) throws SDBException {
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("setRemoveOnCascade")
        .types(boolean.class)
        .params(rm);
    if(cred != null) meth.credentials(cred);
    try {
      rob.invokeVoid(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public boolean isRemoveOnCascade() {
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("isRemoveOnCascade");
    if(cred != null) meth.credentials(cred);
    try {
      return (boolean) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public void close() {
    rob.close();
  }
  
  
  public void stopServer() {
    RemoteMethod meth = new RemoteMethod()
        .forObject(SDBServer.class.getName())
        .method("close");
    if(cred != null) meth.credentials(cred);
    try {
      rob.invokeVoid(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document put(Document doc) throws SDBException {
    if(doc == null) return null;
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("put")
        .types(Document.class)
        .params(doc);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document remove(long blk) throws SDBException {
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("remove")
        .types(long.class)
        .params(blk);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public boolean remove(Document doc) throws SDBException {
    if(doc == null) return false;
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("remove")
        .types(Document.class)
        .params(doc);
    if(cred != null) meth.credentials(cred);
    try {
      return (boolean) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Result removeAll(Query q) throws SDBException {
    if(q == null) return new Result();
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("removeAll")
        .types(Query.class)
        .params(q);
    if(cred != null) meth.credentials(cred);
    try {
      return (Result) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document get(long blk) throws SDBException {
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("get")
        .types(long.class)
        .params(blk);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document getOne(Document doc) throws SDBException {
    if(doc == null) return null;
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("getOne")
        .types(Document.class)
        .params(doc);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document getOne(Query q) throws SDBException {
    if(q == null) return null;
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("getOne")
        .types(Query.class)
        .params(q);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Result get(Document doc) throws SDBException {
    if(doc == null) return new Result();
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("get")
        .types(Document.class)
        .params(doc);
    if(cred != null) meth.credentials(cred);
    try {
      return (Result) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Result get(String label, int limit) throws SDBException {
    if(label == null) return new Result();
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("get")
        .types(String.class, int.class)
        .params(label, limit);
    if(cred != null) meth.credentials(cred);
    try {
      return (Result) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Result get(Query q) throws SDBException {
    if(q == null) return new Result();
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("get")
        .types(Query.class)
        .params(q);
    if(cred != null) meth.credentials(cred);
    try {
      return (Result) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Result join(Query q, List<Document> list) {
    if(q == null || list == null)
      return new Result();
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("join")
        .types(Query.class, List.class)
        .params(q, list);
    if(cred != null) meth.credentials(cred);
    try {
      return (Result) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
  
  @Override
  public Document removeOne(Query q) {
    if(q == null) return null;
    RemoteMethod meth = new RemoteMethod()
        .forObject(SimpleDB.class.getName())
        .method("removeOne")
        .types(Query.class)
        .params(q);
    if(cred != null) meth.credentials(cred);
    try {
      return (Document) rob.invoke(meth);
    }
    catch(MethodInvocationException e) {
      throw new SDBException("Server Error! "+ e.toString(), e);
    }
  }
  
}
