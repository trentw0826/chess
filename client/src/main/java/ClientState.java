import command.Command;

import java.util.List;

import static command.Command.Commands.*;

enum ClientState {
  PRE_LOGIN(List.of(REGISTER, LOGIN, HELP, QUIT)),
  POST_LOGIN(List.of(LIST, CREATE, JOIN, OBSERVE, LOGOUT, HELP)),
  GAMEPLAY(List.of(DRAW, MOVE, RESIGN, LEAVE, HIGHLIGHT, HELP)),
  OBSERVING(List.of(DRAW, LEAVE, HIGHLIGHT, HELP));

  private final List<Command.Commands> associatedCommands;

  ClientState(List<Command.Commands> associatedCommands) {
    this.associatedCommands = associatedCommands;
  }

  public List<Command.Commands> getAssociatedCommands() {
    return associatedCommands;
  }
}
