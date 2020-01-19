package gitVersion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;

public class gitVer {

    /**
     * Funkcja, ktora zwraca nuer comitu, musisz tego stringa wrzucic na graf, powodzonka <3
     *
     * @return {@String} last comit number
     */

    public String getGitVersion() {
        String[] command = {"git", "log"};
        String name = " ";
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            try {
                name = reader.readLine();
                if (name == null) {
                    throw new InvalidObjectException("Name equals null");
                }
                return "" + name.substring(7, 47);

            } catch (InvalidObjectException e) {
                e.printStackTrace();
                return "NIE PYKLO";
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "NIE PYKLO";
    }
}



