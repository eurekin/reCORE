package core.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rekin
 */
public class InputStreamProcessor {
    InputStream in;
    LineProcessor processor;

    public InputStreamProcessor(InputStream in, LineProcessor processor) {
        this.in = in;
        this.processor = processor;
    }
    
    public void process() {
        try {
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            int no = 0;
            while ((line = br.readLine()) != null) {
                processor.process(line, no);
                no++;
            }
        } catch (IOException ex) {
            Logger.getLogger(InputStreamProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
