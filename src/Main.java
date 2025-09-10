import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        AlarmManager alarmManager = new AlarmManager();

        System.out.println("Hello !");
        System.out.println("""
                            Options:
                            1- Set New Alarm
                            2- Stop Existing Alarm
                            """);
        boolean validOption = false;

        while(!validOption) {
            System.out.print("Enter option number: ");
            int inputOption = InputManager.nextInt();

            try{
                Options option = Options.values()[inputOption-1];
                validOption = true;
                switch (option) {
                    case NEW_ALARM :
                        System.out.println("Creating new alarm...");
                        alarmManager.CreateNewAlarm();
                        break;
                }

            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Not valid option. Please try again");
            }

        }

    }

}
