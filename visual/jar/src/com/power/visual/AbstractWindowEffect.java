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

import com.sun.awt.AWTUtilities;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe abstrata que implementa as funcionalidades
 * comuns aos efeitos visuais WindowEffect.
 * @see com.power.visual.WindowEffect
 * @author Juno Roesler - juno.rr@gmail.com
 */
public abstract class AbstractWindowEffect implements WindowEffect {

	/**
	 * Lista para armazenar as janelas controladas pelo efeito visual.
	 */
	protected List<Window> windows;
	
	
	/**
	 * Construtor protegido que deve ser referenciado
	 * pelas sub classes. Verifica se o ambiente
	 * gráfico possui suporte à transparência, 
	 * lançando uma exceção caso não possua.
	 * @throws java.lang.IllegalStateException
	 */
	protected AbstractWindowEffect() {
		if(!isTranslucencyCapable())
			throw new IllegalStateException(
					"GraphicsEnvironment is not translucency capable.");
		windows = new LinkedList<Window>();
	}
	

	/**
	 * Verifica e retorna se o ambiente gráfico possui
	 * suporte à transparência.
	 * @return <code>true</code> caso possua suporte 
	 * à transparência, <code>false</code> caso contrário.
	 */
	public static boolean isTranslucencyCapable() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration conf = device.getDefaultConfiguration();
		return AWTUtilities.isTranslucencySupported(
				AWTUtilities.Translucency.PERPIXEL_TRANSLUCENT)
			&& AWTUtilities.isTranslucencyCapable(conf);
	}


	@Override
	public void addWindow(Window w) {
		if(w == null) return;
		windows.add(w);
	}


	@Override
	public Window[] getWindows() {
		if(windows.isEmpty()) return null;
		Window[] ws = new Window[windows.size()];
		return windows.toArray(ws);
	}


	@Override
	public int numWindows() {
		return windows.size();
	}


	@Override
	public boolean removeWindow(Window w) {
		return windows.remove(w);
	}


	@Override
	public void clearWindows() {
		windows.clear();
	}

}
