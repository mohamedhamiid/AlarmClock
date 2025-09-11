import java.io.File;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AlarmManager {
    private static int noOfActiveAlarms;
    public static ZoneId ourZone = ZoneId.of("+3");
    private final List<AlarmState> alarms;
    private final String timeFormat = "HH:mm:ss";
    private int nextId = 1;

    AlarmManager() {
        noOfActiveAlarms = 0;
        alarms = new ArrayList<>();
    }

    void createNewAlarm() {
        System.out.print("""
                
                |-------------------|
                | Create new Alarm  |
                |-------------------|
                """);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        boolean validData = false;

        while (!validData) {
            System.out.print("Enter time (HH:MM:SS) ... press \"e\" to back: ");
            String inputTime = InputManager.nextLine();
            if(inputTime.equalsIgnoreCase("e")){
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
                AlarmClock alarm = new AlarmClock(alarmTime, audioFile, ourZone, this::handleTimeReaching,nextId);
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

    public void handleTimeReaching(int alarmId){
        System.out.print("\nPlease enter \"stop\" to stop the alarm: ");
        String input = "";
        while (!input.equals("stop")){
            input = InputManager.nextLine().toLowerCase();
        }
        stopAlarmById(alarmId);
    }

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
                    nextId = noOfActiveAlarms+1;
                }
                System.out.println("Alarm " + id + " stopped.");
                return;
            }
        }
        System.out.println("No alarm found with ID " + id);
    }

    public boolean hasAlarms() {
        return !alarms.isEmpty();
    }
}
