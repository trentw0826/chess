package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import httpPath.HttpPath;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
  private final String serverUrl;

  public ServerFacade(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  // TODO make shared http paths enum between server & client
  public AuthData registerUser(UserData user) throws ResponseException {
    return makeRequest("POST", HttpPath.PATHS.USER.getPath(), user, AuthData.class, null);
  }

  public AuthData login(UserData userData) throws ResponseException {
    var path = "/session";
    return makeRequest("POST", HttpPath.PATHS.SESSION.getPath(), userData, AuthData.class, null);
  }

  public void logout() {
    // TODO Implement
  }

  public void listGames() {
    // TODO Implement
  }

  public void createGame() {
    // TODO Implement
  }

  public void joinGame() {
    // TODO Implement
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
  private <T> T makeRequest(String method, String path, Object request,
                            Class<T> responseClass, String auth) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      if (auth != null) {
        http.addRequestProperty("authorization", auth);
      }

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    }
    catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
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

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "Failure [" + status + "]");
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


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}


