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
package badraadv.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 23/05/2012
 */
@ManagedBean
@ApplicationScoped
public class TagCloudBean {
  
  private TagCloudModel model;
  
  public TagCloudBean() {
    model = new DefaultTagCloudModel();
    model.addTag(new DefaultTagCloudItem("Advogado", "#", 5));
    model.addTag(new DefaultTagCloudItem("Brasília", "#", 5));
    model.addTag(new DefaultTagCloudItem("bsb", "#", 4));
    model.addTag(new DefaultTagCloudItem("Família", "#", 3));
    model.addTag(new DefaultTagCloudItem("Previdência", "#", 3));
    model.addTag(new DefaultTagCloudItem("Previdenciário", "#", 3));
    model.addTag(new DefaultTagCloudItem("ação contra o estado", "#", 3));
    model.addTag(new DefaultTagCloudItem("fazenda pública", "#", 3));
    model.addTag(new DefaultTagCloudItem("audiência", "#", 3));
    model.addTag(new DefaultTagCloudItem("Direito", "#", 3));
    model.addTag(new DefaultTagCloudItem("Justiça", "#", 3));
    model.addTag(new DefaultTagCloudItem("homoafetivo", "#", 3));
    model.addTag(new DefaultTagCloudItem("direitos gls", "#", 3));
    model.addTag(new DefaultTagCloudItem("direito glbt", "#", 3));
    model.addTag(new DefaultTagCloudItem("Porto Alegre", "#", 5));
    model.addTag(new DefaultTagCloudItem("poa", "#", 4));
    model.addTag(new DefaultTagCloudItem("rs", "#", 4));
    model.addTag(new DefaultTagCloudItem("consumidor", "#", 3));
    model.addTag(new DefaultTagCloudItem("diligência", "#", 3));
    model.addTag(new DefaultTagCloudItem("homoafetivo", "#", 3));
    model.addTag(new DefaultTagCloudItem("Tapejara", "#", 5));
  }
  
  
  public TagCloudModel getModel() {
    return model;
  }
  
}
