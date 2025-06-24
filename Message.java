import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * Message is used to show a message dialog.
 * 
  * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public abstract class Message extends Actor
{
    private static int MB_WIDTH = 300, MB_HEIGHT = 140, BORDER_WIDTH = 4;
    private static int OK_WIDTH = 50,  OK_HEIGHT = 30;
    
    private static int TXT_Y = MB_HEIGHT - 50, OK_X = (MB_WIDTH - OK_WIDTH)/2, OK_Y = MB_HEIGHT - 35;
    
    static private Color textColor     = Color.BLACK,
                         buttonColor   = Color.LIGHT_GRAY;

    public void act() {
        if ( Greenfoot.mouseClicked( this ) ) {
            getWorld().removeObject( this );
            Greenfoot.stop();
        }
    }

    public static boolean messageActive( World world ) {
        return world.getObjects( Message.class ).size() > 0;
    }
    
    public Message ( String message_text, Color box_color, String icon ) {
        GreenfootImage board_image = new GreenfootImage( MB_WIDTH, MB_HEIGHT );
        drawBox( 0, 0, MB_WIDTH, MB_HEIGHT, BORDER_WIDTH, board_image, box_color);
        drawPicture( icon, MB_WIDTH, 10, board_image);
        board_image.setFont( new Font("Arial", true, false, 16) );
        drawText( message_text, 0, TXT_Y, MB_WIDTH, board_image, textColor );
        drawBox( (MB_WIDTH - OK_WIDTH)/2, OK_Y, OK_WIDTH, OK_HEIGHT, BORDER_WIDTH, board_image, buttonColor );
        drawText( "OK", (MB_WIDTH - OK_WIDTH)/2, OK_Y+OK_HEIGHT-10, OK_WIDTH, board_image, textColor );
        setImage (board_image);
    }
    
    private void drawBox( int x, int y, int w, int h, int bw, GreenfootImage image, Color color ) {
        image.setColor ( color.darker() );
        image.fillRect (x, y, w, h);
        image.setColor ( color );
        image.fillRect (x+bw, y+bw, w-2*bw, h-2*bw);
    }
        
    private void drawText( String text, int x, int y, int maxw, GreenfootImage image, Color color ) {
        Font imageFont = image.getFont();
        int style = imageFont.isItalic()? java.awt.Font.ITALIC : (imageFont.isBold() ? java.awt.Font.BOLD : java.awt.Font.PLAIN );
        java.awt.Font awtFont = new java.awt.Font (imageFont.getName(), style, imageFont.getSize());
        FontMetrics fm = image.getAwtImage().getGraphics().getFontMetrics( awtFont );
        int textWidth = fm.stringWidth( text );
        image.setColor ( color );
        image.drawString( text, x + (maxw - textWidth) / 2, y );
    }
    
    private void drawPicture( String name, int wx, int y, GreenfootImage image ) {
        GreenfootImage picture = new GreenfootImage (name);
        image.drawImage(picture, (wx - picture.getWidth())/2 , y);
    }
    
    public static void showMessage( Message message, World world ) {
        if ( ! messageActive( world ) ) {
            world.addObject (message, world.getWidth()/2, world.getHeight()/2);
            Greenfoot.start();
        }
    }
}
