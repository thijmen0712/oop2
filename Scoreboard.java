import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 2.0 -- 08-08-2015
 */

public class Scoreboard  extends Actor
{
    private static final int SB_WIDTH = 120, SB_WIDTH2 = 250,
                             SB_HEIGHT = 30, BORDER_WIDTH = 3;
    GreenfootImage boardImage;
    
    private String myLabel, myLabel2 = null;
    
    private final Color boardColor    = new Color ( 117, 185, 231 ),
                        textColor     = Color.BLACK;
    private final Font textFont       = new Font("Calibri", true, false, 18);

    public Scoreboard ( String label, int init_score ) {
        boardImage = new GreenfootImage ( SB_WIDTH , SB_HEIGHT );
        myLabel = label;        
        drawScore( SB_WIDTH, init_score );
        setImage ( boardImage ); 
        
    }

    public Scoreboard ( String label1, int init_score1, String label2, int init_score2 ) {
        boardImage = new GreenfootImage ( SB_WIDTH2 , SB_HEIGHT );
        myLabel  = label1;        
        myLabel2 = label2;        
        drawScore( init_score1, init_score2 );
        setImage ( boardImage ); 
        
    }

    public void updateScore( int ... new_scores ) {
        drawScore( new_scores );
    }

    private void drawScore( int ... scores ) {
        int sb_width;
        String sb_text;
        if ( scores.length == 1 ) {
            sb_width = SB_WIDTH;
            sb_text  =  String.format("%s %d", myLabel, scores[0]);
        } else {
            sb_width = SB_WIDTH2;
            sb_text  =  String.format("%s %d  %s %d", myLabel, scores[0], myLabel2, scores[1]);
        }
        boardImage.setColor ( boardColor.darker() );
        boardImage.fillRect( 0, 0, sb_width, SB_HEIGHT );
        boardImage.setColor ( boardColor );
        boardImage.fillRect( BORDER_WIDTH, BORDER_WIDTH, sb_width-2*BORDER_WIDTH,SB_HEIGHT-2*BORDER_WIDTH );
        boardImage.setColor ( textColor );
        boardImage.setFont( textFont );
        boardImage.drawString ( sb_text, BORDER_WIDTH+2, SB_HEIGHT - 10 );
    }
     
}

