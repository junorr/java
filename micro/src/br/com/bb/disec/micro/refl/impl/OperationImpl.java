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

package br.com.bb.disec.micro.refl.impl;

import br.com.bb.disec.micro.refl.Operation;
import br.com.bb.disec.micro.refl.OperationType;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/06/2017
 */
public class OperationImpl implements Operation {
  
  private final String name;
  
  private final OperationType type;
  
  private final List args;
  
  private final List<String> argTypes;
  
  private final String className;
  
  private final Operation next;


  public OperationImpl(String name, OperationType type, List args, List<String> argTypes, String className, Operation next) {
    this.name = name;
    this.type = type;
    this.args = args;
    this.argTypes = argTypes;
    this.className = className;
    this.next = next;
  }
  

  @Override
  public String getName() {
    return name;
  }


  @Override
  public OperationType getType() {
    return type;
  }


  @Override
  public List getArguments() {
    return args;
  }


  @Override
  public List<String> getArgumentTypes() {
    return argTypes;
  }


  @Override
  public String getClassName() {
    return className;
  }


  @Override
  public Optional<Operation> next() {
    return Optional.ofNullable(next);
  }

}
