package com.jpower.fxs.document;

/**
 * Classe para formatação de números, com
 * caracteres de separação de grupo e decimal
 * customizáveis, controle de
 * precisão de casas decimais com arredondamento
 * automático e tamanho dos agrupamentos.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.16
 */
public class NumberFormat {

	private char groupsep;

	private int groupcount;

	private char decsep;

	private int precision;

	private long first;

	private double last;

	private boolean zerofill;


	/**
	 * Construtor padrão, constroi um <code>NumberFormat</code>
	 * com os seguintes padrões de formatação:
	 * <code>
	 * Caractere de separação decimal : ','
	 * Caractere de separação de grupo: '.'
	 * Precisão de casas decimais     :  2
	 * Agrupamento de números         :  3
	 * </code>
	 */
	public NumberFormat() {
		groupsep = '.';
		decsep = ',';
		precision = 2;
		groupcount = 3;
		first = 0;
		last = 0;
		zerofill = false;
	}


	/**
	 * Define o caractere de separação de grupo.
	 * @param separator Caractere de separação de grupo
	 * @return Esta instância modificada de <code>NumberFormat</code>.
	 */
	public NumberFormat groupingBy(char separator) {
		groupsep = separator;
		return this;
	}


	/**
	 * Define o tamanho do agrupamento dos números.
	 * @param count Tamanho do agrupamento.
	 * @return Esta instância modificada de <code>NumberFormat</code>.
	 */
	public NumberFormat groupingOn(int count) {
		groupcount = count;
		return this;
	}


	/**
	 * Retorna o caractere de separação de grupo.
	 * @return Caractere de separação de grupo
	 */
	public char groupingBy() {
		return groupsep;
	}


	/**
	 * Retorna o tamanho do agrupamento dos números.
	 * @return Tamanho do agrupamento.
	 */
	public int grounpingOn() {
		return groupcount;
	}


	/**
	 * Define o caractere de separação decimal.
	 * @param separator Caractere de separação decimal.
	 * @return Esta instância modificada de <code>NumberFormat</code>.
	 */
	public NumberFormat decimalBy(char dec) {
		decsep = dec;
		return this;
	}


	/**
	 * Define a quantidade de casas decimais,
	 * arredondando se necessário.
	 * @param count Quantidade de casas decimais.
	 * @return Esta instância modificada de <code>NumberFormat</code>.
	 */
	public NumberFormat decimalOn(int count) {
		precision = count;
		return this;
	}


	/**
	 * Retorna o caractere de separação decimal.
	 * @return Caractere de separação decimal.
	 */
	public char decimalBy() {
		return decsep;
	}


	/**
	 * Retorna a quantidade de casas decimais.
	 * @return Quantidade de casas decimais.
	 */
	public int decimalOn() {
		return precision;
	}
  
  
  /**
   * Retorna a String do caracter separador decimal.
   * @return String do caracter separador decimal.
   */
  public String stringDecimal() {
    return String.valueOf(decsep);
  }
  
  /**
   * Retorna a String do caracter separador de grupo.
   * @return String do caracter separador de grupo.
   */
  public String stringGroup() {
    return String.valueOf(groupsep);
  }


	/**
	 * Define o preenchimento com zeros, caso
	 * a quantidade de casas decimais do número seja
	 * menor que a quantidade definida em <code>NumberFormat</code>.
	 * @return Esta instância modificada de <code>NumberFormat</code>.
	 */
	public NumberFormat zerofill() {
		zerofill = true;
		return this;
	}
  
  /**
   * Verifica se a String informada é número ou não,
   * considerando a formatação definida para 
   * NumberFormat.
   * @param s String a ser verificada.
   * @return <code>true</code> se a String
   *  representa um número, <code>false</code>
   *  caso contrário.
   */
  public boolean isNumber(String s) {
    if(isEmpty(s)) return false;
    
    int dsc = 0;
    boolean num = true;
    boolean group = groupcount > 0 && groupsep != 0;
    boolean dec = decsep != 0 && precision > 0;
    char[] cs = s.toCharArray();
    
    for(int i = 0; i < cs.length; i++) {
      if(!Character.isDigit(cs[i])
          && cs[i] != groupsep 
          && cs[i] != decsep)
        num = false;
      else if(!group && cs[i] == groupsep)
        num = false;
      else if(!dec && cs[i] == decsep)
        num = false;
      else if(dec && cs[i] == decsep)
        dsc++;
    }
    return num && dsc <= 1;
  }


	private boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}


	/**
	 * Formata para String o número informado de acordo com
	 * os parâmetros definidos em <code>NumberFormat</code>.
	 * @param d Número a ser formatado.
	 * @return String formatada do número.
	 */
	public String format(double d) {
		return this.formatGroup(this.formatDecimal(d));
	}


	/**
	 * Interpreta o número contido na String informada,
	 * retornando-o caso a interpretação seja bem sucedida.
	 * @param s String contendo um número formatado
	 *   de acordo com os parâmetros de <code>NumberFormat</code>.
	 * @return o número interpretado ou <code>-1</code> caso
	 *   ocorra erro na interpretação.
	 */
	public double parse(String s) {
		if(isEmpty(s)) return -1;

		if(groupsep == '.')
			s = s.replaceAll("\\.", "");
		else
			s = s.replaceAll(String.valueOf(groupsep), "");
		if(decsep != '.')
			s = s.replaceAll(String.valueOf(decsep), ".");

		try {
			return Double.parseDouble(s);
		} catch(NumberFormatException ex) {
			return -1;
		}
	}
	

	/**
	 * Arredonda o número informada para a quantidade
	 * de casas decimais informada.
	 * @param d Número a ser arredondado.
	 * @param decimal quantidade de casas decimais.
	 * @return o número arredondado.
	 */
	public double round(double d, int decimal) {
		if(decimal == 0) return (double) (long) d;
		if(d % (long) d == 0) return d;
		
		double ctr = Math.pow(10, decimal);
		d *= ctr;
		return Math.round(d) / ctr;
	}


	private String formatDecimal(double d) {
		if(decsep == 0 || precision == 0
				|| (d % (long) d == 0 && !zerofill))
			return String.valueOf((long) d);

		String s = String.valueOf(d);
		if(precision > 0 && s.indexOf(".") < s.length() - precision -1) {
			d = this.round(d, precision);
			s = String.valueOf(d);
		}

		if(zerofill && precision > 0) {
			zerofill = false;
			for(int i = 0; i < (s.length() -1) - (s.length() - precision); i++) {
				s = s.concat("0");
			}
		}

		return s.replaceAll("\\.", String.valueOf(decsep));
	}


	private String formatGroup(String s) {
		if(isEmpty(s)) return s;
		if(groupsep == 0) return s;

		int gc = 0;
		char[] cs = s.toCharArray();
		String fs = "";
		boolean dec = !s.contains(String.valueOf(decsep));

		for(int i = cs.length -1; i >= 0; i--) {
			fs = String.valueOf(cs[i]).concat(fs);
			gc++;
			if(cs[i] == decsep) {
				gc = 0;
				dec = true;
			}
			if(dec && gc != 0 && gc % groupcount == 0 && i > 0)
				fs = String.valueOf(groupsep).concat(fs);
		}
		return fs;
	}


	public static void main(String[] args) {
		NumberFormat nf = new NumberFormat();
		String s = "5.968.300,456182";
		System.out.println(s);
		System.out.println(String.valueOf(nf.parse(s)));
		System.out.println("-----------------");

		System.out.println(String.valueOf(nf.round(nf.parse(s), 4)));

		System.out.println(nf.formatDecimal(nf.parse(s)));
		System.out.println(nf.formatDecimal(5968300.345));

		System.out.println("-----------------");

		s = nf.decimalOn(2).zerofill().formatDecimal(5968300.3);
		System.out.println(s);
		System.out.println(nf.formatGroup(s));

		System.out.println("-----------------");

		double d = 5968300.3;
		System.out.println(String.valueOf(d));
		System.out.println(nf.groupingOn(3).groupingBy('.').decimalOn(2).zerofill().format(d));
	}

}
