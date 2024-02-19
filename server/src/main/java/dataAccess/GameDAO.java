package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public interface GameDAO extends DataAccessObject{

  /**
   * Creates a new chess game in the game database.
   *
   * @param gameData  game data to be inserted
   * @throws DataAccessException  game data could not be inserted
   */
  void createGame(GameData gameData) throws DataAccessException;


  /**
   * @return  a set of all the games in the game database
   */
  HashSet<GameData> listGames();


  /**
   * Gets a game from the game database.
   *
   * @param gameID  desired game ID
   * @return        the game data of the associated game
   * @throws DataAccessException  no game data is associated with the game ID
   */
  GameData getGame(int gameID) throws DataAccessException;

//TODO: Do we use a ChessGame, GameData, or String in updateGame method?
  /**
   * Updates a game in the game database.
   *
   * @param gameID  desired game ID
   * @param game    the new chess string for the associated game
   * @throws DataAccessException  no game data is associated with the game ID
   */
  void updateGame(int gameID, ChessGame game) throws DataAccessException;
}
