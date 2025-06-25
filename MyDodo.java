import greenfoot.*;
import java.util.*;

public class MyDodo extends Dodo {
    private int myNrOfEggsHatched = 0;
    private int score = 0;
    private int stepsLeft = Mauritius.MAXSTEPS;

    private List<Egg> eggs;
    private List<SurpriseEgg> surpriseEggs;
    private Nest nest;
    private boolean done;
    private boolean surpriseEggsGenerated = false;
    public boolean isAtNest() {
        return this.getX() == nest.getX() && this.getY() == nest.getY();
    }

    private boolean isAtLocation(int x, int y) {
        return Math.abs(getX() - x) < 5 && Math.abs(getY() - y) < 5;
    }


    

    public MyDodo() {
        super(EAST);
        surpriseEggs = new ArrayList<>();
    }

    private int dist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private Egg nearestEgg() {
        Egg best = null;
        int bestD = Integer.MAX_VALUE;
        for (Egg e : eggs) {
            int d = dist(getX(), getY(), e.getX(), e.getY());
            if (d < bestD) {
                bestD = d;
                best = e;
            }
        }
        return best;
    }
    
private SurpriseEgg nearestSurpriseEgg() {
    if (surpriseEggs == null || surpriseEggs.isEmpty()) {
        return null;
    }

    SurpriseEgg best = null;
    int bestD = Integer.MAX_VALUE;
    for (SurpriseEgg e : surpriseEggs) {
        int d = dist(getX(), getY(), e.getX(), e.getY());
        if (d < bestD) {
            bestD = d;
            best = e;
        }
    }
    return best;
}


public Egg pickUpEgg() {
    Egg maybeEgg = getEgg();
    if (maybeEgg == null) {
        showError("There is no egg in this cell");
        Greenfoot.stop();
    } else {
        getWorld().removeObject(maybeEgg);
        if (maybeEgg instanceof SurpriseEgg) {
            score += ((SurpriseEgg) maybeEgg).getValue();
        } else {
            score += 1;
        }
    }
    return maybeEgg;
}





public void act() {
    this.nest = (Nest)getWorld().getObjects(Nest.class).get(0);
    if (done)
        return;

    if (eggs == null) {
        eggs = getWorld().getObjects(Egg.class);
        List<Nest> n = getWorld().getObjects(Nest.class);
        if (!n.isEmpty())
            nest = n.get(0);

        if (!surpriseEggsGenerated) {
            generateSurpriseEggs();
            surpriseEggsGenerated = true;
        }
    }

    // ? Eerst checken of alles op is
    if (eggs.isEmpty() && (surpriseEggs == null || surpriseEggs.isEmpty())) {
        if (isAtNest()) {
            getWorld().addObject(new Compliment("Gefeliciteerd alles gevonden!"), getWorld().getWidth()/2, getWorld().getHeight()/2);
            done = true;
        } else {
            goToLocation(nest.getX(), nest.getY());
        }
        return;
    }

    // Daarna pas doorgaan met zoeken
    Egg target = nearestEgg();
    SurpriseEgg sTarget = nearestSurpriseEgg();

    Egg nearest = null;
    if (target != null && sTarget != null) {
        nearest = (dist(getX(), getY(), target.getX(), target.getY()) <= dist(getX(), getY(), sTarget.getX(), sTarget.getY())) ? target : sTarget;
    } else if (target != null) {
        nearest = target;
    } else if (sTarget != null) {
        nearest = sTarget;
    }

    if (nearest != null) {
        goToLocation(nearest.getX(), nearest.getY());
        pickUpEgg();
        if (nearest instanceof SurpriseEgg) {
            surpriseEggs.remove(nearest);
        } else {
            eggs.remove(nearest);
        }
    }




    stepsLeft--;
    updateScoreboard();
}



    public void move() {
        if (canMove()) {
            step();
        } else {
            showError("I'm stuck!");
        }
    }

    public boolean canMove() {
        return !(borderAhead() || fenceAhead());
    }

    public void hatchEgg() {
        if (onEgg()) {
            pickUpEgg();
            myNrOfEggsHatched++;
            score += 5;
        } else {
            showError("There was no egg in this cell");
        }
    }

    public int getNrOfEggsHatched() {
        return myNrOfEggsHatched;
    }

    public void jump(int distance) {
        while (distance-- > 0)
            move();
    }

    public void jump() {
        move();
        move();
        System.out.println("Jumped to: x = " + getX() + ", y = " + getY());
    }

    public void walkToWorldEdgePrintingCoordinates() {
        while (!borderAhead()) {
            System.out.println("x = " + getX() + ", y = " + getY());
            move();
        }
    }

    public boolean canLayEgg() {
        return getOneObjectAtOffset(0, 0, Egg.class) == null;
    }

    public void turn180() {
        turn(180);
    }

    public boolean fenceAhead() {
        return getOneObjectAtOffset(0, 1, Fence.class) != null;
    }

    public void climbOverFence() {
        turn(-90);
        move();
        turn(90);
        move();
        move();
        turn(90);
        move();
        turn(-90);
    }

    public void stepOneCellBackwards() {
        turn180();
        move();
        turn180();
    }

    public boolean grainAhead() {
        move();
        boolean found = getOneObjectAtOffset(0, 0, Grain.class) != null;
        stepOneCellBackwards();
        return found;
    }

    public void gotoEgg() {
        while (!onEgg())
            move();
    }

    public void walkToWorldEdge() {
        while (!borderAhead())
            move();
    }

    public void goBackToStartOfRowAndFaceBack() {
        turn180();
        walkToWorldEdge();
        turn180();
    }

    public void walkToWorldEdgeClimbingOverFences() {
        while (!borderAhead()) {
            if (fenceAhead())
                climbOverFence();
            else
                move();
        }
    }

    public void pickUpGrainsAndPrintCoordinates() {
        while (!borderAhead()) {
            Actor grain = getOneObjectAtOffset(0, 0, Grain.class);
            if (grain != null) {
                System.out.println("Grain at x=" + getX() + ", y=" + getY());
                getWorld().removeObject(grain);
            }
            move();
        }
    }

    public void layEggsInEmptyNests() {
        while (!borderAhead()) {
            if (getOneObjectAtOffset(0, 0, Nest.class) != null &&
                    getOneObjectAtOffset(0, 0, Egg.class) == null &&
                    canLayEgg()) {
                layEgg();
            }
            move();
        }
    }

    public void walkAroundFencedArea() {
        while (!onEgg()) {
            if (fenceAhead())
                turn(90);
            else
                move();
        }
    }

    public void faceEast() {
        while (getDirection() != EAST)
            turn(90);
    }

    public boolean locationReached(int x, int y) {
        return getX() == x && getY() == y;
    }

    public void goToLocation(int x, int y) {
        if (!validCoordinates(x, y))
            return;

        while (!locationReached(x, y)) {
            if (getX() < x)
                setDirection(EAST);
            else if (getX() > x)
                setDirection(WEST);
            else if (getY() < y)
                setDirection(SOUTH);
            else if (getY() > y)
                setDirection(NORTH);
            move();
        }
    }

    public boolean validCoordinates(int x, int y) {
        boolean valid = x >= 0 && x < getWorld().getWidth() && y >= 0 && y < getWorld().getHeight();
        if (!valid)
            showError("Invalid coordinates");
        return valid;
    }

    public int countEggsInRow() {
        int count = 0;
        while (!borderAhead()) {
            if (onEgg())
                count++;
            move();
        }
        if (onEgg())
            count++;
        goBackToStartOfRowAndFaceBack();
        return count;
    }

    public void eggTrailToNest() {
        int eggs = countEggsInRow();
        showCompliment("Er liggen " + eggs + " eieren in de rij!");
        gotoEgg();
    }

    public void layTrailOfEggs(int n) {
        if (n < 1) {
            showError("Aantal moet positief zijn");
            return;
        }
        for (int i = 0; i < n; i++) {
            if (canLayEgg())
                layEgg();
            if (!borderAhead())
                move();
        }
    }

    public int countAllEggs() {
        int total = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            total += countEggsInRow();
        }
        return total;
    }

    public int findRowWithMostEggs() {
        int max = 0, row = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            int current = countEggsInRow();
            if (current > max) {
                max = current;
                row = y;
            }
        }
        System.out.println("Rij met meeste eieren: " + row);
        return row;
    }

    public void eggPyramid() {
        int hoogte = 1;
        int startX = getX(), startY = getY();

        while (startX + hoogte <= getWorld().getWidth() && startY + hoogte <= getWorld().getHeight()) {
            for (int i = 0; i < hoogte; i++) {
                goToLocation(startX + i, startY + hoogte - 1);
                if (canLayEgg())
                    layEgg();
            }
            hoogte++;
        }
    }

    public void eggBlock() {
        int startX = getX(), startY = getY();
        for (int y = startY; y < getWorld().getHeight(); y++) {
            for (int x = startX; x < getWorld().getWidth(); x++) {
                goToLocation(x, y);
                if (canLayEgg())
                    layEgg();
            }
        }
    }

    public void strongEggBlock() {
        int startX = getX(), startY = getY();
        for (int y = startY; y < getWorld().getHeight(); y += 2) {
            for (int x = startX; x < getWorld().getWidth(); x += 2) {
                goToLocation(x, y);
                if (canLayEgg())
                    layEgg();
            }
        }
    }

    public void averageEggsPerRow() {
        int total = 0, rows = getWorld().getHeight();
        for (int y = 0; y < rows; y++) {
            goToLocation(0, y);
            total += countEggsInRow();
        }
        double avg = (double) total / rows;
        System.out.println("Gemiddeld aantal eieren per rij: " + avg);
    }

    public boolean isEven(int n) {
        return n % 2 == 0;
    }

    public int countEggsInColumn(int x) {
        int count = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            count += getWorld().getObjectsAt(x, y, Egg.class).size();
        }
        return count;
    }

    public int getIncorrectRowNr() {
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            if (!isEven(countEggsInRow()))
                return y;
        }
        return -1;
    }

    public int getIncorrectColumnNr() {
        for (int x = 0; x < getWorld().getWidth(); x++) {
            if (!isEven(countEggsInColumn(x)))
                return x;
        }
        return -1;
    }

    public void gotoIncorrectBit() {
        int row = getIncorrectRowNr();
        int col = getIncorrectColumnNr();
        if (row != -1 && col != -1)
            goToLocation(col, row);
        else
            showCompliment("Geen fout gevonden");
    }

    public void fixIncorrectBit() {
        int row = getIncorrectRowNr();
        int col = getIncorrectColumnNr();

        if (row == -1 || col == -1) {
            showCompliment("Geen fout om te herstellen");
            return;
        }

        goToLocation(col, row);
        Egg egg = (Egg) getOneObjectAtOffset(0, 0, Egg.class);
        if (egg != null) {
            getWorld().removeObject(egg);
            showCompliment("Extra ei verwijderd!");
        } else {
            layEgg();
            showCompliment("Ontbrekend ei toegevoegd!");
        }
    }

    public void fixParityWorld() {
        if (getIncorrectRowNr() == -1 && getIncorrectColumnNr() == -1) {
            showCompliment("De wereld is al goed!");
        } else {
            fixIncorrectBit();
        }
    }

    public void generateSurpriseEggs() {
        surpriseEggs = SurpriseEgg.generateListOfSurpriseEggs(10, getWorld());
    }

    public void printCoordinatesOfEgg(Egg egg) {
        System.out.println("x = " + egg.getX() + ", y = " + egg.getY());
    }

    public void printAllSurpriseEggCoordinates() {
        for (SurpriseEgg egg : surpriseEggs) {
            printCoordinatesOfEgg(egg);
        }
    }

    public void printEggValues() {
        for (SurpriseEgg egg : surpriseEggs) {
            System.out.println("Waarde: " + egg.getValue() + " op (" + egg.getX() + "," + egg.getY() + ")");
        }
    }

    public void findMostValuableEgg() {
        if (surpriseEggs.isEmpty()) {
            System.out.println("Geen surprise eieren gevonden.");
            return;
        }

        SurpriseEgg maxEgg = surpriseEggs.get(0);
        for (SurpriseEgg egg : surpriseEggs) {
            if (egg.getValue() > maxEgg.getValue()) {
                maxEgg = egg;
            }
        }

        printEggValues();
        System.out.println(
                "Meest waardevolle ei: " + maxEgg.getValue() + " op (" + maxEgg.getX() + "," + maxEgg.getY() + ")");
    }

    public void calculateAverageEggValue() {
        if (surpriseEggs.isEmpty()) {
            System.out.println("Geen eieren om te berekenen.");
            return;
        }

        int totaal = 0;
        for (SurpriseEgg egg : surpriseEggs) {
            totaal += egg.getValue();
        }

        double gemiddelde = (double) totaal / surpriseEggs.size();
        System.out.println("Gemiddelde waarde van surprise eieren: " + gemiddelde);
    }

    public void randomMove() {
        int action = Greenfoot.getRandomNumber(4);
        switch (action) {
            case 0:
                move();
                break;
            case 1:
                turn(90);
                break;
            case 2:
                turn(-90);
                break;
            case 3:
                if (onEgg()) {
                    hatchEgg();
                }
                break;
        }
    }

    public void updateScoreboard() {
        ((Mauritius) getWorld()).updateScore(stepsLeft, score);
    }
}
