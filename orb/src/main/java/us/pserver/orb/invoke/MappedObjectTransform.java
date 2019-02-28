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

package us.pserver.orb.invoke;

import us.pserver.orb.OrbConfiguration;
import us.pserver.orb.annotation.Annotations;
import us.pserver.orb.annotation.ClasspathSource;
import us.pserver.orb.annotation.EnvironmentSource;
import us.pserver.orb.annotation.FileSource;
import us.pserver.orb.annotation.SystemPropertySource;
import us.pserver.orb.annotation.UrlSource;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public class MappedObjectTransform implements MethodTransform<Object> {
  
  private final OrbConfiguration config;
  
  public MappedObjectTransform(OrbConfiguration config) {
    this.config = Match.notNull(config).getOrFail("Bad null OrbConfiguration");
  }
  
  @Override
  public boolean canHandle(InvocationContext ctx) {
    return ctx.getMethod().getReturnType().isInterface() 
        && (Annotations.isAnnotationPresent(ClasspathSource.class, ctx.getMethod().getReturnType())
        || Annotations.isAnnotationPresent(EnvironmentSource.class, ctx.getMethod().getReturnType())
        || Annotations.isAnnotationPresent(FileSource.class, ctx.getMethod().getReturnType())
        || Annotations.isAnnotationPresent(SystemPropertySource.class, ctx.getMethod().getReturnType())
        || Annotations.isAnnotationPresent(UrlSource.class, ctx.getMethod().getReturnType()));
  }
  
  @Override
  public Object apply(InvocationContext ctx) {
    //return Orb.create()
        //.withMap((Map)ctx.getValue())
        //.withMethodToKeyFunction(bind)
        //.withTypedStrings(types)
        //.create(ctx.getMethod().getReturnType());
        return null;
  }
  
}
