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

import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;

/**
 * Classe que estende javax.swing.JFrame, provendo
 * funcionalidades "plug and play" para efeitos visuais,
 * sem a necessidade de alteração de código.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see <a href="http://download.oracle.com/javase/6/docs/api/javax/swing/JFrame.html">javax.swing.JFrame</a>
 */
public class PluggableEffectFrame extends JFrame {
	
	private List<WindowEffect> effects;
	
	
	/**
	 * Construtor padrão sem argumentos.
	 */
	public PluggableEffectFrame() {
		effects = new LinkedList<WindowEffect>();
	}
	
	
	/**
	 * Construtor que recebe o título da janela.
	 * @param title Título a ser exibido na janela.
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/javax/swing/JFrame.html">javax.swing.JFrame</a>
	 */
	public PluggableEffectFrame(String title) {
		super(title);
		effects = new LinkedList<WindowEffect>();
	}
	
	
	/**
	 * Adiciona um efeito visual à janela.
	 * @param e Efeito visual.
	 * @see com.power.visual.WindowEffect
	 */
	public void addEffect(WindowEffect e) {
		if(e == null) return;
		if(!effects.isEmpty()) {
			WindowEffect i = null;
			while((i = this.searchIncompatibleEffect(e)) != null)
				this.removeEffect(i);
		}
		e.addWindow(this);
		effects.add(e);
	}
	
	
	/**
	 * Remove um efeito visual da janela.
	 * @param e Efeito visual.
	 * @return <code>true</code> se WindowEffect
	 * foi removido com sucesso, <code>false</code>
	 * caso contrário.
	 * @see com.power.visual.WindowEffect
	 */
	public boolean removeEffect(WindowEffect e) {
		return effects.remove(e);
	}
	
	
	/**
	 * Retorna a quantidade de efeitos visuais
	 * adicionados à janela.
	 * @return Quantidade de efeitos adicionados.
	 */
	public int numEffects() {
		return effects.size();
	}
	
	
	/**
	 * Remove todos os efeitos visuais da janela.
	 */
	public void clearEffects() {
		effects.clear();
	}
	
	
	/**
	 * Retorna um array contendo todos os
	 * efeitos visuais da janela.
	 * @return <code>WindowEffect array</code>.
	 */
	public WindowEffect[] getEffects() {
		WindowEffect[] efs = new WindowEffect[effects.size()];
		return effects.toArray(efs);
	}
	
	
	/**
	 * Procura se existe algum efeito adicionado incompatível
	 * com o efeito informado (e.g. ReflectionEffect / ShadowEffect).
	 * @param e WindowEffect
	 * @return WindowEffect incompatível, ou <code>null</code>, caso 
	 * não exista.
	 */
	public WindowEffect searchIncompatibleEffect(WindowEffect e) {
		if(e == null || effects.isEmpty()) return null;
		
		if(e instanceof ReflectionEffect)
			return this.serchShadowEffect();
		else if(e instanceof ShadowEffect)
			return this.serchReflectionEffect();
		else return null;
	}
	
	
	/**
	 * Procura e retorna a primeira instância de 
	 * <code>ReflectionEffect</code>
	 * @return ReflectionEffect.
	 */
	private ReflectionEffect serchReflectionEffect() {
		if(effects.isEmpty()) return null;
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i) instanceof ReflectionEffect)
				return (ReflectionEffect) effects.get(i);
		}
		return null;
	}

	
	/**
	 * Procura e retorna a primeira instância de 
	 * <code>ShadowEffect</code>
	 * @return ReflectionEffect.
	 */
	private ShadowEffect serchShadowEffect() {
		if(effects.isEmpty()) return null;
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i) instanceof ShadowEffect)
				return (ShadowEffect) effects.get(i);
		}
		return null;
	}
	
	
	/**
	 * Método para tornar visível a janela
	 * (deve ser utilizado no lugar de <code>setVisible(boolean)</code>, 
	 * aplicando todos os efeitos visuais adicionados. 
	 */
	public void open() {
		boolean isFader = false;
		if(!effects.isEmpty()) {
			for(WindowEffect e : effects) {
				if(e instanceof FaderEffect) {
					isFader = true;
          e.applyEffect();
        }
			}
		}
		if(!isFader) this.setVisible(true);
	}
  
  
  public void setVisible(boolean b) {
    super.setVisible(b);
		if(!effects.isEmpty()) {
			for(WindowEffect e : effects) {
        if(!(e instanceof FaderEffect))
          e.applyEffect();
			}
		}
  }
	

	/**
	 * Método para fechar a janela (deve ser utilizado
	 * no lugar de <code>dispose() ou setVisible(boolean)</code>, 
	 * removendo todos os efeitos visuais adicionados. 
	 */
	public void close() {
		boolean isFader = false;
		if(!effects.isEmpty()) {
			for(WindowEffect e : effects) {
				e.removeEffect();
				if(e instanceof FaderEffect)
					isFader = true;
			}
		}
		if(!isFader) this.dispose();
	}
	
}
