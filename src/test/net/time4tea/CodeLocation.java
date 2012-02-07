package net.time4tea;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;

public class CodeLocation {

    public static File sourceFileFor(Class<?> aClass) {
        CodeSource codeSource = aClass.getProtectionDomain().getCodeSource();
        URL location = codeSource.getLocation();
        return new File(location.getFile(), aClass.getName().replaceAll("\\.", "/") + ".class");
    }
}
