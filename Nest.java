import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Nest here.
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public class Nest extends Actor
{
    private GreenfootImage emptyNest, filledNest;
    private boolean myNestIsEmpty = true;

    public Nest() {
        emptyNest   = getImage ();
        filledNest  = new GreenfootImage("egg-in-nest.png");
    }

    private boolean hasABlueEgg() {
        return getOneObjectAtOffset(0, 0, BlueEgg.class) != null;
    }

    public void addEgg() {
        myNestIsEmpty = false;
        setImage( filledNest );
    }
    
    public void removeEgg() {
        myNestIsEmpty = true;
        setImage( emptyNest );
    }

    private void checkIfNestIsFilled () {
        if ( hasABlueEgg( ) ) {
            if ( myNestIsEmpty ){
                addEgg();
            }
        } else if ( ! myNestIsEmpty ){
            removeEgg();
        }
    }

    public void setLocation( int x, int y ){
        if ( Mauritius.checkCellContent ( this, x, y, Nest.class, Fence.class ) ){
            super.setLocation( x, y );
            checkIfNestIsFilled();
        }
    }
}
