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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.pstat;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/02/2013
 */
@ManagedBean
@SessionScoped
public class IndexController implements Serializable {
  
  public static final String
      CPU = "CPU Status and Monitoring",
      MEMORY = "Memory Consumption",
      DISK = "Disk Usage",
      NETWORK = "Network Load and Traffic",
      PROCESSES = "Processes Watch and Control",
      TERMINAL = "Full Control with Terminal Access";
  
  
  private List<ImageInfo> images;
  
  private ImageInfo currentImageInfo;
  
  private int index;

  
  public IndexController() {
    images = new LinkedList<>();
    index = -1;
    init();
  }
  
  
  public void nextImage() {
    index++;
    if(index >= images.size())
      index = 0;
    currentImageInfo = images.get(index);
  }
  
  
  public void prevImage() {
    index--;
    if(index < 0)
      index = images.size() -1;
    currentImageInfo = images.get(index);
  }
  
  
  private void init() {
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("cpu.xhtml?faces-redirect=true")
        .setName("pcware.png")
        .setTitle("CPU")
        .setComment(CPU));
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("memory.xhtml?faces-redirect=true")
        .setName("memory.png")
        .setTitle("Memory")
        .setComment(MEMORY));
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("disk.xhtml?faces-redirect=true")
        .setName("hd-blue.png")
        .setTitle("Disk")
        .setComment(DISK));
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("network.xhtml?faces-redirect=true")
        .setName("network.png")
        .setTitle("Network")
        .setComment(NETWORK));
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("processes.xhtml?faces-redirect=true")
        .setName("servers.png")
        .setTitle("Processes")
        .setComment(PROCESSES));
    images.add(new ImageInfo()
        .setPath("images")
        .setLink("term.xhtml?faces-redirect=true")
        .setName("terminal.png")
        .setTitle("Terminal")
        .setComment(TERMINAL));
    
    this.nextImage();
  }


  public List<ImageInfo> getImages() {
    return images;
  }


  public void setImages(List<ImageInfo> images) {
    this.images = images;
  }


  public ImageInfo getCurrentImageInfo() {
    return currentImageInfo;
  }
  
}
