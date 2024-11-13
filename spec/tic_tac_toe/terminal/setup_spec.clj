(ns tic-tac-toe.terminal.setup-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.setup :as sut]
            [tic-tac-toe.terminal.ui :as terminal]))

(describe "Terminal Setup"
  (with-stubs)

  (it "handles the save data if a save is found"
    (with-redefs [db/existing-save? (stub :existing-save? {:return true})
                  sut/handle-save (stub :handle-save)
                  sut/create-new-game (stub :create-new-game)]
      (core/setup {:console :terminal})
      (should-have-invoked :handle-save)
      (should-not-have-invoked :create-new-game)))

  (it "handles the new game process if no save is found"
    (with-redefs [db/existing-save? (stub :existing-save? {:return false})
                  sut/handle-save (stub :handle-save)
                  sut/create-new-game (stub :create-new-game)]
      (core/setup {:console :terminal})
      (should-have-invoked :create-new-game)
      (should-not-have-invoked :handle-save)))

  (context "and when Save Data is Found"
    (it "loads the save data if the user wants to use it"
      (with-redefs [terminal/get-formatted-user-input (stub :user-input {:return "y"})
                    db/read-stored-game (stub :read-stored-game {:return {:some-stored-keys :some-stored-values}})
                    sut/create-new-game (stub :create-new-game)
                    println (stub :println)]
        (should= {:console :terminal :some-stored-keys :some-stored-values} (sut/handle-save {:console :terminal}))))

    (it "creates a new game if the user does not want to use it"
      (with-redefs [terminal/get-formatted-user-input (stub :user-input {:return "n"})
                    db/read-stored-game (stub :read-stored-game)
                    sut/create-new-game (stub :create-new-game)
                    println (stub :println)]
        (sut/handle-save {:console :terminal})
        (should-have-invoked :create-new-game {:with [{:console :terminal}]})
        (should-not-have-invoked :read-stored-game))))

  (context "Creating a New Game"
    (it "gets the game board from the user"
      (with-redefs [terminal/get-game-type (stub :get-game-type {:return :some-type})
                    terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
        (should= (board/new-board) (:board (sut/create-new-game {:console :terminal})))))

    (it "gets the game type from the user"
      (with-redefs [terminal/get-game-type (stub :get-game-type {:return :some-type})
                    terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
        (should= :some-type (:game-type (sut/create-new-game {:console :terminal})))))

    (it "sets the first turn of the game to player by default"
      (with-redefs [terminal/get-game-type (stub :get-game-type {:return :some-type})
                    terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
        (should= :player (:current-turn (sut/create-new-game {:console :terminal})))))

    (context "When it's a Versus Computer Game"
      (it "gets the difficulty from the user"
        (with-redefs [terminal/get-game-type (stub :get-game-type {:return :versus-computer})
                      terminal/get-difficulty (stub :get-difficulty {:return :some-difficulty})
                      terminal/get-player-token (stub :get-player-token {:return :some-token})
                      terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
          (should= :some-difficulty (:difficulty (sut/create-new-game {:console :terminal})))))

      (it "gets the player token from the user"
        (with-redefs [terminal/get-game-type (stub :get-game-type {:return :versus-computer})
                      terminal/get-difficulty (stub :get-difficulty {:return :some-difficulty})
                      terminal/get-player-token (stub :get-player-token {:return :some-token})
                      terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
          (should= :some-token (:player-token (sut/create-new-game {:console :terminal})))))

      (it "sets the first turn of the game to the player if they select x"
        (with-redefs [terminal/get-game-type (stub :get-game-type {:return :versus-computer})
                      terminal/get-difficulty (stub :get-difficulty {:return :some-difficulty})
                      terminal/get-player-token (stub :get-player-token {:return :x})
                      terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
          (should= :player (:current-turn (sut/create-new-game {:console :terminal})))))
      (it "sets the first turn of the game to the computer if they select o"
        (with-redefs [terminal/get-game-type (stub :get-game-type {:return :versus-computer})
                      terminal/get-difficulty (stub :get-difficulty {:return :some-difficulty})
                      terminal/get-player-token (stub :get-player-token {:return :o})
                      terminal/get-board-size (stub :get-board-size {:return (board/new-board)})]
          (should= :computer (:current-turn (sut/create-new-game {:console :terminal}))))))))
