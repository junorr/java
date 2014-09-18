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

package us.pserver.mapshare;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/01/2014
 */
public class Main {
  
  public static final String FILE_SHARES = "net-shares.xml";
  
  public static final String FILE_CREDENTIALS = "credentials.xml";
  
  
  public static void defineShares() {
    LinkedList<NetShare> shr = new LinkedList<>();
    shr.add(new NetShare("G:", "\\\\172.18.51.197\\NOPGEEX3D3$"));
    shr.add(new NetShare("H:", "\\\\172.18.51.6\\APLIC"));
    shr.add(new NetShare("P:", "\\\\172.18.51.57\\APLICBB"));
    shr.add(new NetShare("R:", "\\\\172.18.51.92\\MULTIMIDIA"));
    shr.add(new NetShare("M:", "\\\\172.18.51.197\\noppnop$"));
    Mapper.persist(shr, "net-shares.xml");
  }

  
  public static void main(String[] args) {
    Credentials cred = null;
    
    if(!Mapper.isMapped(FILE_SHARES))
      defineShares();
    
    if(Mapper.isMapped(FILE_CREDENTIALS)) {
      ObjectLocker locker = Mapper.load(FILE_CREDENTIALS);
      if(locker != null)
        cred = locker.unlock();
    }
    
    List<NetShare> shares = Mapper.load(FILE_SHARES);
    
    if(cred == null) {
      CredentialsDialog dialog = new CredentialsDialog(null);
      cred = dialog.showDialog();
      if(dialog.isRememberChecked())
        Mapper.persist(new ObjectLocker(cred), FILE_CREDENTIALS);
    }
    
    if(!cred.isSetted()) {
      JOptionPane.showMessageDialog(null, 
          "Usuário/Senha inválidos!\nNão foi possível mapear os diretórios de rede.", 
          "Erro", JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }
    
    for(NetShare ns : shares) {
      if(!Mapper.isMapped(ns.getDrive()))
        Mapper.map(ns, cred);
    }
  }
  
}
