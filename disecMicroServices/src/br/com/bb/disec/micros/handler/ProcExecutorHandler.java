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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.conf.ProcExecutorConfig;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/03/2017
 */
public class ProcExecutorHandler implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam par = new URIParam(hse.getRequestURI());
    if(par.length() < 2) {
      hse.setStatusCode(400).setReasonPhrase("Bad Request: Missing process Group and Name");
      hse.endExchange();
      return;
    }
    ProcExecutorConfig cfg = ProcExecutorConfig.builder().load(par.getParam(0), par.getParam(1)).build();
    System.out.println("* ProcExecutorHandler: command: "+ cfg.getCommand());
    System.out.println("* ProcExecutorHandler: waitOutput: "+ cfg.isWaitOutput());
    cfg.getProcExecutor().start();
    if(cfg.isWaitOutput()) {
      hse.startBlocking();
      cfg.getProcExecutor().transferOutput(hse.getOutputStream());
      hse.endExchange();
    }
  }

}