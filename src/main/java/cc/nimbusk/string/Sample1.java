package cc.nimbusk.string;

public class Sample1 {

    public static String countConsectiveChars(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        char currentChar = input.charAt(0);
        int count = 1;
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == currentChar) {
                count++;
            } else {
                result.append(currentChar).append("_").append(count).append("_");
                currentChar = input.charAt(i);
                count = 1;
            }
        }
        result.append(currentChar).append("_").append(count);
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(countConsectiveChars("a"));
        System.out.println(countConsectiveChars("aaabbbcccd"));
    }

}
