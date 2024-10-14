(ns tic-tac-toe.terminal-ui.terminal-ui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.terminal-ui.terminal-ui :as sut]))

(def empty-board [["" "" ""] ["" "" ""] ["" "" ""]])

(describe "A terminal interface for a game of tic tac toe"
  (with-stubs)

  (it "prints the input format for the user interface"
    (should= "Player Input Format: RC where R is the row index and C is the column index.\n" (with-out-str (sut/print-input-format))))

  (it "alerts the user the program is starting a new game"
    (should= "Starting a new game of tic tac toe...\n" (with-out-str (sut/print-new-game-alert))))

  (it "prints the game board to the terminal"
    (should= "  0 1 2\n0 ☐ ☐ ☐\n1 ☐ ☐ ☐\n2 ☐ ☐ ☐\n" (with-out-str (sut/print-board empty-board)))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ ☐ ☐\n2 ☐ ☐ ☐\n" (with-out-str (sut/print-board [[:x "" ""] ["" "" ""] ["" "" ""]])))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ x ☐\n2 ☐ ☐ ☐\n" (with-out-str (sut/print-board [[:x "" ""] ["" :x ""] ["" "" ""]])))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ x ☐\n2 ☐ o ☐\n" (with-out-str (sut/print-board [[:x "" ""] ["" :x ""] ["" :o ""]]))))

  (it "asks the correct player to play their turn"
    (should= "Player x please input your turn: \n" (with-out-str (sut/print-turn empty-board)))
    (should= "Player o please input your turn: \n" (with-out-str (sut/print-turn [[:x "" ""] ["" "" ""] ["" "" ""]]))))

  (it "informs the users of a win"
    (should= "Player x has won the game!\n" (with-out-str (sut/print-win [[:x :x :x] ["" "" ""] ["" "" ""]])))
    (should= "Player o has won the game!\n" (with-out-str (sut/print-win [[:o :o :o] ["" "" ""] ["" "" ""]])))
    (should= "The game results in a tie...\n" (with-out-str (sut/print-tie))))

  (it "asks the user if it would like to play against another player or a computer"
    (should= "Would you like to play against a computer? (y/n)\n" (with-out-str (sut/ask-for-game-type))))

  (it "asks the user what computer difficulty it would like to play against"
    (should= "What difficulty would you like to set the computer at? (easy/med/hard)\n" (with-out-str (sut/ask-for-difficulty))))

  (it "asks the user if it would like to play as X or O"
    (should= "Would you like to play as X, or O? (x/o)\n" (with-out-str (sut/ask-for-player-token))))

  (context "User Input"

    (it "takes the users input and converts it into coordinates"
      (should= {:row 0 :column 0} (sut/input-to-coords "00"))
      (should= {:row 2 :column 1} (sut/input-to-coords "21")))

    (it "makes sure the coordinate input is valid"
      (with-redefs [sut/handle-invalid-coordinate-input (stub :handle-invalid-input)]
        (with-in-str "000\n00" (sut/get-player-move empty-board))
        (should-have-invoked :handle-invalid-input)
        (with-in-str "a\n00" (sut/get-player-move empty-board))
        (should-have-invoked :handle-invalid-input)
        (with-in-str "33\n00" (sut/get-player-move empty-board))
        (should-have-invoked :handle-invalid-input)
        (with-in-str "0\n00" (sut/get-player-move empty-board))
        (should-have-invoked :handle-invalid-input)))

    (it "makes sure the turn is valid"
      (with-redefs [sut/handle-invalid-turn (stub :handle-invalid-turn)]
        (with-in-str "00\n01" (sut/get-player-move [[:x "" ""] ["" "" ""] ["" "" ""]]))
        (should-have-invoked :handle-invalid-turn)))

    (it "asks the game type question and returns a game type based on the users response"
      (with-redefs [sut/ask-for-game-type (stub :ask-for-game-type)]
        (should= :versus-computer (with-in-str "y" (sut/get-game-type)))
        (should= :versus-player (with-in-str "n" (sut/get-game-type)))))

    (it "asks the difficulty question and returns a difficulty based on the users response"
      (with-redefs [sut/ask-for-difficulty (stub :ask-for-difficulty)]
        (should= :easy (with-in-str "easy" (sut/get-difficulty)))
        (should= :med (with-in-str "med" (sut/get-difficulty)))
        (should= :hard (with-in-str "hard" (sut/get-difficulty)))))

    (it "asks the token question and returns a token based on the users response"
      (with-redefs [sut/ask-for-player-token (stub :ask-for-player-token)]
        (should= :x (with-in-str "x" (sut/get-player-token)))
        (should= :o (with-in-str "o" (sut/get-player-token)))))))

