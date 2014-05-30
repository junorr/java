package com.power.visual.test;

import com.power.visual.controls.BlackButton;
import com.power.visual.controls.PaintStyle;
import com.jpower.utils.PowerImage;
import com.power.visual.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe de teste de BlackButton.
 * Exibe uma janela com dois botoes do estilo
 * selecionado, um gradiente e outro nao.
 */
public class TestBlackButton {

  public static void main(String[] args) {

    String button = "black";
    args = new String[] { "black" };

    if(args.length < 1) {
      System.out.println("\nUso: \n"+
        "  TestBlackButton <style>\n"+
        "  <style>:  [black | yellow | blue | winxp]\n"
      );
      //System.exit(0);
    } else
      button = args[0];

    JFrame f = new JFrame("BlackButton Test");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setBounds(200, 200, 200, 400);
    f.setLayout(new FlowLayout());

    String s = "Black Button";

    PaintStyle paint1, paint2;

    if(button.equals("yellow")) {
      paint1 = PaintStyle.YELLOW_STYLE;
      paint2 = PaintStyle.GRADIENT_YELLOW;
    } else if(button.equals("blue")) {
      paint1 = PaintStyle.BLUE_STYLE;
      paint2 = PaintStyle.GRADIENT_BLUE;
    } else if(button.equals("winxp")) {
      paint1 = PaintStyle.WINXP_STYLE;
      paint2 = PaintStyle.GRADIENT_WINXP;
    } else {
      paint1 = PaintStyle.BLACK_STYLE;
      paint2 = PaintStyle.GRADIENT_BLACK;
    }

    BlackButton bb = new BlackButton(paint1, s);

    BlackButton bc = new BlackButton(paint2,
        PowerImage.getInstance(
        "T:/java/com/power/images/elipse3D-[64x34].png"),
        "");

    final RunningLabel rl = new RunningLabel();

    bc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae)
        {
          rl.stop();
        }//method()
    });

    bb.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae)
        {
          rl.start();
        }//method()
    });

    bb.setPreferredSize(new Dimension(110, 40));
    bc.setPreferredSize(new Dimension(110, 40));

    //rl.setSize(180, 180);
    rl.setPreferredSize(new Dimension(100, 100));

    f.add(bb);
    f.add(bc);
    f.add(rl);
    f.setVisible(true);
  }//main()

}//TestBlackButton.class
