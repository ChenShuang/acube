package acube.pack;

public final class PackPositionUnordered extends Pack {
  private final boolean[] careMask;

  public PackPositionUnordered(final boolean[] mask, final boolean[] careMask, final int[] partIds) {
    super(Coder.unordered, CoderTools.maskIntersection(mask, careMask), partIds);
    this.careMask = careMask;
  }

  @Override
  public int startSize() {
    return coder.size(care(), careNotPos());
  }

  @Override
  public int start(final int x) {
    final int[] a = new int[care()];
    coder.decode(a, careNotPos(), x);
    for (int i = 0, j = 0; i < values.length; i++)
      values[i] = careMask[i] ? a[j++] : -1;
    return pack();
  }

  private int care() {
    int n = 0;
    for (int i = 0; i < values.length; i++)
      if (careMask[i])
        n++;
    return n;
  }

  private int careNotPos() {
    int n = 0;
    for (int i = 0; i < values.length; i++)
      if (careMask[i] && !usedMask[i])
        n++;
    return n;
  }

  @Override
  public String toString() {
    return CoderUnordered.toString(values);
  }
}