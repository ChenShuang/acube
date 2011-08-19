package acube.transform;

import java.util.Arrays;
import java.util.Set;
import acube.Turn;

abstract class MoveTable implements TurnTable {
  private final TurnTable move;
  protected final int[] turnIndices = new int[Turn.size];

  static final MoveTable instance(final TurnTable move, final Turn[][][] base) {
    return move.stateSize() <= 65536 ? new MoveTableShort(move, base) : new MoveTableLong(move, base);
  }

  protected MoveTable(final TurnTable move) {
    this.move = move;
    Arrays.fill(turnIndices, -1);
  }

  @Override
  public final int stateSize() {
    return move.stateSize();
  }

  @Override
  public final int startSize() {
    return move.startSize();
  }

  @Override
  public final int start(final int i) {
    return move.start(i);
  }

  @Override
  public final Set<Turn> turnMask() {
    return move.turnMask();
  }

  @Override
  public final Turn[] turnMaskArray() {
    return move.turnMaskArray();
  }

  protected abstract void set(int turnIndex, int state, int newstate);

  protected final void fill(final Turn[][][] base) { // template method
    final Turn[] turns = move.turnMaskArray();
    for (int turnIndex = 0; turnIndex < turns.length; turnIndex++)
      turnIndices[turns[turnIndex].ordinal()] = turnIndex;
    for (final Turn turn : turns) {
      final Turn[] generatorTurns = getGenerator(turn, base);
      for (int stateIndex = 0; stateIndex < move.stateSize(); stateIndex++) {
        int newStateIndex = stateIndex;
        for (final Turn generatorTurn : generatorTurns)
          newStateIndex = move.turn(generatorTurn, newStateIndex);
        set(turnIndices[turn.ordinal()], stateIndex, newStateIndex);
      }
    }
  }

  private Turn[] getGenerator(final Turn turn, final Turn[][][] base) {
    for (final Turn[][] row : base)
      if (turn.equals(row[0][0]))
        return row[1];
    return null;
  }
}