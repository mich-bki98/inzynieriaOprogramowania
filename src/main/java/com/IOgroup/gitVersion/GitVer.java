package com.IOgroup.gitVersion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;

public class GitVer {

    /**
     * Funkcja, ktora zwraca nuer comitu, musisz tego stringa wrzucic na graf, powodzonka <3
     *
     * @return {@String} last comit number
     */

    public static String getGitVersion() {      //dodałem static (Grzegorz Bawęda), aby się dało z tego korzystać z każdego miejsca, oraz zmienilem nazwe klasy żeby byla duza litera, tak jak pan jezus nakazał
        String[] command = {"git", "log"};
        String name;
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            try {
                name = reader.readLine();
                if (name == null) {
                    throw new InvalidObjectException("Name equals null");
                }
                return "" + name.substring(7);

            } catch (InvalidObjectException e) {
                e.printStackTrace();
                return "unknown";
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "unknown";
    }
}



