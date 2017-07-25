package be.kapture.checkers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by missika on 13/07/2017.
 */
public class Piece {

    private PieceType type;
    private Color color;

    private Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public static Piece createPiece(PieceType type, Color color) {
        checkNotNull(type);
        checkNotNull(color);
        return new Piece(type, color);
    }

    @Deprecated
    public static Piece createEmptyPosition() {
        return new Piece(null, null);
    }


    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    @Deprecated
    public boolean isEmpty() {
        return type == null && color == null;
    }


}
