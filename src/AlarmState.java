import java.time.ZonedDateTime;

public record AlarmState(int id, AlarmClock alarm, Thread thread, ZonedDateTime targetTime) {
}
