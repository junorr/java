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

package us.pserver.streams;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;


/**
 * Classe com métodos utilitários para manipulação 
 * de streams.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public abstract class StreamUtils {
  
  /**
   * <code>
   *  BYTES_CRLF = {10, 13}
   * </code><br>
   * Bytes de caracteres de quebra de linha.
   */
  public static final byte[] BYTES_CRLF = {10, 13};
  
  /**
   * <code>
   *  BYTES_EOF = {69, 79, 70}
   * </code><br>
   * Bytes de caracteres 'EOF' de fim de transmissão.
   */
  public static final byte[] BYTES_EOF = {69, 79, 70};
  
  /**
   * <code>
   *  EOF = "EOF"
   * </code><br>
   * Representação <code>String</code> de fim de arquivo.
   */
  public static final String EOF = "EOF";
  
  /**
   * <code>
   *  UTF8 = "UTF-8"
   * </code><br>
   * Codificação de caracteres.
   */
  public static final String UTF8 = "UTF-8";
  
  /**
   * <code>
   *  BUFFER_SIZE = 4096
   * </code><br>
   * Tamanho do buffer em bytes.
   */
  public static int BUFFER_SIZE = 4096;
  
  
  /**
   * Transfere o conteúdo entre dois streams até final
   * ou até encontrar os bytes relativos ao fim de transmissão 
   * <code>EOF</code>.
   * @param in <code>InputStream</code>
   * @param out <code>OutputStream</code>
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    CounterInputStream cin = new CounterInputStream(in);
		StreamConnector.builder()
				.from(cin).to(out)
				.get().connect();
		return cin.getCount();
  }
  
  
  public static long transferUntilEOF(InputStream in, OutputStream out) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    return transfer(
				new SearchableInputStream(
						in, BYTES_EOF), out
    );
  }
  
  
  public static long consume(InputStream is) throws IOException {
    return transfer(is, NullOutput.out);
  }
  
  
  /**
   * Transfere o conteúdo entre dois streams até que seja
   * encontrada a <code>String</code> fornecida no conteúdo da 
   * transmissão, ou até o final da transmissão.
   * @param is <code>InputStream</code>
   * @param os <code>OutputStream</code>
   * @param until <code>String</code> de condição de 
   * parada de transferência de conteúdo.
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static StreamResult transferUntil(InputStream is, OutputStream os, String until) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(os).forNull().fail(OutputStream.class);
    Valid.off(until).forEmpty().fail();
    final StreamResult res = new StreamResult();
		SearchableInputStream si = new SearchableInputStream(is,
				new UTF8String(until).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(until)
		);
		while(true) {
			try {
				os.write(si.readByte());
			} catch(EOFException e) {
				break;
			}
		}
		return res;
  }
	
	
  public static StreamResult transferUntil(InputStream is, OutputStream os, byte[] until) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(os).forNull().fail(OutputStream.class);
    Valid.off(until).forEmpty().fail();
    final StreamResult res = new StreamResult();
		SearchableInputStream si = new SearchableInputStream(is,
				until, stream -> res.setSize(stream.getTotal())
						.setContent(new UTF8String(until).toString())
		);
		while(true) {
			try {
				os.write(si.readByte());
			} catch(EOFException e) {
				break;
			}
		}
		return res;
  }
	
	
  public static StreamResult transferUntilOr(InputStream is, OutputStream os, String until, String or) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(os).forNull().fail(OutputStream.class);
    Valid.off(until).forEmpty().fail();
    Valid.off(or).forEmpty().fail();
    final StreamResult res = new StreamResult();
		SearchableInputStream si = new SearchableInputStream(is,
				new UTF8String(until).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(until)
		);
		si = new SearchableInputStream(si,
				new UTF8String(or).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(or)
		);
		while(true) {
			try {
				os.write(si.readByte());
			} catch(EOFException e) {
				break;
			}
		}
		return res;
  }
  
  
  public static StreamResult transferBetween(
      InputStream in, 
      OutputStream out, 
      String start, 
      String end
  ) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    Valid.off(start).forEmpty().fail();
    Valid.off(out).forEmpty().fail();
    skipUntil(in, start);
    return transferUntil(in, out, end);
  }
  
  
  /**
   * Escreve uma <code>String</code> codificada em <code>UTF-8</code>
   * no stream de saída.
   * @param str <code>String</code> a ser escrita.
   * @param out <code>OuputStream</code>.
   * @throws IOException caso ocorra erro na escrita.
   */
  public static void write(String str, OutputStream out) throws IOException {
    Valid.off(out).forNull().fail(OutputStream.class);
    Valid.off(str).forEmpty().fail();
    out.write(new UTF8String(str).getBytes());
    out.flush();
  }
  
  
  /**
   * Lê e retorna o conteúdo do stream que esteja
   * delimitado entre duas <code>Strings</code>, 
   * <code>start</code> e <code>end</code>, descartando o resto.
   * @param in <code>InputStream</code>
   * @param start <code>String</code> demarcando início do conteúdo.
   * @param end <code>String</code> demarcando o final do conteúdo.
   * @return O conteúdo <code>String</code> lido.
   * @throws IOException caso ocorra erro na leitura.
   */
  public static StreamResult readBetween(InputStream in, String start, String end) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(start).forEmpty().fail();
    Valid.off(end).forEmpty().fail();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamResult res = transferBetween(in, bos, start, end);
    return res.setContent(bos.toString(UTF8));
  }


  /**
   * Lê e descarta o conteúdo do stream, até que 
   * seja encontrada a <code>String</code> fornecida.
   * @param in <code>InputStream</code>
   * @param str <code>String</code> delimitadora.
   * @return <code>true</code> se a <code>String</code>
   * delimitadora for encontrada, <code>false</code>
   * caso contrário.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static StreamResult skipUntil(InputStream in, String str) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(str).forEmpty().fail();
    final StreamResult res = new StreamResult();
		SearchableInputStream si = new SearchableInputStream(in,
				new UTF8String(str).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(str)
		);
		while(true) {
			try {
				si.readByte();
			} catch(EOFException e) {
				break;
			}
		}
		return res;
  }


  /**
   * Lê e descarta o conteúdo do stream até que 
   * seja encontrada a primeira <code>String</code> 
   * fornecida, ou a segunda <code>String</code> 
   * fornecida, retornando a <code>String</code> 
   * encontrada, ou <code>null</code> no caso de 
   * nenhum argumento encontrado.
   * @param in <code>InputStream</code>
   * @param until primeira <code>String</code> delimitadora.
   * @param or segunda <code>String</code> delimitadora.
   * @return a <code>String</code> encontrada, ou 
   * <code>null</code> no caso de nenhum argumento encontrado.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static StreamResult skipUntilOr(InputStream in, String until, String or) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(until).forEmpty().fail();
    Valid.off(or).forEmpty().fail();
    final StreamResult res = new StreamResult();
		SearchableInputStream si = new SearchableInputStream(in,
				new UTF8String(until).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(until)
		);
		si = new SearchableInputStream(si,
				new UTF8String(or).getBytes(),
				stream -> res.setSize(stream.getTotal())
						.setContent(or)
		);
		while(true) {
			try {
				si.readByte();
			} catch(EOFException e) {
				break;
			}
		}
		return res;
  }
  
  
  public static String readString(InputStream is, int length) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(length).forNotBetween(1, Integer.MAX_VALUE);
    byte[] bs = new byte[length];
    int read = is.read(bs);
    if(read <= 0) return null;
    return new UTF8String(bs, 0, read).toString();
  }
  
  
  public static StreamResult readStringUntil(InputStream is, String until) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(until).forEmpty().fail();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamResult res = transferUntil(is, bos, until);
    return res.setContent(bos.toString(UTF8));
  }
  
  
  public static StreamResult readStringUntilOr(InputStream is, String until, String or) throws IOException {
    Valid.off(is).forNull().fail(InputStream.class);
    Valid.off(until).forEmpty().fail();
    Valid.off(or).forEmpty().fail();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamResult res = transferUntilOr(is, bos, until, or);
    return res.setContent(bos.toString(UTF8));
  }
  
  
  public static void writeEOF(OutputStream os) throws IOException {
    Valid.off(os).forNull().fail(OutputStream.class);
    os.write(BYTES_EOF);
    os.write(new byte[0]);
    os.flush();
  }
  
}
