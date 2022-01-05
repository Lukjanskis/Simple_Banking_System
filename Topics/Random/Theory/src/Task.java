// You can experiment here, it won’t be checked

import java.util.Random;

public class Task {
  public static void main(String[] args) {
    Random random = new Random(100000);
    String CONST = "400000";
    System.out.println("Your card has been created");
    String finalCardNumber = CONST;
    for (int i = 0; i < 10; i++) {
      int cardNumber = random.nextInt(10);
      String s = Integer.toString(cardNumber);
      finalCardNumber = finalCardNumber + s;
      if (i == 9){
        System.out.println(finalCardNumber);
      }
    }
    System.out.println(finalCardNumber);
  }
}
