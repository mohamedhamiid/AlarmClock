import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;

public class AlarmClock implements Runnable {
    private File audioFile;
    private Clip clip;
    private final LocalTime alarmTime;

    AlarmClock(LocalTime alarmTime, File audioFile) {
        this.alarmTime = alarmTime;
        this.audioFile = audioFile;

        System.out.println("Alarm is set for \" " +
                            (alarmTime.getHour() - LocalTime.now(AlarmManager.ourZone).getHour()) % 24 +
                            " hours: " +
                            (alarmTime.getMinute() - LocalTime.now(AlarmManager.ourZone).getMinute()) % 60 +
                            " minutes: " +
                            (alarmTime.getSecond() - LocalTime.now(AlarmManager.ourZone).getSecond()) % 60 +
                            " seconds \" from now"
                            );
    }

    @Override
    public void run(){
        while (LocalTime.now(AlarmManager.ourZone).isBefore(alarmTime)){
            System.out.printf("\r%02d:%02d:%02d",
                              LocalTime.now(AlarmManager.ourZone).getHour() % 24,
                              LocalTime.now(AlarmManager.ourZone).getMinute() % 60,
                              LocalTime.now(AlarmManager.ourZone).getSecond() % 60
            );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
            }
        }

        playSound();
        System.out.println("\nEnter \"stop\" to stop alarm");
        String input = "";
        while (!input.equals("stop")){
            input = InputManager.nextLine();
        }
        System.out.println("Alarm stopped");;
    }
    void playSound(){
        try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)){
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file. Please use .wav file and try again!");
        }
        catch (LineUnavailableException e) {
            System.out.println("Audio is not available");
        } catch (IOException e) {
            System.out.println("Can't find audio file " + audioFile);
        }
    }
}
