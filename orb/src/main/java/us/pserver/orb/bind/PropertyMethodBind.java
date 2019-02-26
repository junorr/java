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

package us.pserver.orb.bind;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/02/2019
 */
public class PropertyMethodBind implements MethodBind {

  @Override
  public String apply(Method meth) throws MethodBindException {
    Optional<String> annotation = MethodBind.getConfigPropertyAnnotationValue(meth);
    List<String> splitcc = MethodBind.splitCamelCase(annotation.orElse(meth.getName()));
    if(splitcc.get(0).equals("get")) splitcc.remove(0);
    StringBuilder key = new StringBuilder();
    splitcc.forEach(s -> key.append(s.toLowerCase()).append("."));
    return key.deleteCharAt(key.length() -1).toString();
  }

}
