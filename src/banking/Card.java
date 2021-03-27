package banking;
import java.util.Random;

public class Card {
    private String cardNo;
    private String pin;
    private static Random random = new Random(1);
    //default constructor
    public Card(){}
    //custom constructor
    public Card(String cardNo, String pin) {
        this.cardNo = cardNo;
        this.pin = pin;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getPin() {
        return pin;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public static String setRandomCardNo() {
        StringBuilder sb = new StringBuilder();
        sb.append("400000");
        for(int i = 0; i < 9; i++) {
            sb.append(String.valueOf(random.nextInt(9)));
        }
        int sum = getLuhnSum(sb.toString());
        if(10 - (sum%10) != 10) {
            sb.append(String.valueOf(10-(sum%10)));
        } else {
            sb.append(String.valueOf(0));
        }
        return sb.toString();

    }

    public static String setRandomPin() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            sb.append(String.valueOf(random.nextInt(9)));
        }
        return sb.toString();
    }

    public static int getLuhnSum(String luhnStr) {
        int[] checksum = new int[15];
        int sum = 0;

        for(int i = 0; i < 15; i++) {
            checksum[i] = Character.getNumericValue(luhnStr.charAt(i));
        }

        for(int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                checksum[i] = checksum[i] * 2;
            }
        }

        for(int i = 0; i < 15; i++) {
            if(checksum[i] > 9) {
                checksum[i] = checksum[i] - 9;
            }
        }
        for(int i = 0; i < 15; i++) {
            sum+=checksum[i];
        }
        return sum;
    }

}