package br.com.bb.dinop.arqRedeDinop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author F8712906
 *
 */
public class RemoveCaracteresEstendidos {
	
	public String[] REPLACES = {"a", "e", "i", "o", "u", "c", "n", "A", "E", "I", "O", "U", "C", "N", "_", ""};
	public Pattern[] PATTERNS = null;
	
	public void compilePatterns() {  
		PATTERNS = new Pattern[REPLACES.length];  
		PATTERNS[0]  = Pattern.compile("[âãáàä]");  
		PATTERNS[1]  = Pattern.compile("[éèêë]");  
		PATTERNS[2]  = Pattern.compile("[íìîï]");  
		PATTERNS[3]  = Pattern.compile("[óòôõö]");  
		PATTERNS[4]  = Pattern.compile("[úùûü]");  
		PATTERNS[5]  = Pattern.compile("[ç]");
		PATTERNS[6]  = Pattern.compile("[ñ]");
		PATTERNS[7]  = Pattern.compile("[ÂÃÁÀÄ]");  
		PATTERNS[8]  = Pattern.compile("[ÉÈÊË]");  
		PATTERNS[9]  = Pattern.compile("[ÍÌÎÏ]");  
		PATTERNS[10] = Pattern.compile("[ÓÒÔÕÖ]");  
		PATTERNS[11] = Pattern.compile("[ÚÙÛÜ]");  
		PATTERNS[12] = Pattern.compile("[Ç]");
		PATTERNS[13] = Pattern.compile("[Ñ]");
		PATTERNS[14] = Pattern.compile("[/^/~´`'!@#$%¨&*()¹²³£¢¬º°ª§@?]");
		PATTERNS[15] = Pattern.compile("[\\s]");
	}
	
  
	
	/**
	 * Substitui os caracteres acentuados por nao acentuados.
	 * @param text
	 * @return
	 */
	public String replaceSpecial(String text) {  
		if (PATTERNS == null) {  
			compilePatterns();  
		}  
		String result = text;  
		for (int i = 0; i < PATTERNS.length; i++) {  
			Matcher matcher = PATTERNS[i].matcher(result);  
			result = matcher.replaceAll(REPLACES[i]);  
		}  
		return result;  
	}
}