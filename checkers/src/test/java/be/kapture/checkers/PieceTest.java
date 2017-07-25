package be.kapture.checkers;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by missika on 13/07/2017.
 */
public class PieceTest {
    @Test(expected = NullPointerException.class)
    public void createPosition_TypeNotNull() throws Exception {
        Piece.createPiece(null, Color.WHITE);
    }

    @Test(expected = NullPointerException.class)
    public void createPosition_ColorNotNull() throws Exception {
        Piece.createPiece(PieceType.PAWN, null);
    }

    @Test
    public void createEmptyPosition() throws Exception {
        Piece emptyPiece = Piece.createEmptyPosition();

        assertThat(emptyPiece.getColor(), nullValue());
        assertThat(emptyPiece.getType(), nullValue());
        assertThat(emptyPiece.isEmpty(), is(true));
    }



}