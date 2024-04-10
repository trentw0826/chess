package facade;

import com.google.gson.Gson;
import exception.ResponseException;
import httpPath.HttpPath;
import request.httpRequests.*;
import response.*;
import playerColor.PlayerColor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

/**
 * Acts as an in-between for the client and the server;
 * Takes information for common requests, converts to proper http requests, and makes requests
 */
public class HttpCommunicator {
  private final String serverUrl;

  public HttpCommunicator(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public RegisterResponse registerUser(String username, String password, String email) throws ResponseException {
    RegisterRequest registerRequest = new RegisterRequest(username, password, email);
    return makeRequest("POST", HttpPath.PATHS.USER.getPath(), registerRequest, RegisterResponse.class, null);
  }

  public LoginResponse login(String username, String password) throws ResponseException {
    LoginRequest loginRequest = new LoginRequest(username, password);
    return makeRequest("POST", HttpPath.PATHS.SESSION.getPath(), loginRequest, LoginResponse.class, null);
  }

  public ListGamesResponse listGames(String auth) throws ResponseException {
   return makeRequest("GET", HttpPath.PATHS.GAME.getPath(), null, ListGamesResponse.class, auth);
  }

  public CreateGameResponse createGame(String gameName, String auth) throws ResponseException {
    CreateGameRequest createGameRequest = new CreateGameRequest(gameName, auth);
    return makeRequest("POST", HttpPath.PATHS.GAME.getPath(), createGameRequest, CreateGameResponse.class, auth);
  }

  public JoinGameResponse joinGame(int gameID, String auth, PlayerColor color) throws ResponseException {
    JoinGameRequest joinGameRequest = new JoinGameRequest(auth, color, gameID);
    return makeRequest("PUT", HttpPath.PATHS.GAME.getPath(), joinGameRequest, JoinGameResponse.class, auth);
  }

  public LogoutResponse logout(String auth) throws ResponseException {
    LogoutRequest logoutRequest = new LogoutRequest(auth);
    return makeRequest("DELETE", HttpPath.PATHS.SESSION.getPath(), logoutRequest, null, auth);
  }

  public void clear() throws ResponseException {
    makeRequest("DELETE", HttpPath.PATHS.DB.getPath(), null, null, null);
  }


  /**
   * Makes an http request to the server.
   *
   * @param method        http method
   * @param path          path to desired route (the server URL serving as root)
   * @param request       request object to be serialized into request
   * @param responseClass class of desired response object
   * @return              response object of type T
   * @param <T>           type of desired response
   * @throws ResponseException  if exception thrown during request
   */
  private <T> T makeRequest(String method, String path, ServiceRequest request,
                            Class<T> responseClass, String auth) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);

      if (!Objects.equals(method, "GET")) {
        http.setDoOutput(true);
      }

      if (auth != null) {
        http.addRequestProperty("authorization", auth);
      }

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    }
    catch (ResponseException re) {
      throw re;
    }
    catch (Exception e) {
      throw new ResponseException(e.getMessage(), 500);
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException("Failure", status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private static boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}


