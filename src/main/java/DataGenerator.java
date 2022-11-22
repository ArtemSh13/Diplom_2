import java.util.Random;

public class DataGenerator {
    private static Random random = new Random();

    private static int randInt(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String getRandomEmail () {
        return "email" + random.nextInt(999999) + "@yandex.ru";
    }

    public static String getRandomPassword () {
        return "p@s$w0RD" + random.nextInt(999999);
    }

    public static String getLoginPasswordJson (String login, String password) {
        return "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    }

    public static String getRandomName () {
        return "name" + random.nextInt(999999);
    }
}
