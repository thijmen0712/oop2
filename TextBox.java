import greenfoot.*;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * TextBox is used to display messages in a box.
 * 
 * @author Sjaak Smetsers
 * @version 1.0, 31-03-2015
 */
public class TextBox extends Actor
{
    private GreenfootImage myImage;
    static private Color textColor = Color.BLACK;

    public TextBox( int width, int height, int border_width, Font font, Color box_color ) {
        myImage = new GreenfootImage( width, height );
        drawBox( 0, 0, width, height, border_width, box_color);
        myImage.setFont( font );
        setImage (myImage);
    }

    private void drawBox( int x, int y, int w, int h, int bw, Color color ) {
        myImage.setColor ( color.darker() );
        myImage.fillRect (x, y, w, h);
        myImage.setColor ( color );
        myImage.fillRect (x+bw, y+bw, w-2*bw, h-2*bw);
    }

    public void drawText( String text, int x, int y, int maxw ) {
        FontMetrics fm = myImage.getAwtImage().getGraphics().getFontMetrics( myImage.getFont() );
        int textWidth = fm.stringWidth( text );
        myImage.setColor ( textColor );
        myImage.drawString( text, x + (maxw - textWidth) / 2, y );
    }
    
}
