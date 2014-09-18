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

package com.jpower.tn3270;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public enum Key {
  
  PF1              (0xF1),
  PF2              (0xF2),
  PF3              (0xF3),
  PF4              (0xF4),
  PF5              (0xF5),
  PF6              (0xF6),
  PF7              (0xF7),
  PF8              (0xF8),
  PF9              (0xF9),
  PF10             (0x7A),
  PF11             (0x7B),
  PF12             (0x7C),
  PF13             (0xC1),
  PF14             (0xC2),
  PF15             (0xC3),
  PF16             (0xC4),
  PF17             (0xC5),
  PF18             (0xC7),
  PF19             (0xC8),
  PF20             (0xC9),
  PF21             (0xC),
  PF22             (0x4A),
  PF23             (0x4B),
  PF24             (0x4C),
  
  ENTER            (0x7D),
  BACKSPACE        (9000),
  HOME             (9001),
  DELETE           (9002),
  LEFT             (9003),
  RIGHT            (9004),
  UP               (9005),
  DOWN             (9006),
  TAB              (9007),
  BACK_TAB         (9008),
  SHIFT_TAB        (9009);
  
  
  Key(int value) {
    this.value = (short) value;
  }
  
  
  private short value;
  
  
  public short value() {
    return value;
  }
  
}
