package br.com.bb.dinop.arqRedeDinop.aspectos;

/**
 * The Class ControleFormatExcecao.
 */
public aspect ControleFormatExcecao {

//	declare soft: java.text.ParseException : within(br.com.bb.dinop.arqRedeDinop..*);

	/**
	 * Format int.
	 *
	 * @param i
	 *            the i
	 */
	pointcut formatInt(long i):
		call(* java.text.NumberFormat.format(long))
		&& within(br.com.bb.dinop.arqRedeDinop..*)
		&& ! within (br.com.bb.dinop.arqRedeDinop.aspectos.*)
		&& args(i);

	/**
	 * Format double.
	 *
	 * @param d
	 *            the d
	 */
	pointcut formatDouble(double d):
		call(* java.text.NumberFormat.format(double))
		&& within(br.com.bb.dinop.arqRedeDinop..*)
		&& ! within (br.com.bb.dinop.arqRedeDinop.aspectos.*)
		&& args(d);

	/**
	 * Format date.
	 *
	 * @param d
	 *            the d
	 */
	pointcut formatDate(java.util.Date d):
		call(* java.text.SimpleDateFormat.format(java.util.Date))
		&& within(br.com.bb.dinop.arqRedeDinop..*)
		&& ! within (br.com.bb.dinop.arqRedeDinop.aspectos.*)
		&& args(d);

//	/**
//	 * Around.
//	 *
//	 * @param i
//	 *            the i
//	 * @return the string
//	 */
//	String around(long i): formatInt(i){
//		try {
//			return proceed(i);
//		}catch (Exception nfe){
//			return "";
//		}
//	}

//	/**
//	 * Around.
//	 *
//	 * @param d
//	 *            the d
//	 * @return the string
//	 */
//	String around(double d): formatDouble(d){
//		try {
//			return proceed(d);
//		}catch (Exception nfe){
//			return "";
//		}
//	}

//	/**
//	 * Around.
//	 *
//	 * @param d
//	 *            the d
//	 * @return the string
//	 */
//	String around(java.util.Date d): formatDate(d){
//		try {
//			return proceed(d);
//		}catch (Exception nfe){
//			return "ND";
//		}
//	}
}
