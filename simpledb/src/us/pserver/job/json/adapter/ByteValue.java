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

package us.pserver.job.json.adapter;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/02/2017
 */
public abstract class ByteValue {

  public static final byte START_OBJECT = 123; //'{'
  public static final byte END_OBJECT = 125; //'}'
  public static final byte START_ARRAY = 91; //'['
  public static final byte END_ARRAY = 93; //']'
  public static final byte NUM0 = 48; //'0'
  public static final byte NUM1 = 49; //'1'
  public static final byte NUM2 = 50; //'2'
  public static final byte NUM3 = 51; //'3'
  public static final byte NUM4 = 52; //'4'
  public static final byte NUM5 = 53; //'5'
  public static final byte NUM6 = 54; //'6'
  public static final byte NUM7 = 55; //'7'
  public static final byte NUM8 = 56; //'8'
  public static final byte NUM9 = 57; //'9'
  public static final byte NULL = 110; //'n'
  public static final byte UNULL = 78; //'N'
  public static final byte BOOL_TRUE = 116; //'t'
  public static final byte BOOL_UTRUE = 84; //'T'
  public static final byte BOOL_FALSE = 102; //'f'
  public static final byte BOOL_UFALSE = 70; //'F'
  public static final byte STRING = 34; //'"'
  public static final byte VALUE = 58; //':'
  public static final byte FIELD = 44; //','
  public static final byte IGNORE = 32; //' '
  public static final byte MINUS = 45; //'-'
  
}
