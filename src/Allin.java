import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexey on 28.06.2017.
 */
public class Allin {
    public static void main(String[] args) throws Throwable {
        java.util.Properties properties = System.getProperties();
        String s = properties.getProperty("user.name");
        Date time = new Date();
        SimpleDateFormat formatFortime = new SimpleDateFormat("HH-mm");
        String Filename = s + "_sessionID_time(" + formatFortime.format(time) + ").xml";
        File folder = new File(".\\");
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries) {
            if (entry.isFile()) {
                if (entry.getName().equals(Filename)) {
                    System.out.println("Такой файл уже существует в папке! Он будет пересохранен в .bak");
                    String newname = entry.getName().replace(".xml", ".bak");
                    File newfilename = new File(newname);
                    entry.renameTo(newfilename);
                }
            }
        }

        try (FileWriter writer = new FileWriter(Filename)) {
            try {
                String line;
                Process p = Runtime.getRuntime().exec
                        (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                writer.write("<main username = \"" + s + "\" sessionID = \"2\">\n");
                Pattern pat = Pattern.compile(" (\\d*) Serv");
                Pattern pat2 = Pattern.compile("(\\w*.exe) *\\d");
                while ((line = input.readLine()) != null) {
                    Matcher m2 = pat2.matcher(line);
                    Matcher m = pat.matcher(line);
                    if (m.find() && m2.find())
                        writer.write("<process>\n<name>" + m2.group(1) + "</name>\n" + "<processID>" + m.group(1) + "</ProcessID>\n</process>\n");
                }
                writer.write("</main>");
                input.close();
            } catch (Exception err) {
                err.printStackTrace();
            }
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
