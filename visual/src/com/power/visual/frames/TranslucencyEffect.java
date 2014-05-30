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

import com.sun.awt.AWTUtilities;
import java.awt.Frame;
import java.awt.Window;

/**
 * Efeito visual que aplica transparência às
 * janelas adicionadas, através da classe 
 * <code>com.sun.awt.AWTUtilities</code>.
 * @see com.power.visual.AbstractWindowEffect
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class TranslucencyEffect extends AbstractWindowEffect {

	private float translucency;
	
	
	/**
	 * Construtor padrão.
	 */
	public TranslucencyEffect() {
		super();
		this.translucency = 1f;
	}
	
	
	@Override
	public void addWindow(Window w) {
		if(w == null) return;
		
		if(w instanceof Frame
				&& !((Frame)w).isUndecorated())
			throw new IllegalArgumentException(
					"Frame must be Undecorated");
		
		super.addWindow(w);
	}
	
	
	/**
	 * Define o percentual de transparência a
	 * ser aplicado à janela.
	 * @param f Percentual de transparência, sendo
	 * <code>1.0</code> opaco e <code>0.0</code>
	 * totalmente transparente.
	 */
	public void setTranslucency(float f) {
		if(f >= 0)
			this.translucency = f;
	}
	
	
	/**
	 * Retorna o percentual de transparência
	 * aplicado à janela.
	 * @return percentual de transparência.
	 */
	public float getTranslucency() {
		return this.translucency;
	}
	

	@Override
	public void applyEffect() {
		for(int i = 0; i < windows.size(); i++) {
			Window w = windows.get(i);
			AWTUtilities.setWindowOpacity(w, translucency);
			w.repaint();
		}
	}


	@Override
	public void removeEffect() {
		for(int i = 0; i < windows.size(); i++) {
			Window w = windows.get(i);
			AWTUtilities.setWindowOpacity(w, 1f);
			w.repaint();
		}
	}

}
