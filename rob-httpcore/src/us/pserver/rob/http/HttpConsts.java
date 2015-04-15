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

package us.pserver.rob.http;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2015
 */
public interface HttpConsts {

  public static final String
      
      HTTP = "http://",
      DOTS = ":",
      SLASH = "/",
      
      HD_USER_AGENT = "User-Agent",
      VAL_USER_AGENT = "Mozilla/5.0",
      
      HD_ACCEPT = "Accept",
      VAL_ACCEPT = "text/html, application/x-java-rob",
      
      HD_ENCODING = "Accept-Encoding",
      VAL_ENCODING = "deflate",
      
      HD_CONNECTION = "Connection",
      VAL_CONNECTION = "keep-alive",
      
      HD_DATE = "Date",
      
      HD_CONT_TYPE = "Content-Type",
      
      HD_CONT_ENCODING = "Content-Encoding",
      
      HD_SERVER = "Server",
      VAL_SERVER = "http-rob/v1.0";
  
}
