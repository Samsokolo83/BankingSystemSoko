package banking;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int userResp;
        boolean exit = false;
        boolean logOut = true;
        DB database = new DB();
        // create sqllite db !exists()
        File dbFile = new File("card.s3db");
        if(!dbFile.exists()) {
            if (args[1].equals("card.s3db")) {
                boolean bval = dbFile.setWritable(true);

                database.createNewDataBase("card.s3db");
                database.createNewTable();
            }
        } else {
            database.createNewTable();
        }
        while(!exit) {

            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            userResp = sc.nextInt();
            switch(userResp) {
                case 1:
                    Card card = new Card(Card.setRandomCardNo(),Card.setRandomPin());
                    card.setRandomCardNo();
                    card.setRandomPin();
                   // cardList.add(card);  // deprecated
                    database.insertNewCard(card.getCardNo(), card.getPin());
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(card.getCardNo());
                    System.out.println("Your card PIN:");
                    System.out.println(card.getPin());
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    StringBuilder cardNoString = new StringBuilder(sc.next());
                    System.out.println("Enter your PIN:");
                    StringBuilder pinString = new StringBuilder(sc.next());
                    logOut = !DB.isValidCard(cardNoString.toString(), pinString.toString());
                    if(logOut) {
                            System.out.println("Wrong card number or PIN!");
                            break;
                    } else {
                        System.out.println("You have successfully logged in!");
                    }
                    while(!logOut) {
                        System.out.println("");
                        System.out.println("1. Balance");
                        System.out.println("2. Add income");
                        System.out.println("3. Do transfer");
                        System.out.println("4. Close account");
                        System.out.println("5. Log out");
                        System.out.println("0. Exit");
                        int logInResponse = sc.nextInt();


                        switch(logInResponse) {
                            case 1:
                                System.out.println("Balance: " + DB.getBalance(cardNoString.toString()));
                                break;
                            case 2:
                                System.out.println("Enter income:");
                                int income = sc.nextInt();
                                DB.addIncome(income,cardNoString.toString());
                                System.out.println("Income was added!");
                                break;
                            case 3:
                                System.out.println("Transfer");
                                System.out.println("Enter card number:");
                                String transferAcc = sc.next();
                                int checksum = Card.getLuhnSum(transferAcc.substring(0,transferAcc.length()-1));
                                if((checksum + Character.getNumericValue(transferAcc.charAt(transferAcc.length() - 1))) % 10 != 0 ) {
                                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                            }  else {
                                    if(DB.isRegisteredCard(transferAcc)) {
                                        if(!transferAcc.equals(cardNoString.toString())) {
                                            System.out.println("Enter how much money you want to transfer:");
                                            int transferAmount = sc.nextInt();
                                            int balance = Integer.parseInt(DB.getBalance(cardNoString.toString()));
                                            System.out.println(transferAmount + " " + balance);
                                            if( balance >= transferAmount) {
                                                DB.addIncome(transferAmount,transferAcc);
                                                DB.addIncome(-transferAmount,cardNoString.toString());
                                            } else {
                                                System.out.println("Not enough money!");
                                            }
                                        } else {
                                            System.out.println("You can't transfer money to the same account!");
                                        }
                                    } else {
                                        System.out.println("Such a card does not exist.");
                                    }
                                }
                                break;
                            case 4:
                                DB.deleteAccount(cardNoString.toString());
                                System.out.println("The account has been closed!");
                                break;
                            case 5:
                                System.out.println("You have successfully logged out!");
                                logOut = true;
                                break;
                            case 0:
                                System.out.println("Bye!");
                                logOut = true;
                                exit = true;
                                break;
                            default:
                                System.out.println("Wrong option selected, please try again");
                                break;

                    }
                    }
                    break;

                case 0:
                    System.out.println("Bye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong option selected, please try again");
                    break;



            }
        }
    }
}