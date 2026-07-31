import java.util.Scanner;

public class todolist{
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);
        String[]task=new String[10];
        int count=0;
        int choice;

        do{
            System.out.println("\n====TO DO LIST====");
            System.out.println("1.Add Task");
            System.out.println("2.update");
            System.out.println("3.delete");
            System.out.println("4.view");
            System.out.println("5.exit");


            System.out.print("Enter the choice:"); 
            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    System.out.print("Enter the task:");
                    task[count]=sc.nextLine();
                    count++;
                    System.out.println("Task added sucessfully");
                    break;

                case 2:
                    System.out.print("Enter task number:");
                    int update = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new task:");
                    task[update-1]=sc.nextLine();
                    System.out.println("Task update sucessfully");
                    break;
                case 3:
                    System.out.print("Enter task number");
                    int delete= sc.nextInt();
                    for (int i=delete-1;i<count-1;i++){
                        task[i]=task[i+1];
                    }
                    count --;
                    System.out.println("tasks deleted successfully");
                    break;

                case 4:
                    System.out.println("\n tasks  list:");
                    for(int i =0;i< count;i++){
                        System.out.println((i+1)+"."+task[i]);
                
                    }
                    break;

                case 5:
                    System.out.println("thankyou");
                    break;
                    default:
                        System.out.println("invalid choice");


            }
        }while(choice!=5);
        sc.close();
    }
}

