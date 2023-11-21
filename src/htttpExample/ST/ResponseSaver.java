package htttpExample.ST;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResponseSaver {
    public static void saveResponse(String data, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(data);
        writer.flush();
    }
}
