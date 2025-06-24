/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.1 -- 03-07-2017
 */

import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;


public class WorldReader  
{
    public static class Cell
    {   private int x_pos, y_pos;
        private char c_val;
        
        public Cell (int x, int y, char c) {
            x_pos = x;
            y_pos = y;
            c_val = c;
        }
        
        public int getX () {
            return x_pos; 
        }

        public int getY () {
            return y_pos;
        }

        public char getChar () {
            return c_val;
        }
    }



    private LineNumberReader worldReader;
    private int currentChar;
    private int currentPosition;
    
    private static final int EOF_CHAR = -1;

    private int worldWidth = -1, worldHeight = -1;
    
   
    /**
     * Constructor for objects of class WordReader
     */
    public WorldReader ( File worldFile ) {
        try {
            FileReader file = new FileReader( worldFile );
            worldReader = new LineNumberReader ( file );
            currentChar = worldReader.read();
            readSize  ();
            readGrid  ();
        } catch (IOException ioe) {
            System.out.println("Oops!!! Level seems not to be present.");
        }

    }

    private static boolean isNewline( int c ) {
       return c == '\n' || c == '\r';
    }
    

    private void readSize () throws IOException {
        worldWidth  = FindNumber ();
        worldHeight = FindNumber ();
        skipSpaces ();
    }

    private void readGrid () throws IOException {
        worldReader.setLineNumber( 0 );
        currentChar = worldReader.read();
        currentPosition = 0;
        findNext  ();
    }

    private void skipSpaces () throws IOException {
        while ( currentChar == ' ' ) {
            currentChar = worldReader.read();
        } 
    }
    
    private int FindNumber () throws IOException {
        skipSpaces (); 
        if ( Character.isDigit( currentChar ) ) {
            return readNumber ( );
        } else {
            return -1;
        }
    }
            
    private int readNumber ( ) throws IOException {
        int number  = Character.digit ( currentChar, 10 );
        for (currentChar = worldReader.read(); Character.isDigit(currentChar);
             currentChar = worldReader.read()) {
            number = number*10 + Character.digit ( currentChar, 10 );
        }
        return number;
    }
    
    private void findNext () throws IOException {
        boolean found = false;       
        while ( currentChar != EOF_CHAR && ! found )
            if ( isNewline( currentChar ) )  {
                currentPosition = 0;
                currentChar = worldReader.read();
            } else if ( currentChar == ' ' ) {
                currentPosition++;
                currentChar = worldReader.read();
            } else {
                found = true;
        }
    }
    
    public int getWorldWidth() {
        return worldWidth;
    }
    
    public int getWorldHeight() {
        return worldHeight;
    }
    
    public boolean hasNext () {
        return currentChar != EOF_CHAR;
    }
    
    public WorldReader.Cell next () throws IOException {
        WorldReader.Cell current_cell = new Cell (currentPosition, worldReader.getLineNumber(), (char) currentChar);
        currentChar = worldReader.read();
        currentPosition++;
        findNext ();        
        return current_cell;
    }
    
    public void close() throws IOException {
        worldReader.close();
    }

}
