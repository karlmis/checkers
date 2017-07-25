package be.kapture.checkers;

import org.junit.Test;

import java.util.stream.IntStream;

import static be.kapture.checkers.Board.MAX_NUMBER_OF_ROWS_OR_COLUMNS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * Created by missika on 13/07/2017.
 */
public class CoordinatesTest {

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinates_XTooLow() throws Exception {
        new Coordinates(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinates_XTooHigh() throws Exception {
        new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS + 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinates_YTooLow() throws Exception {
        new Coordinates(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinates_YTooHigh() throws Exception {
        new Coordinates(0, MAX_NUMBER_OF_ROWS_OR_COLUMNS + 1);
    }

    @Test
    public void createCoordinates_Borders() throws Exception {
        new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS, MAX_NUMBER_OF_ROWS_OR_COLUMNS);
        new Coordinates(0, 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinates_NotOnWhite() throws Exception {
        new Coordinates(0, 1);
    }

    @Test
    public void getUpperNeighboursOFSameBoardColor() throws Exception {
        assertThat(new Coordinates(5, 5).getUpperNeighboursOfSameBoardColor(), contains(
                new Coordinates(4, 6),
                new Coordinates(6, 6)
        ));
    }

    @Test
    public void getUpperNeighboursOFSameBoardColor_Borders() throws Exception {
        assertThat(new Coordinates(0, 2).getUpperNeighboursOfSameBoardColor(), contains(new Coordinates(1, 3)));
        assertThat(new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS, 1).getUpperNeighboursOfSameBoardColor(), contains(new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS - 1, 2)));
    }

    @Test
    public void getLowerNeighboursOFSameBoardColor() throws Exception {
        assertThat(new Coordinates(5, 5).getLowerNeighboursOfSameBoardColor(), contains(
                new Coordinates(4, 4),
                new Coordinates(6, 4)
        ));
    }

    @Test
    public void getLowerNeighboursOFSameBoardColor_Borders() throws Exception {
        assertThat(new Coordinates(0, 2).getLowerNeighboursOfSameBoardColor(), contains(new Coordinates(1, 1)));
        assertThat(new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS, 1).getLowerNeighboursOfSameBoardColor(), contains(new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS - 1, 0)));
    }

    @Test
    public void getNextDoorNeighbour() throws Exception {
        assertThat(new Coordinates(5, 5).getNextDoorNeighbours(), contains(
                new Coordinates(3, 7),
                new Coordinates(7, 7),
                new Coordinates(7, 3),
                new Coordinates(3, 3)
        ));
    }

    @Test
    public void getNextDoorNeighbour_Borders() throws Exception {
        assertThat(new Coordinates(1, 1).getNextDoorNeighbours(), contains(
                new Coordinates(3, 3)
        ));
        assertThat(new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS, MAX_NUMBER_OF_ROWS_OR_COLUMNS).getNextDoorNeighbours(), contains(
                new Coordinates(MAX_NUMBER_OF_ROWS_OR_COLUMNS - 2, MAX_NUMBER_OF_ROWS_OR_COLUMNS - 2)
        ));
    }

    @Test
    public void getCoordinateBetweenCoordinates() throws Exception {
        assertThat(new Coordinates(5, 5).getSingleMiddleCoordinate(new Coordinates(7, 7)), is(new Coordinates(6, 6)));
        assertThat(new Coordinates(7, 7).getSingleMiddleCoordinate(new Coordinates(5,5)), is(new Coordinates(6, 6)));

        assertThat(new Coordinates(5, 5).getSingleMiddleCoordinate(new Coordinates(3, 7)), is(new Coordinates(4, 6)));
        assertThat(new Coordinates(3, 7).getSingleMiddleCoordinate(new Coordinates(5, 5)), is(new Coordinates(4, 6)));
    }

    @Test(expected = NullPointerException.class)
    public void getCoordinateBetweenCoordinates_Null() throws Exception {
        new Coordinates(5,5).getSingleMiddleCoordinate(null);
    }

    @Test
    public void getCoordinateBetweenCoordinates_IncorrectDistance() throws Exception {
        try{
            new Coordinates(0,0).getSingleMiddleCoordinate(new Coordinates(0,0));
            fail();
        }catch(IllegalArgumentException ignored){}
        try{
            new Coordinates(0,0).getSingleMiddleCoordinate(new Coordinates(1,1));
            fail();
        }catch(IllegalArgumentException ignored){}

        try{
            new Coordinates(1,1).getSingleMiddleCoordinate(new Coordinates(0,0));
            fail();
        }catch(IllegalArgumentException ignored){}


        IntStream.range(3, MAX_NUMBER_OF_ROWS_OR_COLUMNS).forEach( i-> testFailing(new Coordinates(0, 0), new Coordinates(i, i)));
        IntStream.range(3, MAX_NUMBER_OF_ROWS_OR_COLUMNS).forEach( i-> testFailing(new Coordinates(i, i), new Coordinates(0, 0)));
    }

    private void testFailing(Coordinates coordinates, Coordinates coordinates1) {
        try{
            coordinates.getSingleMiddleCoordinate(coordinates1);
            fail();
        }catch(IllegalArgumentException ignored){}

    }
}