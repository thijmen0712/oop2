import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public class Fence extends Actor
{
    public void setLocation( int x, int y ){
        if ( Mauritius.checkCellContent ( this, x, y, Egg.class, Fence.class, Grain.class, Dodo.class, Nest.class, Fence.class ) ){
            super.setLocation( x, y );
        }
    }
}
