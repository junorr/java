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

  public static final int STATUS_200 = 200;
  
  public static final String
      HTTP = "http://",
      DOTS = ":",
      SLASH = "/",
      
      VAL_USER_AGENT = "Mozilla/5.0",
      
      HD_ACCEPT = "Accept",
      VAL_ACCEPT = "text/xml, application/x-java-rob",
      
      HD_ENCODING = "Accept-Encoding",
      VAL_NO_ENCODING = "deflate",
      VAL_GZIP_ENCODING = "gzip",
      
      HD_CONNECTION = "Connection",
      VAL_CONNECTION = "keep-alive",
      
      HD_DATE = "Date",
      
      HD_CONT_TYPE = "Content-Type",
      
      HD_CONT_ENCODING = "Content-Encoding",
      
      HD_SERVER = "Server",
      VAL_SERVER = "httpcore-revoke/4.4",
      
      STATUS_OK = "OK";
  
}
