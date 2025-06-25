import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;

import java.io.IOException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Mauritius.
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.1 -- 03-07-2017
 */
public class Mauritius extends World
{
    private static final String WORLD_NAME = "worldEmpty.txt";
    private static File WORLD_FILE = null;

    private static final int MAXWIDTH = 10, MAXHEIGHT = 10, CELLSIZE = 60;
    
    private Scoreboard theScoreboard = new Scoreboard ( "Moves left:", MAXSTEPS, "Score:", 0);
    
    public static final int MAXSTEPS = 40;
    

    private static boolean traceOn = true;

    private static final char
        FENCE      = '#'            ,
        EGG_YELLOW = '$'            ,
        EGG_BLUE   = '.'            ,
        NEST       = '='            ,
        GRAIN      = '+'            ,
        DODO_N     = 'N'            ,
        DODO_S     = 'S'            ,
        DODO_E     = 'E'            ,
        DODO_W     = 'W'            ;

    private static WorldReader WORLD_READER = null;
    private static int WORLD_WIDTH, WORLD_HEIGHT;
    
    static {
        if ( ! WORLD_NAME.isEmpty() ) {
            WORLD_FILE   = new File ( WorldWriter.WORLD_PATH + WORLD_NAME );           
            initWorldInfo();
        } else {
            WORLD_WIDTH  = MAXWIDTH;
            WORLD_HEIGHT = MAXHEIGHT;
        }            
    }
    
    private static void initWorldInfo() {
         WORLD_READER = new WorldReader ( WORLD_FILE );
         WORLD_WIDTH  = WORLD_READER.getWorldWidth();
         WORLD_HEIGHT = WORLD_READER.getWorldHeight();
    }
    /**
     * Constructor for objects of class ChickenWorld.
     * 
     */
public Mauritius() {    
    super(WORLD_WIDTH, WORLD_HEIGHT, CELLSIZE); 
    setPaintOrder (Message.class, Scoreboard.class, Dodo.class, Grain.class,
                   Nest.class, Egg.class, Fence.class);        
    populate();
    addObject(theScoreboard, 2, 0); // <-- voeg deze regel toe
}


    public static void traceOn() {
        traceOn = true;
    }

    public static void traceOff() {
        traceOn = false;
    }

    public static boolean traceIsOn() {
        return traceOn;
    }


    public void updateScore( int ... scores ){
        theScoreboard.updateScore( scores );
    }
    
    private Actor charToActor( char c ) {
        MyDodo newDodo;
        switch ( c ) {
            case FENCE:
                return new Fence();
            case NEST:
                return new Nest();
            case GRAIN:
                return new Grain();                
            case EGG_YELLOW:
                return new GoldenEgg();
            case EGG_BLUE:
                return new BlueEgg();
            case DODO_N: 
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.NORTH );
                return newDodo;
            case DODO_S:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.SOUTH );
                return newDodo;
            case DODO_E:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.EAST );
                return newDodo;
            case DODO_W:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.WEST );
                return newDodo;
            default:
                return null;
        }
    }

    private void populate () {
        if ( WORLD_FILE != null ) {
            if ( WORLD_READER == null ) {
                WORLD_READER = new WorldReader ( WORLD_FILE );
            }
            try {
                while (WORLD_READER.hasNext()) {
                    WorldReader.Cell next_cell = WORLD_READER.next();
                    Actor actor = charToActor( next_cell.getChar() );
                    if ( actor != null ) {
                        addObject(actor, next_cell.getX(), next_cell.getY());
                    }
                }
                WORLD_READER.close();
                WORLD_READER = null;
            } catch ( IOException ioe ) {
            }
        }            
    }
    
    private void removeAllActors() {
        removeObjects( getObjects( null ) );
    }
    
    private char getActorAt( int x, int y ){
        List<Actor> actors = getObjectsAt(x, y, null);
        if ( actors.size() > 0 ) {
            Actor actor = actors.get( 0 );
            if ( actor instanceof MyDodo ) {
                MyDodo dodo = (MyDodo) actor;
                switch ( dodo.getDirection() ) {
                    case Dodo.NORTH: return DODO_N;
                    case Dodo.SOUTH: return DODO_S;
                    case Dodo.EAST:  return DODO_E;
                    default:    return DODO_W;
                }
            } else if ( actor instanceof Fence ) {
                return FENCE;
            } else if ( actor instanceof GoldenEgg ) {
                return EGG_YELLOW;
            } else if ( actor instanceof BlueEgg ) {
                return EGG_BLUE;
            } else if ( actor instanceof Nest ) {
                return NEST;
            } else if ( actor instanceof Grain ) {
                return GRAIN;
            } else {
                return ' ';
            }
        } else {
            return ' ';
        }
    }

    public void saveToFile() {
        WorldWriter writer = new WorldWriter ( "saved.txt" );
        try {
            writer.write( String.format("%d %d\n", WORLD_WIDTH, WORLD_HEIGHT) );
            for ( int y = 0; y < WORLD_HEIGHT; y++ ) {
                for ( int x = 0; x < WORLD_WIDTH; x++ ) {
                    writer.write( getActorAt( x, y ) );
                }
                writer.write( '\n' );
            }
            writer.close();
        } catch ( IOException ioe ) {
        }
    }
    
    public void populateFromFile() {
        File world_files = new File ( WorldWriter.WORLD_PATH );
        JFileChooser chooser = new JFileChooser( world_files );
        FileNameExtensionFilter filter = new FileNameExtensionFilter( "Plain text files", "txt" );
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog( null );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            WORLD_FILE = chooser.getSelectedFile();
            initWorldInfo();
            Greenfoot.setWorld( new Mauritius () );
        }
    }

    public static boolean checkCellContent( Actor actor, int x, int y, Class... forbiddenClasses) {
        World world = actor.getWorld();
        List<Actor> allActorsInCell = world.getObjectsAt( x, y, Actor.class );
        allActorsInCell.remove( actor );        
        for ( Actor otherActor: allActorsInCell ) {
            for ( Class forbidden: forbiddenClasses ){
                if ( forbidden.isInstance( otherActor ) ) {
                    showError( world, " cell already occupied " );
                    return false;
                }
            }
        }
        return true;
    }
    
    private static void showError( World world, String err_msg ) {
        Message.showMessage(  new Alert (err_msg), world );
    }
        
}
