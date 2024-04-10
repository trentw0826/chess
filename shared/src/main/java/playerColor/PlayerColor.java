package playerColor;

public enum PlayerColor {
  WHITE,
  BLACK;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
