package response;

public class CreateGameResponse extends ServiceResponse {
  private Integer gameID = null;

  // Successful
  public CreateGameResponse(int gameID) {
    super();
    this.gameID = gameID;
  }

  // Unsuccessful
  public CreateGameResponse(String errorMessage) {
    super(errorMessage);
  }

  public Integer getGameID() {
    return gameID;
  }
}
