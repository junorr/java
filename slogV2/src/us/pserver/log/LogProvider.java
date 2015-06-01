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

  private static SLogV2 slog;
  
  private static SimpleLog log;
  
  
  public static SimpleLog getSimpleLog() {
    if(log == null)
      log = new SimpleLog();
    return log;
  }
  
  
  public static SimpleLog getSimpleLog(String logfile) {
    if(logfile == null || logfile.trim().isEmpty())
      return log;
    
    log = SimpleLogFactory.instance()
        .newStdOutput()
        .enableNonErrorLevels()
        .debug(false)
        .add()
        
        .newErrOutput()
        .enableErrorLevels()
        .add()
        
        .newFileOutput(logfile)
        .enableAllLevels()
        .add()
        
        .create();
    return log;
  }
  
  
  public static SLogV2 getSLogV2() {
    if(slog == null)
      slog = new SLogV2();
    return slog;
  }
  
  
  public static SLogV2 getSLogV2(String logfile) {
    if(logfile == null || logfile.trim().isEmpty())
      return slog;
    
    slog = LogFactory.instance()
        .newStdOutput()
        .enableNonErrorLevels()
        .debug(false)
        .add()
        
        .newErrOutput()
        .enableErrorLevels()
        .add()
        
        .newFileOutput(logfile)
        .enableAllLevels()
        .add()
        
        .create();
    return slog;
  }
  
}
