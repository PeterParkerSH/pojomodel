package de.fh.kiel.advancedjava.pojomodel.attackszenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EvilClass {
    static {
        String str = "I did something evil";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("evilresult.txt"));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
