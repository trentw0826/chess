package httpPath;

/**
 * Contains enum values associated with needed path strings
 */
public class HttpPath {
  public enum PATHS {
    DB("/db"),
    USER("/user"),
    SESSION("/session"),
    GAME("/game");

    private final String path;

    PATHS(String path) {
      this.path = path;
    }

    public String getPath() {
      return path;
    }
  }
}
