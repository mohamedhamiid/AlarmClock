import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class AlarmManager {
    ArrayList<AlarmClock> alarms;
    private final String timeFormat = "HH:mm:ss";

    private Clip validateTonePath(File filepath){
            try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(filepath)){
                return AudioSystem.getClip();
            } catch (UnsupportedAudioFileException e) {
                System.out.println("Unsupported audio file. Please use .wav file and try again!");
            }
            catch (LineUnavailableException e) {
                System.out.println("Audio is not available");
            } catch (IOException e) {
                System.out.println("Can't find audio file " + filepath);
            }
            return null;
    }
    void CreateNewAlarm(){
        System.out.println("""
                           *****************
                           Create new Alarm
                           *****************
                           """);

        Scanner input = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        LocalTime alarmTime = null;
        boolean validData = false;

        while (!validData){
            System.out.print("""
                               Enter time (HH:MM:SS): 
                               """);
            String inputTime = input.nextLine();
            try{
                alarmTime = LocalTime.parse(inputTime, formatter);
                System.out.print("""
                               Enter alarm tone path (path/audio.wav) (optional: press Enter to skip): 
                               """);
                String inputTonePath = "";
                inputTonePath = input.nextLine();
                File audioFile;
                if(inputTonePath.isEmpty()){
                    audioFile = new File("defaultRingtone.wav");
                }
                else{
                    audioFile = new File(inputTonePath);
                }
                if(validateTonePath(audioFile)!= null){
                    validData = true;
                    alarms.add(new AlarmClock(alarmTime));
                }
            }catch(DateTimeParseException e){
                System.out.println("Invalid time format. Please try again!");
            }
        }

    }

}
