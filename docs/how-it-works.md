# How it works
Modules:
- `api`: The fatjar module which users interact with.
- `backend`: Contains any code related to the web server powering the code editor.
- `frontend`: Contains any code related to the actual code editor.

Since on-bot-typescript uses Jsr223 (javax.script), Java's scripting engine, we are limited to only a few languages when writing OpMode scripts. We chose JavaScript, and then TypeScript as another layer to make things nicer for developers. TypeScript compiler files are loaded in and the files are compiled into raw JavaScript. These files are then processed by Java's built-in Nashorn JS engine.

## Why should you use this?
Rebuilding your RobotController project can take anywhere from 15s-2m to fully complete. There are multiple "layers" of delays that are applied during the rebuild process (from longest to shortest in terms of execution time, only significant delays listed):
1. Android Studio, Gradle, TeamCode rebuild
2. Android Studio, wireless/wired ADB, app stop -> app reinstall -> app start
3. *(if applicable)* OnBot Java, Gradle, TeamCode rebuild
4. Robot Controller, OpMode start/stop

The use of on-bot-typescript eliminates all the delay layers listed above and is fully production-ready (meaning you don't need a full app reinstall once you are done with development). Additionally, on-bot-typescript provides features that are not provided out-of-the-box by OnBot Java or Android Studio:
- `Hot Reload`: Your OpMode code is reloaded right after you update it through the code editor (1-2 second reload time).
- `Shared Scripts`: You can provide shared scripts that are automatically inherited by any OpMode scripts. Variables, methods, and constants can be declared in these shared scripts.

## Why should you not use this?
If your team is not comfortable writing TypeScript code for OpModes, don't use this.

## Alternatives
- [Fast Load](https://github.com/MatthewOates36/fast-load-plugin/): Allows you to decrease Android Studio ADB reinstall speeds by 80%.
  - Downsides: Requires a full reinstall once done with fast-load development. Can be inconsistent at times. (?) 
  - Upsides: No need to learn a new language. Better IDE support (tab completion, etc).

## Benchmarks
Cold starts (web server load + script engine load):
- TODO

Hot-reloads/saves:
- TODO
