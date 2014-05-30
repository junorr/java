/*
  Direitos Autorais Reservados (c) 2011 Juno Roesler
  Contato: juno.rr@gmail.com
  
  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.
  
  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.
  
  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/

package com.power.visual.frames;

import java.awt.Window;

/**
 * Interface que deve ser implementada por
 * efeitos visuais aplicados à janelas.
 * @author Juno Roesler - juno.rr@gmail.com
 */
public interface WindowEffect {
	
	/**
	 * Aplica o efeito visual à(s) janela(s)
	 * adicionada(s).
	 */
	public void applyEffect();
	
	/**
	 * Remove o efeito visual da(s) janela(s)
	 * adicionada(s).
	 */
	public void removeEffect();
	
	/**
	 * Adiciona uma janela à qual o efeito
	 * deverá ser aplicado.
	 * @param w java.awt.Window a ser adicionada.
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Window.html">java.awt.Window</a>
	 */
	public void addWindow(Window w);
	
	/**
	 * Retorna todas as instâncias de Window
	 * adicionadas ao efeito.
	 * @return java.awt.Window array.
	 */
	public Window[] getWindows();
	
	/**
	 * Retorna a quantidade de janelas
	 * adicionadas ao efeito.
	 * @return Quantidade de janelas adicionadas.
	 */
	public int numWindows();
	
	/**
	 * Remove a janela especificada do efeito.
	 * @param w java.awt.Window a ser removida.
	 * @return <code>true</code> se a janela foi
	 * removida com sucesso, <code>false</code>
	 * caso contrário.
	 */
	public boolean removeWindow(Window w);
	
	/**
	 * Remove todas as janelas adicionadas ao efeito.
	 */
	public void clearWindows();

}
