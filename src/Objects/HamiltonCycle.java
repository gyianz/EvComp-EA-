package Objects;

import EAs.OnePlusOneEA;

import java.util.*;

/**
 * Created by Marcusljx on 9/2/2015.
 */
public class HamiltonCycle extends ArrayList<City> implements Comparable<HamiltonCycle> {

    Random RNG;
    int cityLocations_X[];
    int cityLocations_Y[];
    private double averageFitness;

    // CONSTRUCTOR WITH RANDOM CITY PLACEMENT
    public HamiltonCycle(int number_of_cities, int x_limit, int y_limit) {  // limitations describe the upper bound of the city positions
        RNG = new Random(System.currentTimeMillis());
        // INIT CYCLE(TOUR)
        Integer ii = 1;
        while (this.size() < number_of_cities) {
            City temp = null;
            do {
                temp = new City(ii, true, x_limit, y_limit);
            } while (this.containsCityPosition(temp));
            this.add(temp);  // <-- on first iteration, city number will still be 1.
            ii++;
        }
        //CHECK CONTAINER SIZE
        if (this.size() > number_of_cities) {
            this.remove(this.size() - 1);
        }
        Collections.shuffle(this);
    }

    public HamiltonCycle() {
    }

    public void printCycle() {
        for (City C : this) {
            System.out.print(C.getNUMBER() + " ");
        }
        System.out.println();
    }

    public void setTourOrder(String order) {
        String[] temp = order.split(" ");
        HamiltonCycle Next = new HamiltonCycle();
        int k = 0;
        while (Next.size() < this.size()) {
            for (int i = 0; i < this.size(); i++) {
                City C = this.get(i);
                int num = Integer.parseInt(temp[k]);
                if (C.getNUMBER() == num) {
                    Next.add(C.clone());
                    break;
                }
            }
            k++;
        }
        for (int i = 0; i < this.size(); i++) {
            this.set(i, Next.get(i));
        }
    }

    public void setTourOrder(Integer[] order) {
        HamiltonCycle Next = new HamiltonCycle();
        int k = 0;
        while (Next.size() < this.size()) {
            for (int i = 0; i < this.size(); i++) {
                City C = this.get(i);
                int num = order[k];
                if (C.getNUMBER() == num) {
                    Next.add(C.clone());
                    break;
                }
            }
            k++;
        }
        for (int i = 0; i < this.size(); i++) {
            this.set(i, Next.get(i));
        }
    }

    public Integer[] getTourOrder() {
        Integer[] tourOrder = new Integer[this.size()];
        for (int i = 0; i < this.size(); i++) {
            tourOrder[i] = this.get(i).getNUMBER();
        }
        return tourOrder;
    }

    @Override
    public HamiltonCycle clone() {
        HamiltonCycle X = new HamiltonCycle();
        for (City C : this) {
            X.add(C.clone());
        }
        X.setAverageFitness(averageFitness);
        return X;
    }

    public double calculateCost() {
        // add the distance from last city to first.
        double result = OnePlusOneEA.distanceBetween(this.get(0), this.get(this.size() - 1));

        // add the remaining distances
        for (int i = 0; i < this.size() - 1; i++) {
            result += OnePlusOneEA.distanceBetween(this.get(i), this.get(i + 1));
        }
        return result;
    }

    public int getNextPos_num(int thisPos_num) {    // small function for reducing code needed to find cyclic iterators
        if (thisPos_num >= this.size() - 1) {
            return thisPos_num + 1 - size();
        } else {
            return thisPos_num + 1;
        }
    }

    public int getPrevPos_num(int thisPos_num) {    // small function for reducing code needed to find cyclic iterators
        if (thisPos_num <= 0) {
            return this.size() - 1 + thisPos_num;
        } else {
            return thisPos_num - 1;
        }
    }

    public boolean isIdenticalTo(HamiltonCycle other) {
        if (other == null) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getNUMBER() != other.get(i).getNUMBER()) {
                return false;
            }
        }
        return true;   // returns 1 if numbers are exact match
    }

    /**
     * Collections.sort(instance) should sort the array in descenting order.
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(HamiltonCycle other) {
        if (this.averageFitness == other.getAverageFitness()) {
            return 0;
        } else if (this.averageFitness > other.getAverageFitness()) {
            return -1;
        } else {
            return 1;
        }
    }

    public void printCityInfo() {
        HamiltonCycle temp = this.clone();
        Collections.sort(temp);
        for (City C : temp) {
            System.out.format("%d %.4f %.4f\n", C.getNUMBER(), (double) C.getX(), (double) C.getY());
        }
    }

    public int getPosByCityNumber(int number) {
        for (int i = 0; i < this.size(); i++) {
            City C = this.get(i);
            if (C.getNUMBER() == number) {
                return i;
            }
        }
        return -1;
    }

    public City getCityByCityNumber(int number) {
        for (City C : this) {
            if (C.getNUMBER() == number) {
                return C;
            }
        }
        return null;
    }

    public boolean containsCityPosition(City checkee) {
        for (City C : this) {
            if ((checkee.getX() == C.getX()) && (checkee.getY() == C.getY())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkForDuplicates() {
        for (int i = 0; i < this.size(); i++) {
            City checkee = this.get(i);
            for (int j = 0; j < this.size(); j++) {
                if (i != j) {
                    City temp = this.get(j);
                    if ((checkee.getX() == temp.getX()) && (checkee.getY() == temp.getY())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void renumber() {
        for (int i = 0; i < this.size(); i++) {
            City C = this.get(i);
            C.setNUMBER(i + 1);
        }
    }

    public void normaliseCitySpace() {  // normalise the city positions to fit in a [20,20]
        double maxPosX = 0.0;
        double maxPosY = 0.0;
        for (City C : this) {
            if (C.getX() > maxPosX) {
                maxPosX = C.getX();
            }
            if (C.getY() > maxPosY) {
                maxPosY = C.getY();
            }
        }
        double rescaleFactorX = 20.0 / maxPosX;
        double rescaleFactorY = 20.0 / maxPosY;

        for (City C : this) {
            C.setX(C.getX() * rescaleFactorX);
            C.setY(C.getY() * rescaleFactorY);
        }
    }

    public boolean hasDuplicateCityInstancesWith(HamiltonCycle other) {
        for (City C : other) {
            if (this.contains(C)) {  // check for exact city instance, not just position
                return true;
            }
        }
        return false;
    }

    public void setTourLengthAsFitness() {
        this.averageFitness = calculateCost();
    }

    public void setAverageFitness(double averageFitness) {
        this.averageFitness = averageFitness;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

}
