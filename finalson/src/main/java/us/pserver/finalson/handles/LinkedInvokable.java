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

package us.pserver.finalson.handles;

import java.lang.invoke.MethodHandles.Lookup;
import java.util.List;
import java.util.Objects;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2018
 */
public interface LinkedInvokable {

  public List<InvokableParam> getInvokableParameters();
  
  public Invokable getInvokable();
  
  public <T> T invoke(FinalsonConfig conf);
  
  public <T> T invoke(FinalsonConfig conf, Lookup lookup);
  
  
  public static LinkedInvokable of(Invokable ivk, List<InvokableParam> prm) {
    return new DefaultLinkedInvokable(ivk, prm);
  }
  
  
  
  
  
  public static class DefaultLinkedInvokable implements LinkedInvokable {
    
    private final Invokable invok;
    
    private final List<InvokableParam> params;


    public DefaultLinkedInvokable(Invokable invok, List<InvokableParam> params) {
      this.invok = Match.notNull(invok).getOrFail("Bad null Invokable");
      this.params = Match.notEmpty(params).getOrFail("Bad null List<InvokableParam>");
    }
    

    @Override
    public List<InvokableParam> getInvokableParameters() {
      return params;
    }


    @Override
    public Invokable getInvokable() {
      return invok;
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 19 * hash + Objects.hashCode(this.params);
      hash = 19 * hash + Objects.hashCode(this.invok);
      return hash;
    }


    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final DefaultLinkedInvokable other = (DefaultLinkedInvokable) obj;
      if (!Objects.equals(this.params, other.params)) {
        return false;
      }
      return Objects.equals(this.invok, other.invok);
    }


    @Override
    public String toString() {
      return "LinkedInvokable{" + "params=" + params + ", invok=" + invok + '}';
    }
    
  }
  
}
