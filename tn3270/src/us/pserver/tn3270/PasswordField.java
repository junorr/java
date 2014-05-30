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

package us.pserver.tn3270;

import us.pserver.cdr.b64.Base64StringCoder;



/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 01/08/2013
 */
public class PasswordField extends Field {
  
  private Base64StringCoder sc;
  
  
  public PasswordField() {
    super();
    sc = new Base64StringCoder();
  }
  
  
  public PasswordField(int row, int col, String pwd) {
    this();
    super.setRow(row);
    super.setColumn(col);
    this.setPlainPassword(pwd);
  }

  
  @Override
  public String getContent() {
    if(super.getContent() != null)
      return sc.decode(super.getContent());
    return null;
  }
  
  
  public PasswordField setPlainPassword(String str) {
    if(str != null && !str.isEmpty())
      super.setContent(sc.encode(str));
    return this;
  }
  
  
  public PasswordField setPassword(String pwd) {
    if(pwd != null)
      super.setContent(pwd);
    return this;
  }
  
  
  public static void main(String[] args) {
    Field fld = new PasswordField()
        .setPassword("NjU0NjU0Nzc=").setCursor(14, 21);
    System.out.println(fld);
    System.out.println(fld.getContent());
  }
  
}
