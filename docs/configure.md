# Configure

## Configure Dependency
Current Version: `0.2.4-SNAPSHOT`

### Gradle:
Groovy:
```groovy
repositories {
    maven {
        url = "https://artifactory.scala.gg/artifactory/opensource"
    }
}

dependencies {
    implementation("io.liftgate.ftc.scripting:[module]:[version]")
}
```
Kotlin:
```kotlin
repositories {
    maven(url = "https://artifactory.scala.gg/artifactory/opensource")
}

dependencies {
    implementation("io.liftgate.ftc.scripting:[module]:[version]")
}
```

**You must also add the following entries to your android.packagingOptions closure in your TeamCode gradle script!**
```groovy
android {
    packagingOptions {
        ...
        pickFirst "assets/obtstatic/**"
    }
}
```

Once you have configured the maven/gradle dependency for on-bot-typescript, follow the instructions below to start writing scripts!

1. Create your OpMode(s):
```java
/*
 * Since this class implements the DevLinearOpMode class, the code editor web 
 * server will be started alongside the configured TeleOp, if not already started.
 * 
 * Additionally, implementing "DevLinearOpMode" enables hot-reload 
 * functionality for scripts (when a file is saved from the web editor, changes are
 * automatically applied to the robot's running OpMode instance).
 * 
 * The implemented methods in this class are in the {@link TSScript} class, so
 *  to avoid boilerplate, you can proxy methods from a shared TSScript 
 * implementation.
 */
@TeleOp
public class DevScriptedTeleOp extends DevLinearOpMode {
    @Override
    public @NotNull String getScriptName() {
        /*
         * Script name can contain directories, delimited by the "/" 
         * character. Script names must also end with the .ts extension.
         */
        return "dev/TeleOp.ts";
    }

    @Override
    public @NotNull List<ImpliedVariable> getImpliedVariables() {
        /*
         * Implied variables allow you to use variables in your script without 
         * having to define them directly. Implied variables can be either methods
         * or variables.
         * 
         * on-bot-typescript offers default implied variables, which can be found in the 
         * {@link ProdLinearOpMode} class on GitHub.
         * 
         * Example script using the following implied variables:
         * <pre>
         * while (!isStopRequested)
         * {
         *      // this will compile even though we didn't explicitly define
         *      leftMotor.setPower(1000)
         * }
         * </pre>
         */
        return Arrays.asList(
                ImpliedVariable.of(
                        "leftMotor",
                        hardwareMap.get(DcMotor.class, "leftMotor")
                ),
                ImpliedVariable.of(
                        "myServo",
                        hardwareMap.get(Servo.class, "myServo")
                )
        );
    }
}
```
 
```java
/*
 * Functions the same as the example above, but with a few differences:
 * 1. No web editor is explicitly started.
 *  - The web editor will stay online if it has already been initialized.
 * 2. Hot-reload is disabled
 */
@TeleOp
public class ProdScriptedTeleOp extends ProdLinearOpMode {
    @Override
    public @NotNull String getScriptName() {
        /*
         * We have changed the script directory from 
         * dev -> prod for consistency. 
         */
        return "prod/TeleOp.ts";
    }

    @Override
    public @NotNull List<ImpliedVariable> getImpliedVariables() {
        return Arrays.asList(
                ImpliedVariable.of(
                        "leftMotor",
                        hardwareMap.get(DcMotor.class, "leftMotor")
                ),
                ImpliedVariable.of(
                        "myServo",
                        hardwareMap.get(Servo.class, "myServo")
                )
        );
    }
}
```

To explicitly start/stop the web editor, you must create your own implementations of `StartEditorOpMode` and `StopEditorOpMode`. The behavior of these OpModes are self explanatory.
> [!IMPORTANT]
> It is good practice to stop your web editor (or not start it in the first place) before competitions or actual trial runs. 

```java
@TeleOp(
        name = "Start",
        group = "WebEditor"
)
public class StartWebEditor extends StartEditorOpMode {
    
}
```
```java
@TeleOp(
        name = "Stop",
        group = "WebEditor"
)
public class StartWebEditor extends StopEditorOpMode {
    
}
```

## Script Layout
Scripts should be laid out in the following structure:
```
dev |
    | DevTeleOp.ts
    | DevAutonomous.ts

prod |
     | ProdTeleOp.ts
     | ProdAutonomous.ts

Shared.ts
```

### Shared Script
The shared script is applied to every other script in the database. For example:
```javascript
// Shared script:
function add(a: number, b: number): number {
    return a + b;
}

// DevTeleOp.ts
print(add(1, 2)) // You're able to use the add function as DevTeleOp inherits the Shared script.
```

The Shared script must be in the root directory on your web editor, and must be named `Shared.ts`.
