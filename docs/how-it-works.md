# How it works
Modules:
- `api`: The fatjar module which users interact with.
- `backend`: Contains any code related to the web server powering the code editor.
- `frontend`: Contains any code related to the actual code editor.

OBT uses the Nashorn ScriptEngine (Jsr223) implementation to dynamically compile JavaScript code written as "Scripts" through the web editor. Since this recompilation process is so quick, we're able to provide quick hot-reloading and a consistent, stable, and robust programming environment for FTC teams. On top of JavaScript, we include the TypeScript compiler (using es3) to allow teams to write their code in TypeScript and take advantage of its features (object-oriented, generics, etc). Having TypeScript keeps the code as close to JVM languages in terms of syntax and design as possible.

## Why should you use this?
Rebuilding your RobotController project can take anywhere from 15s-2m to fully complete. There are multiple "layers" of delays that are applied during the rebuild process (from longest to shortest in terms of execution time, only significant delays are listed):
1. Android Studio, Gradle, TeamCode rebuild
2. Android Studio, wireless/wired ADB, app stop -> app reinstall -> app start
3. *(if applicable)* OnBot Java, Gradle, TeamCode rebuild
4. Robot Controller, OpMode start/stop

The use of on-bot-typescript eliminates all the delay layers listed above and is fully production-ready (meaning you don't need a full app reinstall once you are done with development). Additionally, on-bot-typescript provides features that are not provided out-of-the-box by OnBot Java or Android Studio:
- `Hot Reload`: Your OpMode code is reloaded right after you update it through the code editor (1-2 second reload time).
- `Shared Scripts`: You can provide shared scripts that are automatically inherited by any OpMode scripts. Variables, methods, and constants can be declared in these shared scripts.

## Why should you not use this?
If your team is not comfortable writing TypeScript/JavaScript code, don't use this. Additionally, using OBT presents a unique challenge: no tab completion. Since we have a fairly barebones web editor (monaco editor), nice IDE features such as tab completion, a lot of the major syntax highlighting features, etc. are not present.

## Alternatives
- [Fast Load](https://github.com/MatthewOates36/fast-load-plugin/): Allows you to decrease Android Studio ADB reinstall speeds by 80%.
  - Downsides: Requires a full reinstall once done with fast-load development. Can be inconsistent at times. (?) 
  - Upsides: No need to learn a new language. Better IDE support (tab completion, etc).

## Benchmarks
Cold starts (web server load + script engine load): `~6 seconds`
- `Web Server`: Loaded asynchronously in around 2 seconds
- `Script Engine`: Blocks the OpMode thread until fully ready (if not already initialized). Takes around 6 seconds.
Hot-reloads/saves: `90ms` 
