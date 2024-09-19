(ns tic-tac-toe.game-board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-board :refer :all]))

(describe "A tic tac toe game board"
          (it "creates a three by three grid"
              (should= [["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"]] (new-board)))
          (it "plays an x on the grid"
              (should= [["x" "-" "-"] ["-" "-" "-"] ["-" "-" "-"]] (play-x {:row 0 :column 0} (new-board)))
              (should= [["-" "x" "-"] ["-" "-" "-"] ["-" "-" "-"]] (play-x {:row 0 :column 1} (new-board)))
              (should= [["-" "-" "x"] ["-" "-" "-"] ["-" "-" "-"]] (play-x {:row 0 :column 2} (new-board)))
              (should= [["-" "-" "-"] ["x" "-" "-"] ["-" "-" "-"]] (play-x {:row 1 :column 0} (new-board)))
              (should= [["-" "-" "-"] ["-" "-" "-"] ["x" "-" "-"]] (play-x {:row 2 :column 0} (new-board)))
              )
          (it "plays an o on the grid"
              (should= [["-" "-" "-"] ["-" "-" "-"] ["-" "o" "-"]] (play-o {:row 2 :column 1} (new-board)))))

(run-specs)