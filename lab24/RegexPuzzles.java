import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPuzzles {
    public static List<String> urlRegex(String[] urls) {
        /* Your code here */
        List<String> result = new ArrayList<>();
        Pattern inputForm = Pattern.compile("\\(\\w*https?://(\\w+\\.)+[a-z]{2,3}/\\w+\\.html\\w*\\)");
        for (String url: urls) {
            Matcher inputMatcher = inputForm.matcher(url);
            if (inputMatcher.matches()) {
                    result.add(inputMatcher.group());
            }
        }
        return result;
    }

    public static List<String> findStartupName(String[] names) {
        /* Your code here */
        List<String> result = new ArrayList<>();
        Pattern nameFrom = Pattern.compile("(Data|App|my|on|un)[^i&&\\w]+(ly|sy|ify|\\.io|\\.fm|\\.tv)");
        for (String name: names) {
            Matcher nameMatcher = nameFrom.matcher(name);
            if (nameMatcher.matches()) {
                result.add(nameMatcher.group());
            }
        }
        return result;
    }

    public static BufferedImage imageRegex(String filename, int width, int height) {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file found: " + filename);
        }

        // Possible initialization code
        Pattern rgbForm = Pattern.compile("\\[(\\d{1,3}), (\\d{1,3}), (\\d{1,3})\\]");
        Pattern coordForm = Pattern.compile("\\((\\d+), (\\d+)\\)");
        int[][][] arr = new int[width][height][3];
        try {
            String line;
            while ((line = br.readLine()) != null) {
                // Code for processing each line
                Matcher rgbMatcher = rgbForm.matcher(line);
                Matcher coordMatcher = coordForm.matcher(line);
                if (rgbMatcher.find() && coordMatcher.find()) {
                    int x = Integer.parseInt(coordMatcher.group(1));
                    int y = Integer.parseInt(coordMatcher.group(2));
                    int r = Integer.parseInt(rgbMatcher.group(1));
                    int g = Integer.parseInt(rgbMatcher.group(2));
                    int b = Integer.parseInt(rgbMatcher.group(3));
                    arr[x][y][0] = r;
                    arr[x][y][1] = g;
                    arr[x][y][2] = b;
                }
            }
        } catch (IOException e) {
            System.err.printf("Input error: %s%n", e.getMessage());
            System.exit(1);
        }

        return arrayToBufferedImage(arr);
    }

    public static BufferedImage arrayToBufferedImage(int[][][] arr) {
        BufferedImage img = new BufferedImage(arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                int pixel = 0;
                for (int k = 0; k < 3; k++) {
                    pixel += arr[i][j][k] << (16 - 8*k);
                }
                img.setRGB(i, j, pixel);
            }
        }

        return img;
    }

    public static void main(String[] args) {
        /* For testing image regex */
        BufferedImage img = imageRegex("mystery.txt", 400, 400);

        File outputfile = new File("output_img.jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}

