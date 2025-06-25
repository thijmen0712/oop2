import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


/**
 * Compliment can be used to congratulate sb.
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 2.0 -- 08-08-2015
 */
public class Compliment extends Message
{
   static private Color happyColor = new Color ( 77, 215, 77 );

    public Compliment( String message_text ) {
        super ( message_text, happyColor, "winking.png" );
    }
        
}
