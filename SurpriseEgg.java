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

        for (int i = 0; i < amount; i++) {
            int x = rand.nextInt(world.getWidth());
            int y = rand.nextInt(world.getHeight());
            int value = 1 + rand.nextInt(10); // waarde tussen 1 en 10

            SurpriseEgg egg = new SurpriseEgg(x, y, value);
            world.addObject(egg, x, y);
            list.add(egg);
        }

        return list;
    }
}
