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

import us.pserver.rob.channel.*;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2015
 */
public class BinMethodRequest {

  private byte[] binObject;
  
  private byte[] binMethod;
  
  private byte[] binAuth;
  
  private byte[][] binTypes;
  
  private byte[][] binArgs;


  public BinMethodRequest() {
  }


  public byte[] getBinObject() {
    return binObject;
  }


  public void setBinObject(byte[] binObject) {
    this.binObject = binObject;
  }


  public byte[] getBinMethod() {
    return binMethod;
  }


  public void setBinMethod(byte[] binMethod) {
    this.binMethod = binMethod;
  }


  public byte[] getBinAuth() {
    return binAuth;
  }


  public void setBinAuth(byte[] binAuth) {
    this.binAuth = binAuth;
  }


  public byte[][] getBinTypes() {
    return binTypes;
  }


  public void setBinTypes(byte[][] binTypes) {
    this.binTypes = binTypes;
  }


  public byte[][] getBinArgs() {
    return binArgs;
  }


  public void setBinArgs(byte[][] binArgs) {
    this.binArgs = binArgs;
  }
  
}
