# Configure
Once you have configured the maven/gradle dependency for FTCScript, follow the instructions below to start writing scripts!

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
 * The implemented methods in this class are in the {@link KotlinScript} class, so
 *  to avoid boilerplate, you can proxy methods from a shared KotlinScript 
 * implementation.
 */
@TeleOp
public class DevScriptedTeleOp extends DevLinearOpMode {
    @Override
    public @NotNull String getScriptName() {
        /*
         * Script name can contain directories, delimited by the "/" 
         * character. Script names must also end with the .kts extension.
         */
        return "dev/TeleOp.kts";
    }

    @Override
    public @NotNull List<ImpliedVariable> getImpliedVariables() {
        /*
         * Implied variables allow you to use variables in your script without 
         * having to define them directly. Implied variables can be either methods
         * or variables.
         * 
         * FTCScript offers default implied variables, which can be found in the 
         * {@link ProdLinearOpMode} class on GitHub.
         * 
         * Example script using the following implied variables:
         * <pre>
         * while (!isStopRequested)
         * {
         *      // this will compile even though we didn't explicitly define
         *      leftMotor.power = 1000
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
        return "prod/TeleOp.kts";
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
