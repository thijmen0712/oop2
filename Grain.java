import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public class Grain extends Actor
{
    public void setLocation( int x, int y ){
        if ( Mauritius.checkCellContent ( this, x, y, Fence.class ) ){
            super.setLocation( x, y );
        }
    }
}
