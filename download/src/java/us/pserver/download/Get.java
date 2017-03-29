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

package us.pserver.download;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Enumeration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import us.pserver.download.util.URIParam;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
@WebServlet("/get/*")
public class Get extends Base {
  
  private boolean isParent(Path parent, Path path) {
    return parent != null && path != null
        && path.startsWith(parent);
  }
  
  
  private long[] getRange(HttpServletRequest req) {
    String hrange = req.getHeader("Range");
    long[] range = new long[]{0L,0L};
    if(hrange != null && !hrange.trim().isEmpty()) {
      String[] srange = hrange.replace("bytes=", "").split("-");
      try {
        range[0] = Long.parseLong(srange[0]);
        range[1] = Long.parseLong(srange[1]);
      } catch(Exception e) {}
    }
    return range;
  }
  
  
  private void transfer(Path pth, OutputStream out, long off, long len) throws IOException {
    SeekableByteChannel ch = Files.newByteChannel(pth, StandardOpenOption.READ);
    ch.position(off);
    long nlen = len;
    ByteBuffer buf = ByteBuffer.allocate(4096);
    buf.limit((int) Math.min(len, buf.capacity()));
    int read = 0;
    while((read = ch.read(buf)) > 0 && nlen > 0) {
      buf.flip();
      out.write(buf.array(), buf.position(), buf.limit());
      buf.compact();
      nlen -= read;
    }
    out.flush();
  }
  

  @Override
  public String request(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    HttpSession ses = req.getSession();
    URIParam par = new URIParam(req.getRequestURI());
    Object opath = ses.getAttribute(Ls.CUR_PATH);
    if(par.length() > 1 && opath != null) {
      Path path = (Path) opath;
      String spath = new String(Base64.getDecoder().decode(par.getParam(1)), StandardCharsets.UTF_8);
      Path np = path.resolve(spath);
      if(Files.exists(np) && Files.isRegularFile(np) && isParent(path, np)) {
        long[] range = getRange(req);
        long total = Files.size(np);
        long length = range[0] - range[1] == 0 ? total : range[0] - range[1];
        System.out.println("##### length: "+ length);
        resp.setContentType("application/octet-stream");
        resp.setContentLengthLong(length);
        resp.setHeader("Content-Disposition:", 
            "attachment; filename=\""+ np.getFileName().toString()+ "\"");
        resp.setHeader("Accept-Ranges", "bytes");
        resp.setHeader("Content-Range", "bytes "
            + range[0]+ "-"+ (range[0]+length)+ "/"+ total);
        transfer(np, resp.getOutputStream(), range[0], length);
        resp.flushBuffer();
      }
      else {
        badRequest(resp, "File does not exist");
      }
    }
    printHeaders(req);
    return null;
  }
  
  
  private void printHeaders(HttpServletRequest req) {
    System.out.println(req.getRequestURI());
    Enumeration<String> en = req.getHeaderNames();
    while(en.hasMoreElements()) {
      String hd = en.nextElement();
      System.out.println(hd+ ": "+ req.getHeader(hd));
    }
    System.out.println("----- Request ------");
  }
  
}
