import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


/**
 * Alert can be used to display an alert message.
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 2.0 -- 08-08-2015
 */
public class Alert extends Message
{
   static private Color alertColor = new Color (246,141,130);

    public Alert( String message_text ) {
        super ( message_text, alertColor, "sad.png" );
    }
        
}
