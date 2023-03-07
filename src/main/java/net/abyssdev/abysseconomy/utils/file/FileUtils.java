package net.abyssdev.abysseconomy.utils.file;

import lombok.SneakyThrows;
import net.abyssdev.abysseconomy.AbyssEconomy;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * File utilities for copying data
 * @author Relocation
 */
public final class FileUtils {

    @SneakyThrows
    public static void copy(final InputStream input, final File target) {
        if (target.exists()) {
            throw new IOException("File already exists!");
        }

        final File parentDir = target.getParentFile();

        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed at creating directories!");
            }
        }

        if (!parentDir.isDirectory()) {
            throw new IOException("The parent of this file is no directory!?");
        }

        if (!target.createNewFile()) {
            throw new IOException("Failed at creating new empty file!");
        }

        if (input == null) {
            throw new NullPointerException("Input is null!");
        }

        final byte[] buffer = new byte[1024];
        final OutputStream output = new FileOutputStream(target);

        int realLength;

        while ((realLength = input.read(buffer)) > 0) {
            output.write(buffer, 0, realLength);
        }

        output.flush();
        output.close();
    }

    @SneakyThrows
    public static InputStream getInputFromJar(final String path) {
        if (path == null) {
            throw new IllegalArgumentException("The path can not be null");
        }

        final URL url = AbyssEconomy.class.getClassLoader().getResource(path);

        if (url == null) {
            return null;
        }

        final URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }
}
