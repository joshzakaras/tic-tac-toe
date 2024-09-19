(ns tic-tac-toe.tic-tac-toe-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.tic-tac-toe :refer :all]))

(def empty-board [["" "" ""] ["" "" ""] ["" "" ""]])

(describe "A tic tac toe game"
  (it "creates an empty game board"
    (should= empty-board (new-board)))
  (it "plays an x on the grid"
    (should= [[:x "" ""] ["" "" ""] ["" "" ""]] (play-x {:row 0 :column 0} empty-board))
    (should= [["" :x ""] ["" "" ""] ["" "" ""]] (play-x {:row 0 :column 1} empty-board))
    (should= [["" "" :x] ["" "" ""] ["" "" ""]] (play-x {:row 0 :column 2} empty-board))
    (should= [["" "" ""] [:x "" ""] ["" "" ""]] (play-x {:row 1 :column 0} empty-board))
    (should= [["" "" ""] ["" "" ""] [:x "" ""]] (play-x {:row 2 :column 0} empty-board))
    )
  (it "plays an o on the grid"
    (should= [["" "" ""] ["" "" ""] ["" :o ""]] (play-o {:row 2 :column 1} empty-board)))
  (it "counts values in a collection"
    (should= 0 (count-values empty-board :x))
    (should= 9 (count-values empty-board "")))
  (it "checks who's turn it is"
    (should= :x (check-current-turn empty-board))
    (should= :o (check-current-turn (play-x {:row 0 :column 0} empty-board))))
  (it "checks if a board is full"
    (should-not (full-board? empty-board))
    (should (full-board? [[:x :o :x] [:o :x :o] [:x :o :x]])))
  (it "checks if a turn is valid"
    (should (valid-turn? {:row 0 :column 0} empty-board))
    (should-not (valid-turn? {:row 0 :column 0} (play-x {:row 0 :column 0} empty-board)))))

(run-specs)