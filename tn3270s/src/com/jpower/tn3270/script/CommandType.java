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

package com.jpower.tn3270.script;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/08/2013
 */
public enum CommandType {
  
  //append var1 var2
  APPEND(2), 
  //connect3270 {hostname|ip} port
  CONNECT_3270(2), 
  //contains var1 var2
  CONTAINS(2), 
  //cursor row col
  CURSOR(2),
  //delay {var|time}
  DELAY(1),
  //else;
  ELSE(0),
  //end
  END(0),
  //endwhile
  ENDWHILE(0),
  //equals str1/var1 str2/var2;
  EQUALS(2),
  //fileget varFile varStore varDelim
  FILE_GET(3),
  //fileput varFile varPut varDelim
  FILE_PUT(3),
  //getfield row col length varStore 
  GET_FIELD(4),
  //getscreen varStore
  GET_SCREEN(1),
  //if {var|expression}
  IF(1),
  //key keyStr
  KEY(1),
  //not boolean
  NOT(3),
  //pass name = <senha>
  PASS(2),
  //print var
  PRINT(1),
  //setfield row col varStore
  SET_FIELD(3),
  //var name = <value>
  VAR(2),
  //wait row col {text|var}
  WAIT(3),
  //while {var|expression}
  WHILE(1);
    
  private int argsSize;
    
  public int getArgsSize() {
    return argsSize;
  }
    
  CommandType(int size) {
    argsSize = size;
  }
  
}
