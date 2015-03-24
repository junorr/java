package br.com.bb.dinop.arqRedeDinop.util;

/**
 * @author F8712906
 */
public class URIComponentUtils {
    /**
     * In case of incoming Strings - encoded with js encodeURIComponent() e.g. ö -> %C3%B6 - it is not sufficient to set
     * CharacterEncoding on the ResponseWriter accordingly. Passing the uri to decodeURIComponent(String encodedURI)
     * decodes e.g. %C3%B6 back to ö.
     * 
     * @return decoded uri String
     * @param encoded
     *            uri String
     */
    public String decodeURIComponent(final String encodedURI) {
        char actualChar;
        final StringBuffer buffer = new StringBuffer();
        int bytePattern, sumb = 0;
        for (int i = 0, more = -1; i < encodedURI.length(); i++) {
            actualChar = encodedURI.charAt(i);
            switch (actualChar) {
            case '%': {
                actualChar = encodedURI.charAt(++i);
                final int hb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character
                        .toLowerCase(actualChar) - 'a') & 0xF;
                actualChar = encodedURI.charAt(++i);
                final int lb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character
                        .toLowerCase(actualChar) - 'a') & 0xF;
                bytePattern = (hb << 4) | lb;
                break;
            }
            case '+': {
                bytePattern = ' ';
                break;
            }
            default: {
                bytePattern = actualChar;
            }
            }
            // * Decode byte bytePattern as UTF-8, sumb collects incomplete
            // chars *//*
            if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
                sumb = (sumb << 6) | (bytePattern & 0x3f);
                if (--more == 0) {
                    buffer.append((char) sumb);
                }
            } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
                buffer.append((char) bytePattern);
            } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
                sumb = bytePattern & 0x1f;
                more = 1;
            } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
                sumb = bytePattern & 0x0f;
                more = 2;
            } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
                sumb = bytePattern & 0x07;
                more = 3;
            } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
                sumb = bytePattern & 0x03;
                more = 4;
            } else { // 1111110x
                sumb = bytePattern & 0x01;
                more = 5;
            }
        }
        return buffer.toString();
    }

    public String encodeURIComponent(final String uri) {
        throw new UnsupportedOperationException("Ainda não implementado!");
    }
}
