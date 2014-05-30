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

package us.pserver.log;

import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/04/2014
 */
public class LogProvider {

  private static final SLogV2 slog = new SLogV2();
  
  private static final SimpleLog log = new SimpleLog();
  
  
  public static SimpleLog getSimpleLog() {
    return log;
  }
  
  
  public static SimpleLog getSimpleLog(String logfile) {
    Optional<LogOutput> opt = log.outputs().stream()
        .filter(l->l.isFileOutput())
        .findFirst();
    if(opt.isPresent()) {
      opt.get().close();
      opt.get().setFileOutput(logfile);
    }
    else {
      log.add(new LogOutput().setFileOutput(logfile));
    }
    return log;
  }
  
  
  public static SLogV2 getSLogV2() {
    return slog;
  }
  
  
  public static SLogV2 getSLogV2(String logfile) {
    Optional<LogOutput> opt = slog.outputs().stream()
        .filter(l->l.isFileOutput())
        .findFirst();
    if(opt.isPresent()) {
      opt.get().close();
      opt.get().setFileOutput(logfile);
    }
    else {
      slog.add(new LogOutput().setFileOutput(logfile));
    }
    return slog;
  }
  
}
