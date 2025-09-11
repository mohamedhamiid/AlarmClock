import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.*;

public class AlarmClock implements Runnable {
    private final int id;
    private final Callback cb;
    private final File audioFile;
    private Clip clip;
    private final ZoneId zone;
    private final ZonedDateTime target;
    private volatile boolean stopped = false;

    AlarmClock(LocalTime alarmTime, File audioFile, ZoneId zone, Callback cb, int id) {
        this.audioFile = audioFile;
        this.zone = zone;
        this.cb = cb;
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

    public ZonedDateTime getTarget() {
        return target;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                ZonedDateTime now = ZonedDateTime.now(zone);
                if (!now.isBefore(target)) {
                    break;
                }
                System.out.printf("\rAlarm-%d: %02d:%02d:%02d",
                        id,now.getHour(), now.getMinute(), now.getSecond());
                Thread.sleep(1000);
            }
        } catch (InterruptedException ie) {
            // thread interrupted -> stop early
            return;
        }

        if (!stopped) {
            playSound();
            // cb.timeReached(id);

        }
    }

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
}
