import java.util.*;

public class Carsh extends SynchronousGame {


    /*
    Plan for now:
    1D. swarm, splash, and point are your 3 options.
    5 spaces total. 0 and 4 are 'towers'. Towers attack.
    All characters start at tower space.
    Score is tower health (100 at first).
    If CarshCharacters see another Character that belongs to the opponent in their same spot, they attack.
     */

    private final int NUM_SPACES = 5;
    private final int INITIAL_TOWER_HEALTH = 100;
    private final int NUM_UNIQUE_CHARACTER_TYPES = 1;
    private final int MAX_ELIXIR = 10;
    private final int INITIAL_ELIXIR = 7;

    private int p0TowerHealth;
    private int p1TowerHealth;

    private int p0Elixir;
    private int p1Elixir;

    private LinkedList<LinkedList<CarshCharacter>> spaces;

    public Carsh(SynchronousGamePlayer p0, SynchronousGamePlayer p1) {
        // Set up players
        players = new ArrayList<>();
        players.add(p0);
        players.add(p1);

        // Set up spaces
        spaces = new LinkedList<>();
        for (int currentSpace = 0; currentSpace < NUM_SPACES; currentSpace++) {
            spaces.add(new LinkedList<>());
        }

        // Set up initial values
        p0TowerHealth = INITIAL_TOWER_HEALTH;
        p1TowerHealth = INITIAL_TOWER_HEALTH;

        p0Elixir = INITIAL_ELIXIR;
        p1Elixir = INITIAL_ELIXIR;

    }

    @Override
    public void tick() {
        /*
        Tick steps:
        * Make all player's moves.
        * Have all characters tick.
        * Move everyone that wants to.
        * Have everyone attack that wants to.
        * Kill ppl who died.
         */
        int p0Move = players.get(0).moveMaking;
        if (isLegal(0, p0Move)) {
            spaces.get(0).add(getNewCharacterFromType(0, p0Move));
        }

        int p1Move = players.get(1).moveMaking;
        if (isLegal(0, p0Move)) {
            spaces.get(spaces.size() - 1).add(getNewCharacterFromType(1, p1Move));
        }

    }

    private CarshCharacter getNewCharacterFromType(int player, int move) {
        if (move == 1) {
            return new PointCharacter(player);
        }
        return null;
    }

    @Override
    public boolean isLegal(int player, int move) {
        return false;
    }

    @Override
    public int getMaxLegalMove() {
        return NUM_UNIQUE_CHARACTER_TYPES;
    }

    @Override
    public int getScore(int player) {
        return 0;
    }

    private enum AttackType {
        POINT, SPLASH
    }

    private abstract static class CarshCharacter {
        protected int owningPlayer;
        protected int ticksUntilMove;
        protected int ticksUntilAttack;
        protected int health;
        protected boolean previousTickWasAttackMode;

        public CarshCharacter(int owningPlayer) {
            this.owningPlayer = owningPlayer;
            health = getInitialHealth();
            ticksUntilAttack = getTicksPerAttack();
            ticksUntilMove = getTicksPerMovement();
            previousTickWasAttackMode = true;
        }

        public abstract int getElixirCost();

        protected abstract int getInitialHealth();

        /**
         *
         * @return The number of ticks BETWEEN moves.
         */
        protected abstract int getTicksPerMovement();

        /**
         *
         * @return The number of ticks BETWEEN attacks.
         */
        protected abstract int getTicksPerAttack();
        public abstract int getAttackDamage();
        public abstract List<AttackType> getAttackTypes();


        public int getHealth() {
            return health;
        }

        public boolean getWantsToMoveRightNow() {
            return ticksUntilMove == 0;
        }

        public boolean getWantsToAttackRightNow() {
            return ticksUntilAttack == 0;
        }

        public void decrementHealth(int damage) {
            health -= damage;
        }

        public boolean isDead() {
            return health <= 0;
        }

        public int getOwningPlayer() {
            return owningPlayer;
        }

        public void resetAttackCounter() {
            ticksUntilAttack = getTicksPerAttack();
        }

        public void resetMoveCounter() {
            ticksUntilMove = getTicksPerMovement();
        }

        public void tick(boolean attackMode) {
            if (attackMode) {
                if (! previousTickWasAttackMode) {
                    resetAttackCounter();
                }
                ticksUntilAttack--;
                previousTickWasAttackMode = true;
            } else {
                if (previousTickWasAttackMode) {
                    resetMoveCounter();
                }
                ticksUntilMove--;
                previousTickWasAttackMode = false;
            }
        }
    }

    private static class PointCharacter extends CarshCharacter {

        public PointCharacter(int owningPlayer) {
            super(owningPlayer);
        }

        @Override
        public int getElixirCost() {
            return 4;
        }

        @Override
        protected int getInitialHealth() {
            return 50;
        }

        @Override
        protected int getTicksPerMovement() {
            return 3;
        }

        @Override
        protected int getTicksPerAttack() {
            return 2;
        }

        @Override
        public int getAttackDamage() {
            return 10;
        }

        @Override
        public List<AttackType> getAttackTypes() {
            ArrayList<AttackType> attackTypes = new ArrayList<>();
            attackTypes.add(AttackType.POINT);
            return attackTypes;
        }
    }
}

