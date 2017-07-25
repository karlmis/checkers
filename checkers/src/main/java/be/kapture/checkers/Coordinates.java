package be.kapture.checkers;

import com.google.common.collect.Range;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Range.*;
import static java.util.Arrays.asList;

/**
 * Created by missika on 13/07/2017.
 */
public class Coordinates{
    private  static final Range<Integer> ALLOWED_VALUES = closed(0, Board.MAX_NUMBER_OF_ROWS_OR_COLUMNS);

    private int x, y;

    public Coordinates(int x, int y) {
        checkArgument(ALLOWED_VALUES.containsAll(asList(x, y)));
        checkArgument((x+y)%2==0);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public List<Coordinates> getUpperNeighboursOfSameBoardColor() {
        List<Coordinates> upperNeighboursOfSameBoardColor = new ArrayList<>();
        getCoordinates(x - 1, y + 1).ifPresent(upperNeighboursOfSameBoardColor::add);
        getCoordinates(x + 1, y + 1).ifPresent(upperNeighboursOfSameBoardColor::add);
        return upperNeighboursOfSameBoardColor;
    }

    private Optional<Coordinates> getCoordinates(int x, int y) {
        if (ALLOWED_VALUES.containsAll(asList(x, y))){
            return Optional.of(new Coordinates(x, y));
        }
        return Optional.empty();
    }

    public List<Coordinates> getLowerNeighboursOfSameBoardColor() {
        List<Coordinates> lowerNeighboursOfSameBoardColor = new ArrayList<>();
        getCoordinates(x - 1, y - 1).ifPresent(lowerNeighboursOfSameBoardColor::add);
        getCoordinates(x + 1, y - 1).ifPresent(lowerNeighboursOfSameBoardColor::add);
        return lowerNeighboursOfSameBoardColor;
    }

    public List<Coordinates> getNextDoorNeighbours() {
        ArrayList<Coordinates> res = new ArrayList<>();
        getCoordinates(x - 2, y +2).ifPresent(res::add);
        getCoordinates(x+2, y+2).ifPresent(res::add);
        getCoordinates(x +2, y - 2).ifPresent(res::add);
        getCoordinates(x - 2, y - 2).ifPresent(res::add);
        return res;
    }

    public Coordinates getSingleMiddleCoordinate(Coordinates coordinates) {
        checkNotNull(coordinates);
        checkArgument(Math.abs(x-coordinates.x)==2);
        checkArgument(Math.abs(y-coordinates.y)==2);

        return new Coordinates((x+coordinates.x)/2, (y+coordinates.y)/2);
    }

}
