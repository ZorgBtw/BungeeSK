package fr.zorg.bungeesk.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtils {

    public static List<Class<?>> getClassesFromPackage(String packageName, Class<?> subTypeOf, File pluginFile) throws IOException {
        final String path = packageName.replace(".", "/");
        final List<Class<?>> classes = new ArrayList<>();
        final JarFile jarFile = new JarFile(pluginFile);
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            if (entry.getName().startsWith(path) && entry.getName().endsWith(".class")) {
                final String className = entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6);
                try {
                    final Class<?> clazz = Class.forName(className);
                    if (clazz.getSuperclass().equals(subTypeOf)) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        return classes;
    }

}