package net.time4tea.asm.transform.staticaccesschanger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class TestA {
    
    
    public void printer() {
        System.out.print("hello");
    }

    
    public void printerExpectedResult() {
        Bob.stream.print("hello");
    }
    
    public static class Bob extends PrintStream {

        public static PrintStream stream;
        
        public Bob(File file) throws FileNotFoundException {
            super(file);
        }
    }
}
