package zunpiau.sqljudger.web.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomStringGenerator {

    private static final char[] CHAR_SHEET = "abcdefghjkmnpqrstuvwxyz".toCharArray();
    private static final int CHARS_LENGTH = CHAR_SHEET.length;
    private final SecureRandom random;

    public RandomStringGenerator() {
        random = new SecureRandom();
    }

    public String generate(String prefix, int length) {
        return prefix + generate(length);
    }

    public String generate(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must bigger than zero.");
        }
        StringBuilder builder = new StringBuilder(length);
        while (length-- > 0) {
            builder.append(CHAR_SHEET[random.nextInt(CHARS_LENGTH)]);
        }
        return builder.toString();
    }

}
