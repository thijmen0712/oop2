import greenfoot.*;
import java.util.*;

public class SurpriseEgg extends Egg
{
    private int value;

    public SurpriseEgg(int x, int y, int value) {
        super(value); // stuur de waarde door naar Egg
        this.value = value;
        // afbeelding en plaatsing worden apart gedaan
    }

    public int getValue() {
        return value;
    }

   public static List<SurpriseEgg> generateListOfSurpriseEggs(int amount, World world) {
    List<SurpriseEgg> list = new ArrayList<>();
    Random rand = new Random();

    int tries = 0;
    while (list.size() < amount && tries < amount * 10) {
        int x = rand.nextInt(world.getWidth());
        int y = rand.nextInt(world.getHeight());
        if (world.getObjectsAt(x, y, null).isEmpty()) {
            int value = 1 + rand.nextInt(10);
            SurpriseEgg egg = new SurpriseEgg(x, y, value);
            world.addObject(egg, x, y);
            list.add(egg);
        }
        tries++;
    }

    return list;
}
}

