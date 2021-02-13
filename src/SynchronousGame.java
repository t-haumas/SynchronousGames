import java.util.ArrayList;
import java.util.List;

/**
 * A synchronous game is defined by the property that all moves are
 * gathered at the same time, and the game is updated accordingly.
 *
 * Moves have to be ints because it provides an easy way to get a
 * list of all legal moves. Just iterate through all moves up to
 * max move, and check if they're all legal.
 *
 * --- Minimum move is 0 ---
 */
public abstract class SynchronousGame {

    protected List<SynchronousGamePlayer> players;

    public abstract void tick();

    public abstract boolean isLegal(int player, int move);

    public abstract int getMaxLegalMove();

    public List<Integer> getLegalMoves(int player) {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int move = 0; move <= getMaxLegalMove(); move++) {
            if (isLegal(player, move)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public abstract int getScore(int player);

}