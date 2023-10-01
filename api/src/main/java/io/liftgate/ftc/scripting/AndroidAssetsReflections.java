package io.liftgate.ftc.scripting;

import kotlin.Pair;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 10/1/2023
 */
public enum AndroidAssetsReflections {
    ;

    public static final Class<?> APPLICATION_CLASS;
    public static final Class<?> ASSET_MANAGER_CLASS;

    public static final Method GET_DEF_CONTEXT_METHOD;
    public static final Method APPLICATION_GET_ASSET_MANAGER_METHOD;

    public static final Method ASSET_MANAGER_LIST;
    public static final Method ASSET_MANAGER_OPEN;

    public static final Object DEF_CONTEXT;
    public static final Object ASSET_MANAGER;

    @Nullable
    public static InputStream open(@NotNull final String path) {
        try {
            return (InputStream) ASSET_MANAGER_OPEN.invoke(ASSET_MANAGER, path);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static String[] list(@NotNull final String path) {
        try {
            return (String[]) ASSET_MANAGER_LIST.invoke(ASSET_MANAGER, path);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static List<Pair<String, InputStream>> openAll(@NotNull final String path) {
        try {
            final String[] files = (String[]) ASSET_MANAGER_LIST
                    .invoke(ASSET_MANAGER, path);
            final List<Pair<String, InputStream>> streams = new ArrayList<>(files.length);

            for (final String file : files) {
                streams.add(new Pair<>(file, open(file)));
            }

            return streams;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            APPLICATION_CLASS = Class.forName("android.app.Application");
            ASSET_MANAGER_CLASS = Class.forName("android.content.res.AssetManager");

            GET_DEF_CONTEXT_METHOD = AppUtil.class.getMethod("getDefContext");
            APPLICATION_GET_ASSET_MANAGER_METHOD = APPLICATION_CLASS.getMethod("getAssets");

            DEF_CONTEXT = GET_DEF_CONTEXT_METHOD.invoke(AppUtil.getInstance());
            ASSET_MANAGER = APPLICATION_GET_ASSET_MANAGER_METHOD.invoke(DEF_CONTEXT);

            ASSET_MANAGER_LIST = ASSET_MANAGER_CLASS.getMethod("list", String.class);
            ASSET_MANAGER_OPEN = ASSET_MANAGER_CLASS.getMethod("open", String.class);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
