package de.fh.kiel.advancedjava.pojoapplication.attackszenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EvilClass {
    static {
        String str = "I did something evil";
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("evilresult.txt"));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
