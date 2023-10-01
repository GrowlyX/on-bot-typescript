package io.liftgate.ftc.scripting;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
public interface TSScript {
    /**
     * Get the script file name.
     */
    @NotNull
    String getScriptName();

    /**
     * Provide implicit local variables
     * or the script [getScriptName].
     */
    @NotNull
    List<ImpliedVariable> getImpliedVariables();

    class ImpliedVariable {
        private final @NotNull String name;
        private final @NotNull Object instance;

        private ImpliedVariable(
                @NotNull final String name, @NotNull final Object instance
        ) {
            this.name = name;
            this.instance = instance;
        }

        public static @NotNull ImpliedVariable of(
                @NotNull final String name,
                @NotNull final Object instance
        ) {
            return new ImpliedVariable(name, instance);
        }

        @NotNull
        public Object getInstance() {
            return instance;
        }

        @NotNull
        public String getName() {
            return name;
        }
    }
}
