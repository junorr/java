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

package com.jpower.lcdpaper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Juno Roesler
 * @version 0.0 - 15/12/2012
 */
public class Menu {
  
  private JPopupMenu popup;
  
  private JMenu drawMenu;
  
  private JMenu confMenu;
  
  private JMenuItem freeDraw;
  
  private JMenuItem lineDraw;
  
  private JMenuItem ovalDraw;
  
  private JMenuItem ovalFill;
  
  private JMenuItem rectDraw;
  
  private JMenuItem rectFill;
  
  private JMenuItem triDraw;
  
  private JMenuItem triFill;
  
  private JMenuItem arrowDraw;
  
  private JMenuItem arrowFill;
  
  private JMenuItem textBox;
  
  private JMenuItem textDraw;
  
  private JMenuItem checkDraw;
  
  private JMenuItem checkBox;
  
  private JMenuItem backDraw;
  
  private JMenuItem clear;
  
  private JMenuItem dashConf;
  
  private JMenuItem paperConf;
  
  private JMenuItem colorConf;
  
  private JMenuItem fontConf;
  
  private JMenu langSelect;
  
  private JMenuItem exit;
  
  private JMenu saveMenu;
  
  private JMenuItem savePaper, saveBack, saveAll;
  
  private JMenuItem open;
  
  private Labels labels;
  
  private DrawablePanel drawpanel;
  
  
  public Menu(DrawablePanel dp) {
    init(dp);
  }
  

  public void init(DrawablePanel dp) {
    drawpanel = dp;
    
    labels = new Labels();
    
    popup = new JPopupMenu(labels.contextMenu());
    
    drawMenu = new JMenu(labels.draw());
    drawMenu.setIcon(Icons.getIcon(Icons.FREE_DRAW));
    
    freeDraw = new JMenuItem(labels.free(), Icons.getIcon(Icons.FREE_DRAW));
    freeDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        freeMode();
      }
    });
    
    lineDraw = new JMenuItem(labels.line(), Icons.getIcon(Icons.LINE_DRAW));
    lineDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lineMode();
      }
    });
    
    ovalDraw = new JMenuItem(labels.oval(), Icons.getIcon(Icons.OVAL_DRAW));
    ovalDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ovalMode();
      }
    });
    
    ovalFill = new JMenuItem(labels.ovalFill(), Icons.getIcon(Icons.OVAL_FILL));
    ovalFill.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ovalFillMode();
      }
    });
    
    rectDraw = new JMenuItem(labels.rectangle(), Icons.getIcon(Icons.RECT_DRAW));
    rectDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rectMode();
      }
    });
    
    rectFill = new JMenuItem(labels.rectangleFill(), Icons.getIcon(Icons.RECT_FILL));
    rectFill.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rectFillMode();
      }
    });
    
    triDraw = new JMenuItem(labels.triangle(), Icons.getIcon(Icons.TRI_DRAW));
    triDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        triangleMode();
      }
    });
    
    triFill = new JMenuItem(labels.triangleFill(), Icons.getIcon(Icons.TRI_FILL));
    triFill.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        triangleFillMode();
      }
    });
    
    arrowDraw = new JMenuItem(labels.arrow(), Icons.getIcon(Icons.ARROW_DRAW));
    arrowDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        arrowRightMode();
      }
    });
    
    arrowFill = new JMenuItem(labels.arrowFill(), Icons.getIcon(Icons.ARROW_FILL));
    arrowFill.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        arrowRightFillMode();
      }
    });
    
    textBox = new JMenuItem(labels.textBox(), Icons.getIcon(Icons.FONT_ICON));
    textBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.setMode(DrawablePanel.Mode.TEXT_BOX);
      }
    });
    
    textDraw = new JMenuItem(labels.textDraw(), Icons.getIcon(Icons.FONT_ICON));
    textDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textDraw();
      }
    });
    
    checkBox = new JMenuItem(labels.checkBox(), Icons.getIcon(Icons.CHECK_ICON));
    checkBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.setMode(DrawablePanel.Mode.CHECK_BOX);
      }
    });
    
    checkDraw = new JMenuItem(labels.checkDraw(), Icons.getIcon(Icons.CHECK_ICON));
    checkDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.setMode(DrawablePanel.Mode.CHECK_DRAW);
      }
    });
    
    clear = new JMenuItem(labels.clear(), Icons.getIcon(Icons.ERASER_ICON));
    clear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.setMode(DrawablePanel.Mode.CLEAR);
      }
    });
    
    backDraw = new JMenuItem(labels.drawBackground(), Icons.getIcon(Icons.BACKGROUND_ICON));
    backDraw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.setMode(DrawablePanel.Mode.BACK_DRAW);
      }
    });
    
    
    drawMenu.add(freeDraw);
    drawMenu.add(lineDraw);
    drawMenu.add(ovalDraw);
    drawMenu.add(rectDraw);
    drawMenu.add(triDraw);
    drawMenu.add(arrowDraw);
    drawMenu.addSeparator();
    
    drawMenu.add(backDraw);
    drawMenu.addSeparator();
    
    drawMenu.add(ovalFill);
    drawMenu.add(rectFill);
    drawMenu.add(triFill);
    drawMenu.add(arrowFill);
    drawMenu.addSeparator();
    
    drawMenu.add(textDraw);
    drawMenu.add(checkDraw);
    drawMenu.addSeparator();

    drawMenu.add(textBox);
    drawMenu.add(checkBox);
    drawMenu.addSeparator();
    
    drawMenu.add(clear);
    
    popup.add(drawMenu);
    
    
    confMenu = new JMenu(labels.configurations());
    confMenu.setIcon(Icons.getIcon(Icons.GEAR_ICON));
    
    dashConf = new JMenuItem(labels.dashConf(), Icons.getIcon(Icons.LINE_DRAW));
    dashConf.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exit();
      }
    });
    confMenu.add(dashConf);
    
    colorConf = new JMenuItem(labels.color(), Icons.getIcon(Icons.COLOR_ICON));
    colorConf.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseColor();
      }
    });
    confMenu.add(colorConf);
    
    fontConf = new JMenuItem(labels.font(), Icons.getIcon(Icons.FONT_ICON));
    fontConf.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseFont();
      }
    });
    confMenu.add(fontConf);
    
    paperConf = new JMenuItem(labels.paperConf(), Icons.getIcon(Icons.PAPER_ICON));
    paperConf.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseMask();
      }
    });
    confMenu.add(paperConf);
    
    langSelect = new JMenu(labels.langSelect());
    langSelect.setIcon(Icons.getIcon(Icons.WORLD_ICON));
    String[] langs = labels.getAvailableLanguages();
    
    for(int i = 0; i < langs.length; i++) {
      final JMenuItem mi = new JMenuItem(langs[i]);
      mi.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          labels.setDefaultLanguage(mi.getText());
          init(drawpanel);
        }
      });
      langSelect.add(mi);
    }
    confMenu.addSeparator();
    confMenu.add(langSelect);
    
    popup.add(confMenu);
    
    
    saveMenu = new JMenu(labels.save());
    saveMenu.setIcon(Icons.getIcon(Icons.SAVE_ICON));
    
    saveAll = new JMenuItem(labels.saveAll());
    saveAll.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.saveAll();
      }
    });
    
    saveBack = new JMenuItem(labels.saveBackground());
    saveBack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.saveBackground();
      }
    });
    
    savePaper = new JMenuItem(labels.savePaper());
    savePaper.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.savePaper(null);
      }
    });
    
    saveMenu.add(saveAll);
    saveMenu.add(saveBack);
    saveMenu.add(savePaper);
    
    popup.addSeparator();
    popup.add(saveMenu);

    
    open = new JMenuItem(labels.open(), Icons.getIcon(Icons.OPEN_ICON));
    open.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drawpanel.loadPaper(null);
      }
    });
    
    popup.add(open);
    
    
    popup.addSeparator();
    exit = new JMenuItem(labels.exit(), Icons.getIcon(Icons.EXIT_ICON));
    exit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exit();
      }
    });
    popup.add(exit);
  }
  
  
  public Menu show(Component c, Point p) {
    popup.show(c, p.x, p.y);
    return this;
  }
  
  
  public void exit() {
    System.exit(0);
  }
  
  
  public void textDraw() {
    drawpanel.setMode(DrawablePanel.Mode.TEXT_DRAW);
  }
  
  
  public void freeMode() {
    drawpanel.setMode(DrawablePanel.Mode.FREE_DRAW);
  }
  
  
  public void lineMode() {
    drawpanel.setMode(DrawablePanel.Mode.LINE_DRAW);
  }
  
  
  public void ovalMode() {
    drawpanel.setMode(DrawablePanel.Mode.OVAL_DRAW);
  }
  
  
  public void ovalFillMode() {
    drawpanel.setMode(DrawablePanel.Mode.OVAL_FILL);
  }
  
  
  public void rectMode() {
    drawpanel.setMode(DrawablePanel.Mode.RECT_DRAW);
  }
  
  
  public void rectFillMode() {
    drawpanel.setMode(DrawablePanel.Mode.RECT_FILL);
  }
  
  
  public void triangleMode() {
    drawpanel.setMode(DrawablePanel.Mode.TRI_DRAW);
  }
  
  
  public void triangleFillMode() {
    drawpanel.setMode(DrawablePanel.Mode.TRI_FILL);
  }
  
  
  public void arrowRightMode() {
    drawpanel.setMode(DrawablePanel.Mode.ARROW_RIGHT_DRAW);
  }
  
  
  public void arrowRightFillMode() {
    drawpanel.setMode(DrawablePanel.Mode.ARROW_RIGHT_FILL);
  }
  
  
  public void chooseColor() {
    Color c = JColorChooser.showDialog(drawpanel, labels.colorChoose(), null);
    if(c != null) drawpanel.setColor(c);
  }
  
  
  public void chooseMask() {
    Color c = JColorChooser.showDialog(drawpanel, labels.colorChoose(), null);
    if(c != null) drawpanel.setMask(c);
  }
  
  
  public void chooseFont() {
    final FontDialog fd = new FontDialog(null, false)
        .setDefaultFont(drawpanel.getDrawFont());
    
    fd.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        drawpanel.setDrawFont(fd.getSelectedFont());
      }
    });
    fd.setLocationRelativeTo(drawpanel);
    fd.setVisible(true);
  }
  
}
