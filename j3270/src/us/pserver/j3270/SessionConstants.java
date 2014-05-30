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

package us.pserver.j3270;

import java.awt.Font;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/02/2014
 */
public interface SessionConstants {
  
  public static final int EVENT_F1 = 112;
  
  public static final int EVENT_F2 = 113;
  
  public static final int EVENT_F3 = 114;
  
  public static final int EVENT_F4 = 115;
  
  public static final int EVENT_F5 = 116;
  
  public static final int EVENT_F6 = 117;
  
  public static final int EVENT_F7 = 118;
  
  public static final int EVENT_F8 = 119;
  
  public static final int EVENT_F9 = 120;
  
  public static final int EVENT_F10 = 121;
  
  public static final int EVENT_F11 = 112;
  
  public static final int EVENT_F12 = 112;
  
  public static final int EVENT_PGUP = 33;
  
  public static final int EVENT_PGDOWN = 34;
  
  public static final int EVENT_INSERT = 155;
  
  public static final int EVENT_HOME = 36;
  
  public static final int EVENT_END = 35;
  
  public static final int EVENT_ESC = 27;
  
  public static final int EVENT_TAB = 9;
  
  public static final int EVENT_ENTER = 10;
  
  public static final String MONO = "Monospaced";
  
  public static final Font DEFAULT_FONT = new Font(MONO, Font.PLAIN, 16);
  
  public static final int 
      COLS = 80, ROWS = 24;
}
