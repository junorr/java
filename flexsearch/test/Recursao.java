/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author f6036477
 */
public class Recursao {
  
  private static double recEx(double inc, double dec, double sum) {
    if(dec == 1) return sum;
    sum += ++inc / dec--;
    System.out.println(sum + " += " + inc + " / " + dec);
    return recEx(inc, dec, sum);
  }
  
  public static double recEx(double n) {
    return recEx(0, n, 0);
  }
  
  public static void main(String[] args) {
    System.out.println(Recursao.recEx(10));
  }
  
}
