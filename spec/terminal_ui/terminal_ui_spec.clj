(ns terminal-ui.terminal-ui-spec
  (:require [speclj.core :refer :all]
            [terminal-ui.terminal-ui :refer :all]))

(def empty-board [["" "" ""] ["" "" ""] ["" "" ""]])

(describe "A terminal based game of tic tac toe"
  (it "Starts the game of tic tac toe"
    (should= "Starting a new game of tic tac toe...\nPlayer Input Format: RC where R is the row index and C is the column index.\n" (with-out-str (start-game))))
  (it "Prints the game board to the terminal"
    (should= "  0 1 2\n0 ☐ ☐ ☐\n1 ☐ ☐ ☐\n2 ☐ ☐ ☐\n" (with-out-str (print-board empty-board)))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ ☐ ☐\n2 ☐ ☐ ☐\n" (with-out-str (print-board [[:x "" ""] ["" "" ""] ["" "" ""]])))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ x ☐\n2 ☐ ☐ ☐\n" (with-out-str (print-board [[:x "" ""] ["" :x ""] ["" "" ""]])))
    (should= "  0 1 2\n0 x ☐ ☐\n1 ☐ x ☐\n2 ☐ o ☐\n" (with-out-str (print-board [[:x "" ""] ["" :x ""] ["" :o ""]]))))
  (it "asks the correct player to play their turn"
    (should= "Player x please input your turn: \n" (with-out-str (print-turn empty-board)))
    (should= "Player o please input your turn: \n" (with-out-str (print-turn [[:x "" ""] ["" "" ""] ["" "" ""]]))))
  (it "Informs the users of a win"
    (should= "Player x has won the game!\n" (with-out-str (print-win [[:x :x :x] ["" "" ""] ["" "" ""]])))
    (should= "Player o has won the game!\n" (with-out-str (print-win [[:o :o :o] ["" "" ""] ["" "" ""]])))
    (should= "The game results in a tie...\n" (with-out-str (print-tie))))
  (it "Takes the users input and converts it into the proper format"
    (should= {:row 0 :column 0} (with-in-str "00" (get-player-turn empty-board)))
    (should= {:row 1 :column 1} (with-in-str "11" (get-player-turn empty-board)))
    (should= "Your turn is invalid, please select again: \n" (with-out-str (with-in-str "00\n01" (get-player-turn [[:x "" ""] ["" "" ""] ["" "" ""]]))))))

(run-specs)