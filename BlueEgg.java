import greenfoot.*;

/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public class BlueEgg extends Egg
{
    public BlueEgg () {
        super( 1 );
    }

    private boolean hasABlueEgg() {
        return getOneObjectAtOffset(0, 0, BlueEgg.class) != null;
    }
    
    
    public void setLocation( int x, int y ){
        Nest mayBeNest = (Nest) getOneObjectAtOffset(0, 0, Nest.class);
        if ( mayBeNest != null ) {
            mayBeNest.removeEgg();
        }
        super.setLocation( x, y );
        mayBeNest = (Nest) getOneObjectAtOffset(0, 0, Nest.class);
        if ( mayBeNest != null ) {
            mayBeNest.addEgg();
        }        
    }
}
