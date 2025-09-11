# Alarm Clock (Console, Java)

Alarm Clock is a simple console-based Java application that lets you set alarms, list active alarms, stop a specific alarm by ID, and view the current time in a configurable time zone (default GMT+3). Alarms ring using a looping WAV audio file until you stop them via the menu.

## Features

- Create alarms for the next occurrence of a given time (HH:mm:ss)
- List active alarms with unique IDs and their trigger timestamps
- Stop any active alarm by its ID
- Show current time (GMT+3 by default) with live updates until you exit
- Custom or default ringtone (WAV only), looping until stopped

## Requirements

- Java 17+ (or a compatible JDK with `javax.sound.sampled` support)
- Windows PowerShell (commands below are shown for PowerShell)
- A WAV file for the ringtone (a `defaultRingtone.wav` is included)

## Project structure

```
AlarmClock/
├─ defaultRingtone.wav
├─ README.md
└─ src/
	 ├─ AlarmClock.java       # Runnable alarm: schedules next occurrence and plays sound
	 ├─ AlarmManager.java     # Creates, lists, stops alarms; prints time now
	 ├─ AlarmState.java       # Record holding alarm state (id, thread, target time)
	 ├─ InputManager.java     # Console input helpers (int/line parsing)
	 ├─ Main.java             # App entry point and main menu loop
	 └─ Options.java          # Menu options enum
```

## Build and run

From the project root in Windows PowerShell:

```powershell
# Create output folder
New-Item -ItemType Directory -Force -Path out | Out-Null

# Compile all sources
javac -d out src/*.java

# Run the app
java -cp out Main
```

If you see an error about Java not found, ensure the JDK `bin` folder is on your PATH or run with the full path to `javac`/`java`.

## Usage

When you run the app, you’ll see a menu like this:

```
|-------------------------------|
| Welcome to Alarm Clock app :) |
|-------------------------------|
| Options:                      |
| 1- Get time now (GMT+3)       |
| 2- Set New Alarm              |
| 3- Get active alarms          |
| 4- Stop Existing Alarm        |
|-------------------------------|
```

- 1 – Get time now (GMT+3):
	- Shows a live-updating clock. Press `e` then Enter to exit the view.

- 2 – Set New Alarm:
	- Enter the time in `HH:mm:ss` (24-hour). If the time is earlier than now, the alarm is scheduled for the next day.
	- Optionally enter a path to a `.wav` file for a custom ringtone, or press Enter to use `defaultRingtone.wav`.

- 3 – Get active alarms:
	- Lists all currently scheduled alarms with their IDs and target timestamps.

- 4 – Stop Existing Alarm:
	- Lists active alarms. Enter the alarm ID to stop (or `0` to go back). The alarm sound stops immediately and is removed from the active list.

## Time zone

The app uses a static time zone set to GMT+3:

```java
public static ZoneId ourZone = ZoneId.of("+3");
```

You can change this in `AlarmManager.java` if you prefer a specific region (e.g., `ZoneId.of("Europe/Moscow")`) or the system default (`ZoneId.systemDefault()`).

## Audio notes

- Format: WAV (`javax.sound.sampled` works reliably with PCM WAV files)
- Default file: `defaultRingtone.wav` in the project root
- Custom file: provide a full or relative path when prompted
- The audio clip loops continuously until you stop the alarm via the menu

## Design notes

- The app is structured with a light MVC flavor:
	- Model: `AlarmClock`, `AlarmState`
	- Controller: `AlarmManager`, `Main`
	- View: Console I/O is currently mixed across `Main` and `AlarmManager`
- Alarms are run on their own threads and scheduled to the next occurrence of the given time using `ZonedDateTime`.
- Only the main thread should manage input; alarm threads do not read from the console.

## Troubleshooting

- No sound plays:
	- Ensure your WAV file is readable and supported (PCM WAV). Some compressed WAVs may not play.
	- Close other apps using the audio device.

- “Invalid time format” errors:
	- Enter time as `HH:mm:ss` with leading zeros (e.g., `07:05:00`).

- Multiple lines while showing time:
	- The console uses standard printing; in-place updates may vary across terminals. This app keeps it simple and portable.

## Next steps (optional improvements)

- Separate a dedicated view layer to fully align with MVC
- Persist alarms between runs
- Add unit tests for scheduling logic
- Support stopping all alarms or pausing
- Add validation for audio file path/format before creating alarm

---

MIT-style licensing or contribution guidelines can be added here if needed.

# AlarmClock