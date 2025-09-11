/**
 * Entry point for the Alarm Clock application.
 * Handles the main menu loop and delegates user actions to the AlarmManager.
 */
public class Main {
    /**
     * Main method to start the Alarm Clock app.
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        AlarmManager alarmManager = new AlarmManager();

        boolean keepRunning = true;
        while (keepRunning) {

            // Display the main menu
            System.out.println("""
                    |-------------------------------|
                    | Welcome to Alarm Clock app :) |
                    |-------------------------------|
                    | Options:                      |
                    | 1- Get time now (GMT+3)       |
                    | 2- Set New Alarm              |
                    | 3- Get active alarms          |
                    | 4- Stop Existing Alarm        |
                    |-------------------------------|
                    """);
            System.out.print("Enter option number: ");
            int inputOption = InputManager.nextInt();

            try {
                Options option = Options.values()[inputOption - 1];
                switch (option) {
                    case GET_TIME_NOW -> {
                        // Get time now
                        System.out.println("Press \"e\" to exit");
                        String input = "";

                        // Create New thread to display time
                        Thread th = new Thread(()->{
                            while(true) {
                                System.out.print("\r");
                                AlarmManager.printTimeNow();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        });
                        th.start();
                        while (!input.equalsIgnoreCase("e")){
                            input = InputManager.nextLine();
                        }
                        th.interrupt();

                    }
                    case NEW_ALARM -> {
                        // Create a new alarm
                        alarmManager.createNewAlarm();
                    }
                    // list all alarms
                    case GET_ACTIVE_ALARMS -> {
                        alarmManager.listAlarms();
                        Thread.sleep(1000);
                    }
                    case STOP_EXISTING_ALARM -> {
                        // List and stop an existing alarm by ID
                        alarmManager.listAlarms();
                        if (alarmManager.hasAlarms()) {
                            System.out.print("Enter Alarm ID to stop (press 0 to back) : ");
                            int id = InputManager.nextInt();
                            if (id == 0) {
                                break;
                            }
                            alarmManager.stopAlarmById(id);
                        }
                    }
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println("Not valid option. Please try again");
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted");
            }
        }
    }

}
