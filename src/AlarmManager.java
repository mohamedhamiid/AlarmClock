import java.io.File;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the creation, listing, and stopping of alarms.
 * Acts as the controller in the application, coordinating between user input
 * and alarm logic.
 */
public class AlarmManager {
    private static int noOfActiveAlarms;
    /**
     * The time zone used for all alarms. Adjust as needed for your region.
     */
    public static ZoneId ourZone = ZoneId.of("+3");
    private final List<AlarmState> alarms;
    private int nextId = 1;

    /**
     * Constructs a new AlarmManager with no active alarms.
     */
    AlarmManager() {
        noOfActiveAlarms = 0;
        alarms = new ArrayList<>();
    }

    /**
     * Prompts the user to create a new alarm, including time and optional audio
     * file.
     * Handles input validation and starts the alarm thread.
     */
    void createNewAlarm() {
        System.out.print("""

                |-------------------|
                | Create new Alarm  |
                |-------------------|
                """);

        String timeFormat = "HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        boolean validData = false;

        while (!validData) {
            System.out.print("Enter time (HH:MM:SS) ... press \"e\" to back: ");
            String inputTime = InputManager.nextLine();
            if (inputTime.equalsIgnoreCase("e")) {
                return;
            }
            try {
                LocalTime alarmTime = LocalTime.parse(inputTime, formatter);
                System.out.print("""
                        Enter alarm tone path (path/audio.wav) (optional: press Enter to skip):
                        """);
                String inputTonePath = InputManager.nextLine();
                File audioFile = inputTonePath.isEmpty() ? new File("defaultRingtone.wav") : new File(inputTonePath);

                validData = true;
                // Create and start the alarm thread
                AlarmClock alarm = new AlarmClock(alarmTime, audioFile, ourZone, nextId);
                Thread alarmThread = new Thread(alarm, "alarm-" + nextId);
                AlarmState state = new AlarmState(nextId++, alarm, alarmThread, alarm.getTarget());
                alarms.add(state);
                noOfActiveAlarms++;
                alarmThread.start();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please try again!");
            }
        }
    }

    /**
     * Lists all currently active alarms with their IDs and trigger times.
     */
    public void listAlarms() {
        if (alarms.isEmpty()) {
            System.out.println("No active alarms.");
            return;
        }
        System.out.println("Active alarms:");
        for (AlarmState s : alarms) {
            System.out.println("- ID " + s.id() + ": triggers at " + s.targetTime());
        }
    }

    /**
     * Stops and removes the alarm with the given ID, if it exists.
     * 
     * @param id The ID of the alarm to stop
     */
    public void stopAlarmById(int id) {
        for (int i = 0; i < alarms.size(); i++) {
            AlarmState s = alarms.get(i);
            if (s.id() == id) {
                try {
                    s.alarm().stop();
                    s.thread().interrupt();
                } finally {
                    alarms.remove(i);
                    noOfActiveAlarms = Math.max(0, noOfActiveAlarms - 1);
                    nextId = noOfActiveAlarms + 1;
                }
                System.out.println("Alarm " + id + " stopped.");
                return;
            }
        }
        System.out.println("No alarm found with ID " + id);
    }

    /**
     * Checks if there are any active alarms.
     * 
     * @return true if there is at least one alarm, false otherwise
     */
    public boolean hasAlarms() {
        return !alarms.isEmpty();
    }

    public static void printTimeNow(){
        ZonedDateTime now = ZonedDateTime.now(ourZone);
        System.out.printf("%02d:%02d:%02d",
                now.getHour(), now.getMinute(), now.getSecond());
    }
}
