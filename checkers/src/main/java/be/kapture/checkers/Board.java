package be.kapture.checkers;

import java.util.*;

import static be.kapture.checkers.Piece.createPiece;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Created by missika on 13/07/2017.
 */
public class Board {


    public static final int MAX_NUMBER_OF_ROWS_OR_COLUMNS = 9;
    public static final Comparator<Coordinates> COORDINATES_COMPARATOR = (c1, c2) -> Integer.compare(c1.getX(), c2.getX()) * 100 + Integer.compare(c1.getY(), c2.getY());
    private Map<Coordinates, Piece> grid= new HashMap<>();

    public void addPiece(Color color, Coordinates coordinates) {
        checkNotNull(color);
        checkNotNull(coordinates);

        addPawn(color, PieceType.PAWN, coordinates);
    }

    private void addPawn(Color color, PieceType pieceType, Coordinates coordinates) {
        if (grid.get(coordinates)!=null) {
            throw new IllegalArgumentException();
        }
        grid.put(coordinates, createPiece(pieceType,color));
    }

    public int getElements() {
         return grid.values().size();
    }

    public int getElements(Color color) {
        checkNotNull(color);
        return (int) grid.values().stream().filter(c->c.getColor()==color).count();

    }

    public void addKing(Color color, Coordinates coordinates) {
        checkNotNull(color);
        checkNotNull(coordinates);

        addPawn(color, PieceType.KING, coordinates);
    }

    public int getElementsOfType(PieceType pieceType) {
        checkNotNull(pieceType);

        return (int) grid.values().stream().filter(c->c.getType()==pieceType).count();
    }


    public List<Coordinates> getMoves(Coordinates coordinates) {
        checkArgument(grid.containsKey(coordinates));
        return getCoordinatesToMoveTo(coordinates).stream().filter(c -> !grid.containsKey(c)).collect(toList());
    }

    private List<Coordinates> getCoordinatesToMoveTo(Coordinates coordinates) {
        if(grid.get(coordinates).getColor()== Color.WHITE) {
            return coordinates.getUpperNeighboursOfSameBoardColor();
        }else {
            return coordinates.getLowerNeighboursOfSameBoardColor();
        }
    }

    public List<Coordinates> getTakes(Coordinates coordinate) {
        checkNotNull(coordinate);
        Color currentColor = grid.get(coordinate).getColor();
        return coordinate.getNextDoorNeighbours().stream()
                .filter(c -> !grid.containsKey(c))
                .filter(c -> isMiddleOccupiedAndDifferentColor(coordinate, c, currentColor))
                .collect(toList());
    }

    private boolean isMiddleOccupiedAndDifferentColor(Coordinates coordinate, Coordinates c, Color color) {
        return Optional.ofNullable(grid.get(coordinate.getSingleMiddleCoordinate(c)))
                .filter(p -> p.getColor() != color)
                .isPresent();
    }

    public List<Coordinates> getOccupiedCoordinates(){
        return grid.keySet().stream().sorted(COORDINATES_COMPARATOR).collect(toList());
    }

    public void move(Coordinates originalCoordinates, Coordinates destinationCoordinates) {
        checkArgument(getMoves(originalCoordinates).contains(destinationCoordinates));

        Piece piece = grid.remove(originalCoordinates);
        grid.put(destinationCoordinates, piece);

    }

    public void take(Coordinates startCoordinates, Coordinates targetCoordinates) {
        Coordinates middleCoordinate = startCoordinates.getSingleMiddleCoordinate(targetCoordinates);
        grid.remove(middleCoordinate);

        Piece takerPiece = grid.remove(startCoordinates);
        grid.put(targetCoordinates, takerPiece);
    }
}
