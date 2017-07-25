package be.kapture.checkers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BoardTest {

    private Board board;

    @Mock
    private Coordinates coordinatesUpperLeft, coordinatesUpperRight, coordinatesLowerLeft, coordinatesLowerRight;
    @Mock
    private Coordinates coordinatesLanding2, coordinatesLanding1Occupied;
    @Mock
    private Coordinates coordinates1Landing, coordinates2Landing, coordinates1MiddleOccupied, coordinates2MiddleEmpty, coordinates2MiddleWhite, coordinates1MiddleBlack;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        board = new Board();
    }

    @Test
    public void addPiece() {
        board.addPiece(Color.BLACK, new Coordinates(1, 1));

        assertThat(board.getElements(), is(1));
        assertThat(board.getElements(Color.BLACK), is(1));
        assertThat(board.getElements(Color.WHITE), is(0));

        board.addPiece(Color.WHITE, new Coordinates(2, 2));
        assertThat(board.getElements(), is(2));
        assertThat(board.getElements(Color.BLACK), is(1));
        assertThat(board.getElements(Color.WHITE), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPiece_TwiceOnSamePlace() {
        board.addPiece(Color.BLACK, new Coordinates(1, 1));

        assertThat(board.getElements(), is(1));
        assertThat(board.getElements(Color.BLACK), is(1));
        assertThat(board.getElements(Color.WHITE), is(0));

        board.addPiece(Color.WHITE, new Coordinates(1, 1));
    }


    @Test(expected = NullPointerException.class)
    public void addPiece_NoColor() throws Exception {
        board.addPiece(Color.WHITE, new Coordinates(0, 0));

        board.addPiece(null, new Coordinates(0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void addPiece_NoCoordinates() throws Exception {
        board.addPiece(Color.WHITE, null);
    }

    @Test
    public void addKing() {
        board.addKing(Color.BLACK, new Coordinates(1, 1));

        assertThat(board.getElements(), is(1));
        assertThat(board.getElements(Color.BLACK), is(1));
        assertThat(board.getElements(Color.WHITE), is(0));

        board.addKing(Color.WHITE, new Coordinates(2, 2));
        assertThat(board.getElements(), is(2));
        assertThat(board.getElements(Color.BLACK), is(1));
        assertThat(board.getElements(Color.WHITE), is(1));
        assertThat(board.getElementsOfType(PieceType.KING), is(2));
    }

    @Test(expected = NullPointerException.class)
    public void addKing_NoColor() throws Exception {
        board.addKing(Color.WHITE, new Coordinates(0, 0));

        board.addKing(null, new Coordinates(0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void addKing_NoCoordinates() throws Exception {
        board.addKing(Color.WHITE, null);
    }

    @Test(expected = NullPointerException.class)
    public void getElementsOfType() throws Exception {
        board.getElementsOfType(null);
    }

    @Test(expected = NullPointerException.class)
    public void getElements() throws Exception {
        board.getElements(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMoves_NoPieceOnCoordinates() throws Exception {
        board.getMoves(new Coordinates(5, 5));
    }

    @Test
    public void getMoves_White() throws Exception {
        Coordinates coordinates = mock(Coordinates.class);
        when(coordinates.getUpperNeighboursOfSameBoardColor()).thenReturn(asList(coordinatesUpperLeft, coordinatesUpperRight));

        board.addPiece(Color.WHITE, coordinates);

        assertThat(board.getMoves(coordinates), contains(coordinatesUpperLeft, coordinatesUpperRight));
    }


    @Test
    public void getMoves_Black() throws Exception {
        Coordinates coordinates = mock(Coordinates.class);
        when(coordinates.getLowerNeighboursOfSameBoardColor()).thenReturn(asList(coordinatesLowerLeft, coordinatesLowerRight));

        board.addPiece(Color.BLACK, coordinates);

        assertThat(board.getMoves(coordinates), contains(coordinatesLowerLeft, coordinatesLowerRight));
    }


    @Test
    public void getMoves_Neighbours() throws Exception {
        Coordinates coordinatesWhite = mock(Coordinates.class);
        when(coordinatesWhite.getUpperNeighboursOfSameBoardColor()).thenReturn(asList(coordinatesUpperLeft, coordinatesUpperRight));

        board.addPiece(Color.WHITE, coordinatesWhite);
        board.addPiece(Color.WHITE, coordinatesUpperLeft);

        assertThat(board.getMoves(coordinatesWhite), contains(coordinatesUpperRight));

        Coordinates coordinatesBlack = mock(Coordinates.class);
        when(coordinatesBlack.getLowerNeighboursOfSameBoardColor()).thenReturn(asList(coordinatesLowerLeft, coordinatesLowerRight));

        board.addPiece(Color.BLACK, coordinatesBlack);
        board.addPiece(Color.BLACK, coordinatesLowerLeft);

        assertThat(board.getMoves(coordinatesBlack), contains(coordinatesLowerRight));
    }


    @Test
    public void getTake_LandingOccupied() throws Exception {
        Coordinates coordinates = mock(Coordinates.class);
        Coordinates coordinates2Middle = mock(Coordinates.class);

        when(coordinates.getNextDoorNeighbours()).thenReturn(asList(coordinatesLanding1Occupied, coordinatesLanding2));
        when(coordinates.getSingleMiddleCoordinate(coordinatesLanding2)).thenReturn(coordinates2Middle);

        board.addPiece(Color.BLACK, coordinates);
        board.addPiece(Color.WHITE, coordinates2Middle);

        board.addPiece(Color.BLACK, coordinatesLanding1Occupied);

        assertThat(board.getTakes(coordinates), contains(coordinatesLanding2));
    }

    @Test
    public void getTake_ToTakeAbsent() throws Exception {
        Coordinates coordinates = mock(Coordinates.class);
        when(coordinates.getNextDoorNeighbours()).thenReturn(asList(coordinates1Landing, coordinates2Landing));
        when(coordinates.getSingleMiddleCoordinate(coordinates1Landing)).thenReturn(coordinates1MiddleOccupied);
        when(coordinates.getSingleMiddleCoordinate(coordinates2Landing)).thenReturn(coordinates2MiddleEmpty);

        board.addPiece(Color.BLACK, coordinates);
        board.addPiece(Color.WHITE, coordinates1MiddleOccupied);

        assertThat(board.getTakes(coordinates), contains(coordinates1Landing));

    }

    @Test
    public void getTake_ToTakeSameColor() throws Exception {
        Coordinates coordinatesBlack = mock(Coordinates.class);
        when(coordinatesBlack.getNextDoorNeighbours()).thenReturn(asList(coordinates1Landing, coordinates2Landing));

        when(coordinatesBlack.getSingleMiddleCoordinate(coordinates1Landing)).thenReturn(coordinates1MiddleBlack);
        when(coordinatesBlack.getSingleMiddleCoordinate(coordinates2Landing)).thenReturn(coordinates2MiddleWhite);

        board.addPiece(Color.BLACK, coordinatesBlack);
        board.addPiece(Color.BLACK, coordinates1MiddleBlack);
        board.addPiece(Color.WHITE, coordinates2MiddleWhite);

        assertThat(board.getTakes(coordinatesBlack),contains(coordinates2Landing));

    }

    @Test(expected = NullPointerException.class)
    public void getTake_Null() throws Exception {
        board.getTakes(null);
    }

    @Test
    public void move() throws Exception {
        Coordinates originalCoordinates = new Coordinates(1, 1);
        Coordinates destinationCoordinates = new Coordinates(2,2);

        board.addPiece(Color.WHITE, originalCoordinates);
        board.move(originalCoordinates,destinationCoordinates);

        assertThat(board.getOccupiedCoordinates(), contains(destinationCoordinates));
    }

    @Test(expected = IllegalArgumentException.class)
    public void move_TooFar() throws Exception {
        Coordinates originalCoordinates = new Coordinates(1, 1);
        Coordinates destinationCoordinates = new Coordinates(3,3);

        board.addPiece(Color.WHITE, originalCoordinates);

        board.move(originalCoordinates,destinationCoordinates);
    }

    @Test(expected = IllegalArgumentException.class)
    public void move_ToOccupied() throws Exception {
        Coordinates originalCoordinates = new Coordinates(1, 1);
        Coordinates destinationCoordinates = new Coordinates(2,2);

        board.addPiece(Color.WHITE, originalCoordinates);
        board.addPiece(Color.WHITE, destinationCoordinates);

        board.move(originalCoordinates,destinationCoordinates);

    }


    @Test
    public void take() throws Exception {
        Coordinates startCoordinates = new Coordinates(1,1);
        Coordinates takenCoordinates = new Coordinates(2,2);
        Coordinates targetCoordinates = new Coordinates(3,3);

        board.addPiece(Color.WHITE, startCoordinates);
        board.addPiece(Color.BLACK, takenCoordinates);

        assertThat(board.getOccupiedCoordinates(), contains(startCoordinates, takenCoordinates));

        board.take(startCoordinates, targetCoordinates);

        assertThat(board.getOccupiedCoordinates(), contains(targetCoordinates));
    }
}