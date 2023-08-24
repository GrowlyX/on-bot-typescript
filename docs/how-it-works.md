# How it works
Modules:
- `api`: The fatjar module which users interact with.
- `backend`: Contains any code related to the web server powering the code editor.
- `frontend`: Contains any code related to the actual code editor.

## Why should you use this?
Rebuilding your RobotController project can take anywhere from 15s-2m to fully complete. There are multiple "layers" of delays that are applied during the rebuild process (from longest to shortest in terms of execution time):
1. Android Studio, Gradle, TeamCode rebuild
2. Android Studio, wireless/wired ADB, app stop -> app reinstall -> app start
4. *(if applicable)* OnBot Java, Gradle, TeamCode rebuild
5. Robot Controller, OpMode start/stop

The use of FTCScript eliminates all the delay layers listed above and is fully production-ready (meaning you don't need a full app reinstall once you are done with development). Additionally, FTCScript provides features that are not provided out-of-the-box by OnBot Java or Android Studio:
- `Hot Reload`: Your OpMode code is reloaded right after you update it through the code editor (1-2 second reload time).
- `Shared Scripts`: You can provide shared scripts that are automatically inherited by any OpMode scripts. Variables, methods, and constants can be declared in these shared scripts.

## Why should you not use this?
Since FTCScript uses Jsr223 (javax.script), Java's scripting engine, we are limited to only a few languages when writing OpMode scripts. We chose to keep native compatibility with Kotlin for FTCScript. If your team is not comfortable with writing Kotlin code for OpModes, don't use this.

## Alternatives
- [Fast Load](https://github.com/MatthewOates36/fast-load-plugin/): Allows you to decrease Android Studio ADB reinstalls by 80%.
  - Downsides: Requires a full reinstall once done with fast-load development. Can be inconsistent at times. (?)

## Benchmarks
TODO
