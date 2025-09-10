import javax.sound.sampled.Clip;
import java.time.LocalTime;

public class AlarmClock {
    private Clip clip;
    private final LocalTime alarmTime;

    AlarmClock(LocalTime alarmTime){
        this.alarmTime = alarmTime;
    }

    AlarmClock(LocalTime alarmTime, Clip clip){
        this.alarmTime = alarmTime;
        this.clip = clip;
    }
}
