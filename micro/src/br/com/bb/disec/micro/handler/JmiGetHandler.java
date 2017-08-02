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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.ServerSetupEnum;
import br.com.bb.disec.micro.box.OpBuilder;
import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.util.JsonClass;
import br.com.bb.disec.micro.util.JsonParam;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpServerExchange;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.pserver.tools.rfl.Reflector;

/**
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class JmiGetHandler implements JsonHandler {
  
  public static final String CREATE = "create";
  
  public static final String GET = "get";
  
  public static final String SET = "set";
  
  public static final String METHOD = "method";
  
  public static final String LS_METH = "lsmeth";
  
  public static final String LS_JARS = "lsjars";
  
  public static final String LS_CLASS = "lsclass";
  
  public static final String LS_CACHE = "lscache";
  
  /**
   * 
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(Class.class, new JsonClass())
        .setPrettyPrinting()
        .create();
    OpResult response = null;
    switch(pars.getParam(0)) {
      case CREATE:
        response = create(pars);
        break;
      case GET:
        response = get(pars);
        break;
      case LS_CACHE:
        response = lscache(pars);
        break;
      case LS_CLASS:
        response = lsclass(pars);
        break;
      case LS_JARS:
        response = lsjars(pars);
        break;
      case LS_METH:
        response = lsmeth(pars);
        break;
      case METHOD:
        response = method(pars);
        break;
      case SET:
        response = set(pars);
        break;
      default:
        response = OpResult.of(new UnsupportedOperationException(pars.getParam(0)));
    }
    this.putJsonHeader(hse);
    hse.getResponseSender().send(gson.toJson(response));
    hse.endExchange();
  }
  
  private OpResult create(URIParam pars) throws Exception {
    try {
      if(pars.length() < 2) {
        throw new IllegalArgumentException("Missing target class (/jmi/create/<class>/[args])");
      }
      OpBuilder bld = new OpBuilder().onClass(pars.getParam(1));
      if(pars.length() > 2) {
        Class cls = ServerSetupEnum.INSTANCE.objectBox().load(pars.getParam(1));
        Constructor[] cts = Reflector.of(cls).constructors();
        Object[] args = null;
        for(Constructor c : cts) {
          if(c.getParameterCount() == pars.length() -2) {
            Class[] types = c.getParameterTypes();
            args = new JsonParam(types, pars.shift(2)).getParams();
          }
        }
        bld = bld.withArgs(args);
      }
      return ServerSetupEnum.INSTANCE.objectBox().execute(bld.constructor().build());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult method(URIParam pars) throws Exception {
    try {
      if(pars.length() < 3) {
        throw new IllegalArgumentException("Missing target class and method name (/jmi/method/<class>/<name>/[args])");
      }
      OpBuilder bld = new OpBuilder()
          .onClass(pars.getParam(1))
          .withName(pars.getParam(2));
      if(pars.length() > 3) {
        Class cls = ServerSetupEnum.INSTANCE.objectBox().load(pars.getParam(1));
        Method[] mts = Reflector.of(cls).methods();
        Object[] args = null;
        int npar = pars.length() - 3;
        String name = pars.getParam(2);
        for(Method m : mts) {
          if(m.getParameterCount() == npar && m.getName().equals(name)) {
            Class[] types = m.getParameterTypes();
            args = new JsonParam(types, pars.shift(3)).getParams();
          }
        }
        bld = bld.withArgs(args);
      }
      return ServerSetupEnum.INSTANCE.objectBox().execute(bld.method().build());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult set(URIParam pars) throws Exception {
    try {
      if(pars.length() < 4) {
        throw new IllegalArgumentException("Missing target class, field name and argument (/jmi/set/<class>/<name>/<arg>)");
      }
      OpBuilder bld = new OpBuilder()
          .onClass(pars.getParam(1))
          .withName(pars.getParam(2));
      Class cls = ServerSetupEnum.INSTANCE.objectBox().load(pars.getParam(1));
      Field fld = Reflector.of(cls).selectField(pars.getParam(2)).field();
      Class[] types = new Class[] {fld.getType()};
      Object[] args = new JsonParam(types, pars.shift(3)).getParams();
      bld = bld.withArgs(args);
      return ServerSetupEnum.INSTANCE.objectBox().execute(bld.set().build());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult get(URIParam pars) throws Exception {
    try {
      if(pars.length() < 3) {
        throw new IllegalArgumentException("Missing target class and field name (/jmi/get/<class>/<name>)");
      }
      OpBuilder bld = new OpBuilder()
          .onClass(pars.getParam(1))
          .withName(pars.getParam(2));
      return ServerSetupEnum.INSTANCE.objectBox().execute(bld.get().build());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult lsclass(URIParam pars) throws Exception {
    try {
      if(pars.length() < 2) {
        throw new IllegalArgumentException("Missing jar file name (/jmi/lsclass/<jar>)");
      }
      return OpResult.of(ServerSetupEnum.INSTANCE.objectBox().listClasses(pars.getParam(1)));
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult lsmeth(URIParam pars) throws Exception {
    try {
      if(pars.length() < 2) {
        throw new IllegalArgumentException("Missing target class (/jmi/lsmeth/<class>)");
      }
      Class cls = ServerSetupEnum.INSTANCE.objectBox().load(pars.getParam(1));
      List<String> lsmeth = new ArrayList<>();
      Method[] mts = Reflector.of(cls).methods();
      Method[] cmt = Reflector.of(Class.class).methods();
      for(Method m : mts) {
        if(Arrays.asList(cmt).stream().allMatch(
            c->!c.getName().equals(m.getName()))) {
          Class[] types = m.getParameterTypes();
          StringBuilder sb = new StringBuilder()
              .append(m.getName())
              .append("(");
          for(Class c : types) {
            sb.append(c.getSimpleName()).append(", ");
          }
          if(types.length > 0) {
            sb.delete(sb.length()-2, sb.length());
          }
          lsmeth.add(sb.append(")").toString());
        }
      }
      return OpResult.of(lsmeth);
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult lsjars(URIParam pars) throws Exception {
    try {
      return OpResult.of(ServerSetupEnum.INSTANCE.objectBox().listJars());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  private OpResult lscache(URIParam pars) throws Exception {
    try {
      return OpResult.of(ServerSetupEnum.INSTANCE.objectBox().cache());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
}
