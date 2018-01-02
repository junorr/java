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

package us.pserver.tools.om;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/01/2018
 */
public class TypedStrings {

  public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  
  public static final String DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*";
  
  public static final String LOCAL_TIME_PATTERN = "\\d{2}:\\d{2}(:\\d{2})?(\\.\\d+)?";
  
  public static final String OFFSET_TIME_PATTERN = "\\d{2}:\\d{2}(:\\d{2})?(\\.\\d+)?(-|\\+)\\d{2}:\\d{2}";
  
  public static final String CLASS_PATTERN = "\\w+\\..*\\.\\w+";
  
  public static final String IPV4_PATTERN = "(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})";
  
  public static final String DOUBLE_PATTERN = "(-|\\+)?\\d+\\.?\\d*";
  
  public static final String LONG_PATTERN = "(-|\\+)?\\d+";
  
  public static final String BOOLEAN_PATTERN = "(?i)(true|false)";
  
  
  private final List<TypedString<?>> types;
  
  
  public TypedStrings() {
    this.types = new ArrayList<>();
  }
  
}
