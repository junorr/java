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

package com.power.visual;

import com.power.utils.Keeper;
import com.sun.awt.AWTUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Implementa o efeito visual de esmaecer a
 * janela gradualmente até se tornar totalmente opaca 
 * ou totalmente transparente.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see com.power.visual.AbstractWindowEffect
 * @see com.power.visual.WindowEffect
 */
public class FaderEffect extends TranslucencyEffect {
	
	
	/**
	 * Construtor padrão.
	 * @see com.power.visual.AbstractWindowEffect
	 */
	public FaderEffect() {
		super();
	}
	
	
	@Override
	public void applyEffect() {
		this.fadeIn();
	}
	
	
	@Override
	public void removeEffect() {
		this.fadeOut();
	}


	/**
	 * Faz com que a janela controlada pelo
	 * efeito apareça gradualmente de tornar-se
	 * totalmente opaca.
	 */
	public void fadeIn() {
		if(windows.isEmpty()) return;
		
		final Keeper<Float> opacity = new Keeper<Float>(0f);
		final float maxOpacity = 1f;
		final float opacityInc = 0.2f;

		for(int i = 0; i < windows.size(); i++) {
			Window w = windows.get(i);
			AWTUtilities.setWindowOpacity(w, opacity.get());
			w.setVisible(true);
		}

		final Keeper<Timer> fade = new Keeper<Timer>();

		fade.set(new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opacity.set(opacity.get() + opacityInc);
				if(opacity.get() <= maxOpacity) {
					setTranslucency(opacity.get());
					FaderEffect.super.applyEffect();
				} else {
					fade.get().stop();
				}//else
			}
		}));
		fade.get().setRepeats(true);
		fade.get().start();
	}


	/**
	 * Faz com que a janela controlada pelo
	 * efeito desapareça gradualmente de 
	 * totalmente opaca até tornar-se
	 * totalmente transparente.
	 */
	public void fadeOut() {
		if(windows.isEmpty()) return;
		
		final float maxOpacity = 1f;
		final Keeper<Float> opacity = new Keeper<Float>(maxOpacity);
		final float opacityDec = 0.2f;

		boolean visible = true;
		for(int i = 0; i < windows.size(); i++) {
			Window w = windows.get(i);
			if(!w.isVisible()) visible = false;
			AWTUtilities.setWindowOpacity(w, opacity.get());
		}
		if(!visible) return;

		final Keeper<Timer> fade = new Keeper<Timer>();

		fade.set(new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opacity.set(opacity.get() - opacityDec);
				if(opacity.get() >= 0) {
					setTranslucency(opacity.get());
					FaderEffect.super.applyEffect();
				} else {
					for(int i = 0; i < windows.size(); i++) {
						windows.get(i).dispose();
					}					
					fade.get().stop();
				}
			}
		}));
		fade.get().setRepeats(true);
		fade.get().start();
	}

}
