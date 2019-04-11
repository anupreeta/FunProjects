import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class RockPaperScissors {
    enum Result {
        WIN, LOSE, DRAW
    }

    enum Move {
        ROCK, PAPER, SCISSORS ;
        static {
            ROCK.beats = SCISSORS ;
            PAPER.beats = ROCK ;
            SCISSORS.beats = PAPER ;
        }
        private Move beats ;

        public boolean beats( Move other ) {
            return beats == other ;
        }
    }

    class PlayerMove {
        private final String player ;
        private final Move move ;

        public PlayerMove( String player, Move move ) {
            this.player = player ;
            this.move = move ;
        }

        @Override
        public String toString() {
            return String.format( "%s:%s", getPlayer(), getMove()) ;
        }

        public String getPlayer() {
            return player;
        }

        public Move getMove() {
            return move;
        }
    }

    class Turn {
        private final PlayerMove p1 ;
        private final PlayerMove p2 ;

        public Turn( PlayerMove p1, PlayerMove p2 ) {
            this.p1 = p1 ;
            this.p2 = p2 ;
        }

        @Override
        public String toString() {
            return String.format( "%s vs %s", getP1(), getP2()) ;
        }

        public PlayerMove getP1() {
            return p1;
        }

        public PlayerMove getP2() {
            return p2;
        }
    }

    private final Random random = new Random() ;

    Stream<Move> moves() {
        return Stream.generate( () -> Move.values()[ random.nextInt( Move.values().length ) ] ) ;
    }

    Stream<PlayerMove> player( String name ) {
        return moves().map( ( m ) -> new PlayerMove( name, m ) ) ;
    }

    Stream<Turn> turn( String player1, String player2 ) {
        // No zip for Streams :-(
        Iterator<PlayerMove> p1 = player( player1 ).iterator() ;
        Iterator<PlayerMove> p2 = player( player2 ).iterator() ;
        return Stream.generate( () -> new Turn( p1.next(), p2.next() ) ) ;
    }

    Result result( Turn t ) {
        return t.getP1().getMove() == t.getP2().getMove() ? Result.DRAW :
            t.getP1().getMove().beats( t.getP2().getMove()) ? Result.WIN :
                Result.LOSE ;
    }

    String resultMessage( Turn t, Result r ) {
        return r == Result.DRAW ? "DRAW" :
            r == Result.WIN ? String.format( "%s wins", t.getP1().getPlayer()) :
                String.format( "%s wins", t.getP2().getPlayer()) ;
    }

    String message( Turn t ) {
        return String.format( "%s : %s", t.toString(), resultMessage( t, result( t ) ) ) ;
    }

    Stream<String> game( String player1, String player2, int turns ) {
        return turn( player1, player2 ).limit( turns )
            .map( (t) -> message( t ) ) ;
    }

    public static void main( String[] args ) {
        new RockPaperScissors().game( "Jack", "Jill", 5 )
            .forEach( (m) -> System.out.println( m ) ) ;
    }
}