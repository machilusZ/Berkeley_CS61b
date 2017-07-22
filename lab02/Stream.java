import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Stream implements Runnable {

    private final int BUFFER_SIZE = 128000;

    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    public Stream(String filename) {
        this(new File(filename));
    }

    public Stream(File file) {
        try {
            audioStream = AudioSystem.getAudioInputStream(file);
            audioFormat = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void playSound(){
        sourceLine.start();
        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }
        sourceLine.drain();
        sourceLine.close();
    }

    @Override
    public void run() {
        playSound();
    }
}
