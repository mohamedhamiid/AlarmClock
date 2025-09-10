import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class AlarmManager {
    private static int NoOfActiveAlarms;
    public static ZoneId ourZone = ZoneId.of("+3");
    private ArrayList<AlarmState> alarms ;
    private final String timeFormat = "HH:mm:ss";


    AlarmManager(){
        NoOfActiveAlarms = 0;
        alarms = new ArrayList<>();
    }

    void CreateNewAlarm(){
        System.out.println("""
                           *****************
                           Create new Alarm
                           *****************
                           """);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        LocalTime alarmTime = null;
        boolean validData = false;

        while (!validData){
            System.out.print("Enter time (HH:MM:SS): ");
            String inputTime = InputManager.nextLine();
            try{
                alarmTime = LocalTime.parse(inputTime, formatter);
                System.out.print("""
                               Enter alarm tone path (path/audio.wav) (optional: press Enter to skip): 
                               """);
                String inputTonePath = "";
                inputTonePath = InputManager.nextLine();
                File audioFile;
                if(inputTonePath.isEmpty()){
                    audioFile = new File("defaultRingtone.wav");
                }
                else{
                    audioFile = new File(inputTonePath);
                }
                        NoOfActiveAlarms++;
                        validData = true;
                        AlarmClock alarm = new AlarmClock(alarmTime, audioFile);
                        Thread alarmThread = new Thread(alarm);
                        alarms.add(new AlarmState(alarm, alarmThread));
                        alarmThread.start();
            }catch(DateTimeParseException e){
                System.out.println("Invalid time format. Please try again!");
            }
        }

    }

}
