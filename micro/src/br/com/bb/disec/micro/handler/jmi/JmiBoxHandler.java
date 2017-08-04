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

package br.com.bb.disec.micro.handler.jmi;

import br.com.bb.disec.micro.ServerSetupEnum;
import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.JsonClass;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpServerExchange;
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
public class JmiBoxHandler extends JsonSendHandler {
  
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
      default:
        response = OpResult.of(new UnsupportedOperationException(pars.getParam(0)));
    }
    this.putJsonHeader(hse);
    hse.getResponseSender().send(gson.toJson(response));
    hse.endExchange();
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
