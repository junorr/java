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

package com.jpower.lcdpaper;

import java.io.File;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2012
 */
public class Labels {
  
  public static final String
      
      BACKGROUND = "background",
      
      CONTEXT_MENU = "context_menu",
      
      DASH_CONF = "dash_conf",
      
      PAPER_CONF = "paper_conf",
      
      CANCEL = "cancel",
      
      CHECK_BOX = "check_box",
      
      CHECK_DRAW = "check_draw",
      
      CLEAR = "clear",
      
      COLOR = "color",
      
      COLOR_CHOOSE = "color_choose",
      
      CONFIG = "configurations",
      
      DRAW = "draw",
      
      DRAW_BACKGROUND = "draw_background",
      
      DELETE = "delete",
      
      EXIT = "exit",
      
      FREE = "free",
      
      FONT = "font",
      
      FONT_NAME = "font_name",
      
      FONT_SIZE = "font_size",
      
      FONT_STYLE = "font_style",
      
      FONT_BOLD = "font_bold",
      
      FONT_ITALIC = "font_italic",
      
      LINE = "line",
      
      OVAL = "oval",
      
      OVAL_FILL = "oval_fill",
      
      OPEN = "open",
      
      RECTANGLE = "rectangle",
      
      RECTANGLE_FILL = "rectangle_fill",
      
      SAVE_PAPER = "save_paper",
      
      SAVE_BACKGROUND = "save_background",
      
      SAVE = "save",
      
      SAVE_ALL = "save_all",
      
      TRIANGLE = "triangle",
      
      TRIANGLE_FILL = "triangle_fill",
      
      TEXT_BOX = "text_box",
      
      TEXT_DRAW = "text_draw",
      
      TEXT_QUESTION = "text_draw_question",
      
      TEXT_TITLE = "text_draw_title",
      
      ARROW = "arrow",
      
      ARROW_FILL = "arrow_fill",
      
      LANGUAGE = "language";
      
  
  public static final String
      
      LANG_DIR = "./languages/",
      
      LANG_ENGLISH = LANG_DIR + "english.conf",
      
      LANG_PORTUGUESE = LANG_DIR + "portuguese.conf",
      
      LANG_DEFAULT = LANG_DIR + "default.conf";
  
  
  private Config conf;
  
  
  public Labels() {
    this(LANG_DEFAULT);
  }
   
  
  public Labels(String langfile) {
    conf = new Config(langfile);
    if(!conf.getFile().exists())
      createDefault();
  }
  
  
  private void createDefault() {
    conf.put(ARROW, "Arrow")
        .put(ARROW_FILL, "Arrow Fill")
        .put(BACKGROUND, "Background")
        .put(CANCEL, "Cancel")
        .put(CHECK_BOX, "Check Box")
        .put(CHECK_DRAW, "Check Draw")
        .put(CLEAR, "Clear")
        .put(COLOR, "Color")
        .put(COLOR_CHOOSE, "Choose a Color")
        .put(CONTEXT_MENU, "Context Menu")
        .put(CONFIG, "Configurations")
        .put(PAPER_CONF, "Paper")
        .put(DASH_CONF, "Dash")
        .put(DRAW, "Draw")
        .put(DRAW_BACKGROUND, "Draw Background")
        .put(DELETE, "Delete")
        .put(FREE, "Free")
        .put(FONT, "Font")
        .put(FONT_NAME, "Font Name")
        .put(FONT_SIZE, "Font Size")
        .put(FONT_STYLE, "Font Style")
        .put(FONT_ITALIC, "Italic")
        .put(FONT_BOLD, "Bold")
        .put(EXIT, "Exit")
        .put(LANGUAGE, "Language")
        .put(LINE, "Line")
        .put(OVAL, "Oval")
        .put(OVAL_FILL, "Oval Fill")
        .put(OPEN, "Open")
        .put(RECTANGLE, "Rectangle")
        .put(RECTANGLE_FILL, "Rectangle Fill")
        .put(SAVE_PAPER, "Save Paper")
        .put(SAVE_BACKGROUND, "Save Background")
        .put(SAVE_ALL, "Save All")
        .put(SAVE, "Save")
        .put(TRIANGLE, "Triangle")
        .put(TRIANGLE_FILL, "Triangle Fill")
        .put(TEXT_BOX, "Text Box")
        .put(TEXT_DRAW, "Text Draw")
        .put(TEXT_QUESTION, "Informe the text to draw:")
        .put(TEXT_TITLE, "Draw Text");
    
    conf.setComment("LCD Paper - Languages");
    conf.save();
    conf.setFile(LANG_ENGLISH).save();
    conf.setFile(LANG_DEFAULT);
  }
  
  
  public String[] getAvailableLanguages() {
    File langDir = new File(LANG_DIR);
    if(!langDir.exists()) return null;
    File[] langs = langDir.listFiles();
    
    String[] names = new String[langs.length];
    for(int i = 0; i < langs.length; i++) {
      String name = langs[i].getName();
      names[i] = name.substring(0, 1)
          .toUpperCase().concat(
          name.substring(1, 
          name.lastIndexOf(".")));
    }
    return names;
  }
  
  
  public Labels setDefaultLanguage(String lang) {
    conf.setFile(LANG_DIR + lang.toLowerCase() + ".conf")
        .load();
    conf.setFile(LANG_DEFAULT)
        .setComment("LCD Paper - Languages").save();
    return this;
  }
  
  
  public String arrow() {
    return conf.get(ARROW);
  }

  
  public String arrowFill() {
    return conf.get(ARROW_FILL);
  }
  
  
  public String background() {
    return conf.get(BACKGROUND);
  }
  
  
  public String cancel() {
    return conf.get(CANCEL);
  }

  
  public String checkBox() {
    return conf.get(CHECK_BOX);
  }

  
  public String checkDraw() {
    return conf.get(CHECK_DRAW);
  }

  
  public String clear() {
    return conf.get(CLEAR);
  }

  
  public String color() {
    return conf.get(COLOR);
  }
  
  
  public String colorChoose() {
    return conf.get(COLOR_CHOOSE);
  }

  
  public String contextMenu() {
    return conf.get(CONTEXT_MENU);
  }
  
  
  public String configurations() {
    return conf.get(CONFIG);
  }

  
  public String paperConf() {
    return conf.get(PAPER_CONF);
  }

  
  public String dashConf() {
    return conf.get(DASH_CONF);
  }
  
  
  public String draw() {
    return conf.get(DRAW);
  }

  
  public String drawBackground() {
    return conf.get(DRAW_BACKGROUND);
  }

  
  public String delete() {
    return conf.get(DELETE);
  }
  
  
  public String free() {
    return conf.get(FREE);
  }
  
  
  public String font() {
    return conf.get(FONT);
  }

  
  public String fontBold() {
    return conf.get(FONT_BOLD);
  }

  
  public String fontItalic() {
    return conf.get(FONT_ITALIC);
  }

  
  public String fontName() {
    return conf.get(FONT_NAME);
  }

  
  public String fontSize() {
    return conf.get(FONT_SIZE);
  }

  
  public String fontStyle() {
    return conf.get(FONT_STYLE);
  }

  
  public String exit() {
    return conf.get(EXIT);
  }

  
  public String langSelect() {
    return conf.get(LANGUAGE);
  }

  
  public String line() {
    return conf.get(LINE);
  }

  
  public String oval() {
    return conf.get(OVAL);
  }

  
  public String ovalFill() {
    return conf.get(OVAL_FILL);
  }

  
  public String open() {
    return conf.get(OPEN);
  }

  
  public String rectangle() {
    return conf.get(RECTANGLE);
  }

  
  public String rectangleFill() {
    return conf.get(RECTANGLE_FILL);
  }

  
  public String save() {
    return conf.get(SAVE);
  }

  
  public String saveAll() {
    return conf.get(SAVE_ALL);
  }

  
  public String saveBackground() {
    return conf.get(SAVE_BACKGROUND);
  }

  
  public String savePaper() {
    return conf.get(SAVE_PAPER);
  }

  
  public String triangle() {
    return conf.get(TRIANGLE);
  }

  
  public String triangleFill() {
    return conf.get(TRIANGLE_FILL);
  }

  
  public String textBox() {
    return conf.get(TEXT_BOX);
  }

  
  public String textDraw() {
    return conf.get(TEXT_DRAW);
  }

  
  public String textQuestion() {
    return conf.get(TEXT_QUESTION);
  }

  
  public String textTitle() {
    return conf.get(TEXT_TITLE);
  }

}
