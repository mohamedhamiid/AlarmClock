
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.*;

/**
 * Represents a single alarm that triggers at a specific time and plays a sound.
 * Implements Runnable so it can be run on a separate thread.
 */
public class AlarmClock implements Runnable {
    /** The audio file to play when the alarm triggers. */
    private final File audioFile;
    /** The audio clip instance for playback. */
    private Clip clip;
    /** The time zone for the alarm. */
    private final ZoneId zone;
    /** The target date and time when the alarm should trigger. */
    private final ZonedDateTime target;
    /** Unique identifier for this alarm. */
    private final int id;
    /** Flag to indicate if the alarm has been stopped. */
    private volatile boolean stopped = false;

    /**
     * Constructs a new AlarmClock.
     * 
     * @param alarmTime The time of day to trigger the alarm
     * @param audioFile The audio file to play
     * @param zone      The time zone for the alarm
     * @param id        Unique identifier for this alarm
     */
    AlarmClock(LocalTime alarmTime, File audioFile, ZoneId zone, int id) {
        this.audioFile = audioFile;
        this.zone = zone;
        this.id = id;

        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime candidate = now.withHour(alarmTime.getHour())
                .withMinute(alarmTime.getMinute())
                .withSecond(alarmTime.getSecond())
                .withNano(0);
        if (candidate.isBefore(now)) {
            candidate = candidate.plusDays(1);
        }
        this.target = candidate;

        Duration until = Duration.between(now, target);
        long hours = until.toHours();
        long minutes = until.minusHours(hours).toMinutes();
        long seconds = until.minusHours(hours).minusMinutes(minutes).toSeconds();
        System.out.println("Alarm is set for \" " + hours + " hours: " + minutes + " minutes: " + seconds
                + " seconds \" from now");
    }

    /**
     * Gets the target date and time when the alarm will trigger.
     * 
     * @return The ZonedDateTime of the alarm
     */
    public ZonedDateTime getTarget() {
        return target;
    }

    /**
     * Main run loop for the alarm thread. Waits until the target time, then plays
     * the sound.
     */
    @Override
    public void run() {
        try {
            while (!stopped) {
                ZonedDateTime now = ZonedDateTime.now(zone);
                if (!now.isBefore(target)) {
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException ie) {
            // thread interrupted -> stop early
            return;
        }

        if (!stopped) {
            playSound();
            // Optionally notify callback if needed
        }
    }

    /**
     * Plays the alarm sound in a loop until stopped.
     */
    private void playSound() {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file. Please use .wav file and try again!");
        } catch (LineUnavailableException e) {
            System.out.println("Audio is not available");
        } catch (IOException e) {
            System.out.println("Can't find audio file " + audioFile);
        }
    }

    /**
     * Stops the alarm and releases any audio resources.
     */

    public void stop() {
        stopped = true;
        if (clip != null) {
            try {
                if (clip.isRunning())
                    clip.stop();
                if (clip.isOpen()) {
                    clip.flush();
                    clip.close();
                }
            } catch (Exception ignore) {
            }
        }
    }
}
