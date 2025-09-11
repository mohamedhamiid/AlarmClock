public class Main {
    public static void main(String[] args) {
        AlarmManager alarmManager = new AlarmManager();

        boolean keepRunning = true;
        while (keepRunning) {

            System.out.println("""
                    |-------------------------------|
                    | Welcome to Alarm Clock app :) |
                    |-------------------------------|
                    | Options:                      |
                    | 1- Set New Alarm              |
                    | 2- Stop Existing Alarm        |
                    |-------------------------------|
                    """);
            System.out.print("Enter option number: ");
            int inputOption = InputManager.nextInt();

            try {
                Options option = Options.values()[inputOption - 1];
                switch (option) {
                    case NEW_ALARM -> {
                        alarmManager.createNewAlarm();
                    }
                    case STOP_EXISTING_ALARM -> {
                        alarmManager.listAlarms();
                        if (alarmManager.hasAlarms()) {
                            System.out.print("Enter Alarm ID to stop (press 0 to back) : ");
                            int id = InputManager.nextInt();
                            if(id == 0){
                                break;
                            }
                            alarmManager.stopAlarmById(id);
                        }
                    }
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println("Not valid option. Please try again");
            }
        }
    }

}
