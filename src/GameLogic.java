import java.util.*;

/**
 * Game Logic - Handles all game rules, scoring, and AI
 * This class manages the game state and implements advanced features
 */
public class GameLogic {
    private GameMode gameMode;
    private int totalRounds;
    private int currentRound;
    private int player1Score;
    private int player2Score;
    
    // Current round state
    private Move player1Move;
    private Move player2Move;
    private boolean player1Cheating;
    private boolean player2Cheating;
    
    // AI Strategy for computer player
    private AIStrategy aiStrategy;
    
    // Game history for statistics and AI learning
    private List<RoundResult> gameHistory;
    
    public GameLogic(GameMode mode, int rounds) {
        this.gameMode = mode;
        this.totalRounds = rounds;
        this.currentRound = 0;
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameHistory = new ArrayList<>();
        
        if (mode == GameMode.PVC) {
            this.aiStrategy = new AIStrategy();
        }
    }
    
    /**
     * Start a new round
     */
    public void startNewRound() {
        currentRound++;
        player1Move = null;
        player2Move = null;
        player1Cheating = false;
        player2Cheating = false;
    }
    
    /**
     * Finish the current round and calculate results
     */
    public GameResult finishRound() {
        System.out.println("Finishing round - Player 1: " + player1Move + ", Player 2: " + player2Move);
        
        if (player1Move == null || player2Move == null) {
            System.out.println("Missing moves - Player 1: " + player1Move + ", Player 2: " + player2Move);
            // Handle case where one or both players didn't make a move
            if (player1Move == null && player2Move == null) {
                System.out.println("Both players missed - draw");
                return GameResult.DRAW;
            } else if (player1Move == null) {
                System.out.println("Player 1 missed - player 2 wins");
                player2Score++;
                return GameResult.LOSE;
            } else {
                System.out.println("Player 2 missed - player 1 wins");
                player1Score++;
                return GameResult.WIN;
            }
        }
        
        GameResult result = player1Move.compare(player2Move);
        System.out.println("Game result: " + result + " (" + player1Move + " vs " + player2Move + ")");
        
        // Update scores
        switch (result) {
            case WIN:
                player1Score++;
                System.out.println("Player 1 wins round - score now " + player1Score + "-" + player2Score);
                break;
            case LOSE:
                player2Score++;
                System.out.println("Player 2 wins round - score now " + player1Score + "-" + player2Score);
                break;
            case DRAW:
                System.out.println("Draw - score stays " + player1Score + "-" + player2Score);
                // No score change
                break;
        }
        
        // Record the round for history and AI learning
        RoundResult roundResult = new RoundResult(player1Move, player2Move, result);
        gameHistory.add(roundResult);
        
        // Update AI strategy if playing against computer
        if (gameMode == GameMode.PVC && aiStrategy != null) {
            aiStrategy.learnFromRound(player1Move, player2Move, result);
        }
        
        return result;
    }
    
    /**
     * Get computer move using AI strategy
     */
    public Move getComputerMove() {
        if (aiStrategy != null) {
            return aiStrategy.getNextMove(gameHistory);
        } else {
            return Move.getRandomMove();
        }
    }
    
    /**
     * Determine the overall game winner
     */
    public String getGameWinner() {
        if (player1Score > player2Score) {
            if (gameMode == GameMode.PVC) {
                return "You Win!";
            } else {
                return "Player 1 Wins!";
            }
        } else if (player2Score > player1Score) {
            if (gameMode == GameMode.PVC) {
                return "Computer Wins!";
            } else {
                return "Player 2 Wins!";
            }
        } else {
            return "It's a Tie!";
        }
    }
    
    /**
     * Get formatted score text
     */
    public String getScoreText() {
        return player1Score + " - " + player2Score;
    }
    
    /**
     * Check if there are more rounds to play
     */
    public boolean hasMoreRounds() {
        return currentRound < totalRounds;
    }
    
    /**
     * Get game statistics
     */
    public GameStatistics getStatistics() {
        return new GameStatistics(gameHistory, player1Score, player2Score, gameMode);
    }
    
    // Getters and Setters
    public GameMode getGameMode() { return gameMode; }
    public int getCurrentRound() { return currentRound; }
    public int getTotalRounds() { return totalRounds; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    
    public Move getPlayer1Move() { return player1Move; }
    public void setPlayer1Move(Move move) { this.player1Move = move; }
    
    public Move getPlayer2Move() { return player2Move; }
    public void setPlayer2Move(Move move) { this.player2Move = move; }
    
    public boolean isPlayer1Cheating() { return player1Cheating; }
    public void setPlayer1Cheating(boolean cheating) { this.player1Cheating = cheating; }
    
    public boolean isPlayer2Cheating() { return player2Cheating; }
    public void setPlayer2Cheating(boolean cheating) { this.player2Cheating = cheating; }
    
    public void addWinToPlayer1() { this.player1Score++; }
    public void addWinToPlayer2() { this.player2Score++; }
}

/**
 * AI Strategy for computer player
 * Implements intelligent move selection based on player patterns
 */
class AIStrategy {
    private Map<Move, Integer> playerMoveFrequency;
    private List<Move> recentPlayerMoves;
    private Random random;
    
    private static final int LEARNING_WINDOW = 5; // Consider last 5 moves for pattern detection
    
    public AIStrategy() {
        this.playerMoveFrequency = new HashMap<>();
        this.recentPlayerMoves = new ArrayList<>();
        this.random = new Random();
        
        // Initialize frequency map
        for (Move move : Move.values()) {
            playerMoveFrequency.put(move, 0);
        }
    }
    
    /**
     * Learn from the completed round
     */
    public void learnFromRound(Move playerMove, Move computerMove, GameResult result) {
        if (playerMove != null) {
            // Update frequency count
            playerMoveFrequency.put(playerMove, playerMoveFrequency.get(playerMove) + 1);
            
            // Add to recent moves (keep only last LEARNING_WINDOW moves)
            recentPlayerMoves.add(playerMove);
            if (recentPlayerMoves.size() > LEARNING_WINDOW) {
                recentPlayerMoves.remove(0);
            }
        }
    }
    
    /**
     * Get the next move for the computer
     */
    public Move getNextMove(List<RoundResult> gameHistory) {
        if (gameHistory.isEmpty()) {
            // First round - random move
            return Move.getRandomMove();
        }
        
        // Use different strategies based on game progress
        int rounds = gameHistory.size();
        
        if (rounds < 3) {
            // Early game - mostly random with slight frequency bias
            return getRandomWithBias();
        } else {
            // Mid/late game - use pattern detection
            return getStrategicMove();
        }
    }
    
    /**
     * Get a random move with slight bias towards countering frequent player moves
     */
    private Move getRandomWithBias() {
        if (random.nextDouble() < 0.7) {
            // 70% chance of random move
            return Move.getRandomMove();
        } else {
            // 30% chance of countering most frequent player move
            Move mostFrequent = getMostFrequentPlayerMove();
            return getCounterMove(mostFrequent);
        }
    }
    
    /**
     * Get a strategic move based on pattern analysis
     */
    private Move getStrategicMove() {
        // Strategy 1: Counter the most frequent recent move (40% chance)
        if (random.nextDouble() < 0.4) {
            Move predicted = getMostFrequentRecentMove();
            return getCounterMove(predicted);
        }
        
        // Strategy 2: Detect and counter patterns (30% chance)
        if (random.nextDouble() < 0.5) { // 30% of remaining 60%
            Move predicted = detectPattern();
            if (predicted != null) {
                return getCounterMove(predicted);
            }
        }
        
        // Strategy 3: Anti-frequency strategy (20% chance)
        if (random.nextDouble() < 0.67) { // 20% of remaining 30%
            return getLeastCounteredMove();
        }
        
        // Strategy 4: Random move (10% chance)
        return Move.getRandomMove();
    }
    
    /**
     * Get the most frequent move from all player moves
     */
    private Move getMostFrequentPlayerMove() {
        Move mostFrequent = Move.ROCK;
        int maxCount = 0;
        
        for (Map.Entry<Move, Integer> entry : playerMoveFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    /**
     * Get the most frequent move from recent moves
     */
    private Move getMostFrequentRecentMove() {
        if (recentPlayerMoves.isEmpty()) {
            return Move.getRandomMove();
        }
        
        Map<Move, Integer> recentFreq = new HashMap<>();
        for (Move move : Move.values()) {
            recentFreq.put(move, 0);
        }
        
        for (Move move : recentPlayerMoves) {
            recentFreq.put(move, recentFreq.get(move) + 1);
        }
        
        Move mostFrequent = Move.ROCK;
        int maxCount = 0;
        
        for (Map.Entry<Move, Integer> entry : recentFreq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    /**
     * Detect simple patterns in recent moves
     */
    private Move detectPattern() {
        if (recentPlayerMoves.size() < 3) {
            return null;
        }
        
        // Check for alternating pattern
        if (recentPlayerMoves.size() >= 4) {
            Move last = recentPlayerMoves.get(recentPlayerMoves.size() - 1);
            Move secondLast = recentPlayerMoves.get(recentPlayerMoves.size() - 2);
            Move thirdLast = recentPlayerMoves.get(recentPlayerMoves.size() - 3);
            Move fourthLast = recentPlayerMoves.get(recentPlayerMoves.size() - 4);
            
            // Check A-B-A-B pattern
            if (last == thirdLast && secondLast == fourthLast && last != secondLast) {
                return secondLast; // Predict next will be B
            }
        }
        
        // Check for sequence pattern (Rock -> Paper -> Scissors -> Rock)
        if (recentPlayerMoves.size() >= 3) {
            Move last = recentPlayerMoves.get(recentPlayerMoves.size() - 1);
            Move secondLast = recentPlayerMoves.get(recentPlayerMoves.size() - 2);
            Move thirdLast = recentPlayerMoves.get(recentPlayerMoves.size() - 3);
            
            if (isSequentialPattern(thirdLast, secondLast, last)) {
                return getNextInSequence(last);
            }
        }
        
        return null;
    }
    
    /**
     * Check if three moves form a sequential pattern
     */
    private boolean isSequentialPattern(Move first, Move second, Move third) {
        Move[] sequence = {Move.ROCK, Move.PAPER, Move.SCISSORS};
        
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == first && 
                sequence[(i + 1) % sequence.length] == second && 
                sequence[(i + 2) % sequence.length] == third) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get the next move in the Rock->Paper->Scissors sequence
     */
    private Move getNextInSequence(Move current) {
        switch (current) {
            case ROCK: return Move.PAPER;
            case PAPER: return Move.SCISSORS;
            case SCISSORS: return Move.ROCK;
            default: return Move.getRandomMove();
        }
    }
    
    /**
     * Get the move that counters the predicted move
     */
    private Move getCounterMove(Move predicted) {
        switch (predicted) {
            case ROCK: return Move.PAPER;      // Paper beats Rock
            case PAPER: return Move.SCISSORS;  // Scissors beats Paper
            case SCISSORS: return Move.ROCK;   // Rock beats Scissors
            default: return Move.getRandomMove();
        }
    }
    
    /**
     * Get a move that is least countered by the player's frequent moves
     */
    private Move getLeastCounteredMove() {
        // Find which of our moves would be least frequently beaten
        Map<Move, Integer> ourMoveBeatenCount = new HashMap<>();
        for (Move ourMove : Move.values()) {
            ourMoveBeatenCount.put(ourMove, 0);
        }
        
        for (Map.Entry<Move, Integer> entry : playerMoveFrequency.entrySet()) {
            Move playerMove = entry.getKey();
            int frequency = entry.getValue();
            
            // Count how often each of our moves would be beaten
            for (Move ourMove : Move.values()) {
                if (playerMove.compare(ourMove) == GameResult.WIN) {
                    ourMoveBeatenCount.put(ourMove, ourMoveBeatenCount.get(ourMove) + frequency);
                }
            }
        }
        
        // Return the move that gets beaten least
        Move best = Move.ROCK;
        int minBeaten = Integer.MAX_VALUE;
        
        for (Map.Entry<Move, Integer> entry : ourMoveBeatenCount.entrySet()) {
            if (entry.getValue() < minBeaten) {
                minBeaten = entry.getValue();
                best = entry.getKey();
            }
        }
        
        return best;
    }
}

/**
 * Round Result - Records the outcome of a single round
 */
class RoundResult {
    private Move player1Move;
    private Move player2Move;
    private GameResult result;
    
    public RoundResult(Move player1Move, Move player2Move, GameResult result) {
        this.player1Move = player1Move;
        this.player2Move = player2Move;
        this.result = result;
    }
    
    // Getters
    public Move getPlayer1Move() { return player1Move; }
    public Move getPlayer2Move() { return player2Move; }
    public GameResult getResult() { return result; }
}

/**
 * Game Statistics - Provides detailed game analysis
 */
class GameStatistics {
    private List<RoundResult> rounds;
    private int player1Score;
    private int player2Score;
    private GameMode gameMode;
    
    public GameStatistics(List<RoundResult> rounds, int p1Score, int p2Score, GameMode mode) {
        this.rounds = new ArrayList<>(rounds);
        this.player1Score = p1Score;
        this.player2Score = p2Score;
        this.gameMode = mode;
    }
    
    /**
     * Get move frequency for player 1
     */
    public Map<Move, Integer> getPlayer1MoveFrequency() {
        Map<Move, Integer> frequency = new HashMap<>();
        for (Move move : Move.values()) {
            frequency.put(move, 0);
        }
        
        for (RoundResult round : rounds) {
            if (round.getPlayer1Move() != null) {
                Move move = round.getPlayer1Move();
                frequency.put(move, frequency.get(move) + 1);
            }
        }
        
        return frequency;
    }
    
    /**
     * Get win percentage for player 1
     */
    public double getPlayer1WinPercentage() {
        if (rounds.isEmpty()) return 0.0;
        return (double) player1Score / rounds.size() * 100.0;
    }
    
    /**
     * Get formatted statistics summary
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Statistics:\n");
        sb.append("Total Rounds: ").append(rounds.size()).append("\n");
        sb.append("Final Score: ").append(player1Score).append(" - ").append(player2Score).append("\n");
        
        if (gameMode == GameMode.PVC) {
            sb.append("Your Win Rate: ").append(String.format("%.1f", getPlayer1WinPercentage())).append("%\n");
        } else {
            sb.append("Player 1 Win Rate: ").append(String.format("%.1f", getPlayer1WinPercentage())).append("%\n");
        }
        
        return sb.toString();
    }
}