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

package com.pserver.isys;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/08/2013
 */
public enum EmailDefinition {

  
  TITLE_EXEC_DEFINED_SCRIPT(":Lembrete:"),
  
  TITLE_EXEC_ATTACHED_SCRIPT(":Arquivar:"),
  
  TITLE_EXEC_SYSTEM_COMMAND(":Atualizar:"),
  
  
  CONTENT_TAG("###"),
  
  CONTENT_SCHEDULE_TAG("@@@"),
  
  CONTENT_BLOCK_SCREEN("BLS"),
  
  CONTENT_SHUTDOWN("STD"),
  
  CONTENT_LOGOFF("LGF"),
  
  CONTENT_WTN("WTN"),
  
  CONTENT_CMD("CMD:"),
  
  CONTENT_REBOOT("RBT");
  
  
  EmailDefinition(String tag) {
    this.tag = tag;
  }
  
  public String getTag() {
    return tag;
  }
  
  private String tag;
  
}
