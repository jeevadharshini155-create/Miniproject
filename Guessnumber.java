import java.util.Scanner;
public class Guessnumber {
    public static void main(String[]args){
        Scanner sc=new Scanner(System.in);
        int secretNumber = 5;
        int guess;
        System.out.println("===Guess the number Game===");
        
        while(true){
            System.out .print("Enter your guess:");
            guess = sc.nextInt();
            if(guess==secretNumber){
                System.out.println("congratulations! you won");
                break;
            }else if(guess>secretNumber)

        
                {
                System.out.println("too High!");
            }else{
                System.out.println("too small!");
            }
        

            }
        }

    }
    

