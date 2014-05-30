/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual;

import java.util.Scanner;

/**
 *
 * @author f6036477
 */
public class HexColor {

  public static void main(String[] args)
  {
    System.out.println("Informe o tipo de conversao:");
    System.out.println("<option> <x> [y] [z]");
    System.out.println("Onde: ");
    System.out.println("  <option>     - 'h': inteiro para hexadecimal;");
    System.out.println("                 'i': hexadecimal para inteiro");
    System.out.println("  <x> [y] [z]' - Valores a serem convertidos;");
    System.out.print("> ");

    String opt = "";
    Scanner sc = new Scanner(System.in);
    opt = sc.next();

    if(opt.equalsIgnoreCase("h")) {
      int r, g, b;
      r = g = b = 0;
      r = sc.nextInt();
      g = sc.nextInt();
      b = sc.nextInt();

      System.out.print("= ");
      System.out.print("0x"+Integer.toString(r, 16).toUpperCase());
      System.out.print(Integer.toString(g, 16).toUpperCase());
      System.out.println(Integer.toString(b, 16).toUpperCase());
    } else {
      System.out.print("= ");
      int h = Integer.parseInt(sc.next(), 16);
      System.out.print( "[ " + ((h >> 16) & 0xFF) + ", " );
      System.out.print( ((h >> 8) & 0xFF) + ", " );
      System.out.println( (h & 0xFF) + " ]" );
    }


  }

}
