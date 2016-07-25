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

package us.pserver.zeromap.mapper;

import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class PrimitiveArrayMapper implements Mapper {
	
  @Override
  public Node map(Object o) {
    Node n = null;
    if(o != null && o.getClass().isArray() 
				&& o.getClass().getComponentType().isPrimitive()) {
			Class<?> ctype = o.getClass().getComponentType();
			n = new ONode(o.getClass().getName());
			Mapper map = MapperFactory.factory().mapper(ctype);
      if(byte.class == ctype) {
				byte[] bs = (byte[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(char.class == ctype) {
				char[] bs = (char[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(boolean.class == ctype) {
				boolean[] bs = (boolean[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(short.class == ctype) {
				short[] bs = (short[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(int.class == ctype) {
				int[] bs = (int[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(long.class == ctype) {
				long[] bs = (long[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(float.class == ctype) {
				float[] bs = (float[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
			else if(double.class == ctype) {
				double[] bs = (double[]) o;
				for(int i = 0; i < bs.length; i++) {
					n.add(map.map(bs[i]));
				}
			}
    }
    return n;
  }


  @Override
  public Object unmap(Node n, Class cls) {
		Object o = null;
    if(n != null) {
			Node[] nds = new Node[n.childs().size()];
			nds = n.childs().toArray(nds);
			Class<?> ctype = cls.getComponentType();
			Mapper map = MapperFactory.factory().mapper(ctype);
      if(byte.class == ctype) {
				byte[] bs = new byte[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (byte) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(char.class == ctype) {
				char[] bs = new char[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (char) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(boolean.class == ctype) {
				boolean[] bs = new boolean[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (boolean) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(short.class == ctype) {
				short[] bs = new short[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (short) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(int.class == ctype) {
				int[] bs = new int[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (int) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(long.class == ctype) {
				long[] bs = new long[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (long) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(float.class == ctype) {
				float[] bs = new float[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (float) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
			else if(double.class == ctype) {
				double[] bs = new double[nds.length];
				for(int i = 0; i < bs.length; i++) {
					bs[i] = (double) map.unmap(nds[i], ctype);
				}
				o = bs;
			}
		}
		return o;
  }
  
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& cls.isArray() 
				&& cls.getComponentType().isPrimitive();
	}

}
