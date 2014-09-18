/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package com.jpower.simplemail.filter;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;


/**
 * <p style="font-size: medium;">
 * Interface para implementação de filtros
 * de mensagens.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "Filter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Interface para implementação de filtros de mensagens"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public interface Filter<E> {
  
  /**
   * Verifica se o elemento informado 
   * corresponde aos critérios do filtro.
   * @param e Elemento a ser verificado.
   * @return <code>true</code> se o elemento
   * corresponde aos criterios do filtro,
   * <code>false</code> caso contrário.
   */
  public boolean match(E e);
  
}
