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

package us.pserver.dyna;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import us.pserver.dyna.impl.DynaLoaderImpl;

/**
 * A singletone version of DynaLoader.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public final class DynaLoaderInstance implements DynaLoader {
  
  private static final DynaLoader instance = new DynaLoaderImpl();
  
  public DynaLoaderInstance() {}
  
  @Override
  public List<Path> listJars() {
    return instance.listJars();
  }

  @Override
  public URL[] listURLs() {
    return instance.listURLs();
  }

  @Override
  public Class<?> load(String cls) {
    return instance.load(cls);
  }
  
  @Override
  public Class<?> load(String cls, Path jar) {
    return instance.load(cls, jar);
  }

  @Override
  public Object loadAndCreate(String cls) {
    return instance.loadAndCreate(cls);
  }

  @Override
  public ResourceLoader getResourceLoader(String cls) {
    return instance.getResourceLoader(cls);
  }

  @Override
  public ResourceLoader getResourceLoader(Class cls) {
    return instance.getResourceLoader(cls);
  }

  @Override
  public ClassLoader getClassLoader() {
    return instance.getClassLoader();
  }

  @Override
  public boolean isRegistered(Path path) {
    return instance.isRegistered(path);
  }

  @Override
  public DynaLoader register(Path path) {
    return instance.register(path);
  }

  @Override
  public DynaLoader unregister(Path path) {
    return instance.register(path);
  }

  @Override
  public void close() throws IOException {
    instance.close();
  }
  
  @Override
  public DynaLoader reset() {
    return instance.reset();
  }

}