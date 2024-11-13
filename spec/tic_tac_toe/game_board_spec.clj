(ns tic-tac-toe.game-board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.game-board :as sut]))

(def empty-3x3-board [["" "" ""] ["" "" ""] ["" "" ""]])
(def empty-4x4-board [["" "" "" ""] ["" "" "" ""] ["" "" "" ""] ["" "" "" ""]])

(describe "A tic tac toe game board"

  (it "creates an empty 3x3 game board"
    (should= empty-3x3-board (sut/new-board 3 3))
    (should= empty-3x3-board (sut/new-board)))

  (it "creates an empty 4x4 game board"
    (should= empty-4x4-board (sut/new-board 4 4)))

  (it "counts values in a collection"
    (should= 0 (sut/count-values empty-3x3-board :x))
    (should= 9 (sut/count-values empty-3x3-board ""))
    (should= 0 (sut/count-values empty-4x4-board :x))
    (should= 16 (sut/count-values empty-4x4-board "")))

  (it "plays an x on the grid"
    (should= [[:x "" ""] ["" "" ""] ["" "" ""]] (sut/set-square {:row 0 :column 0} :x empty-3x3-board))
    (should= [["" :x ""] ["" "" ""] ["" "" ""]] (sut/set-square {:row 0 :column 1} :x empty-3x3-board))
    (should= [["" "" :x] ["" "" ""] ["" "" ""]] (sut/set-square {:row 0 :column 2} :x empty-3x3-board))
    (should= [["" "" ""] [:x "" ""] ["" "" ""]] (sut/set-square {:row 1 :column 0} :x empty-3x3-board))
    (should= [["" "" ""] ["" "" ""] [:x "" ""]] (sut/set-square {:row 2 :column 0} :x empty-3x3-board))
    (should= [["" "" "" ""]["" "" "" :x]["" "" "" ""]["" "" "" ""]] (sut/set-square {:row 1 :column 3} :x empty-4x4-board)))

  (it "plays an o on the grid"
    (should= [["" "" ""] ["" "" ""] ["" :o ""]] (sut/set-square {:row 2 :column 1} :o empty-3x3-board)))

  (it "checks if a board is full"
    (should-not (sut/full-board? empty-3x3-board))
    (should-not (sut/full-board? [[:x :o ""]
                                  [:x "" :o]
                                  [:o :x :x]]))
    (should (sut/full-board? [[:x :o :x]
                              [:x :o :o]
                              [:o :x :x]])))

  (it "checks if a turn is valid"
    (should (sut/valid-turn? {:row 0 :column 0} empty-3x3-board))
    (should-not (sut/valid-turn? {:row 0 :column 0} [[:x "" ""] ["" "" ""] ["" "" ""]])))

  (it "gets the current turn based on the board"
    (should= :x (sut/get-current-turn empty-3x3-board))
    (should= :o (sut/get-current-turn [[:x "" ""] ["" "" ""] ["" "" ""]])))

  (it "gets the next turn based on the board"
    (should= :o (sut/get-next-turn empty-3x3-board))
    (should= :x (sut/get-next-turn [[:x "" ""] ["" "" ""] ["" "" ""]])))

  (it "gets the winning token from the board"
    (should-not (sut/get-win empty-3x3-board))
    (should= :x (sut/get-win [[:x :x :x] ["" "" ""] ["" "" ""]]))
    (should= :x (sut/get-win [[:x "" ""] [:x "" ""] [:x "" ""]]))
    (should= :x (sut/get-win [[:x "" ""] ["" :x ""] ["" "" :x]]))
    (should= :o (sut/get-win [[:o :o :o] ["" "" ""] ["" "" ""]]))
    (should= :o (sut/get-win [[:o "" ""] [:o "" ""] [:o "" ""]]))
    (should= :o (sut/get-win [[:o "" ""] ["" :o ""] ["" "" :o]]))
    (should= :x (sut/get-win [[:x :x :x :x][""""""""][""""""""][""""""""]])))

  (it "checks if the game has been won"
    (should (sut/is-there-win? [[:x :x :x] ["" "" ""] ["" "" ""]]))
    (should (sut/is-there-win? [[:o :o :o] ["" "" ""] ["" "" ""]]))
    (should (sut/is-there-win? [[:x "" "" ""]["" :x "" ""]["" "" :x ""]["" "" "" :x]]))
    (should-not (sut/is-there-win? empty-3x3-board)))

  (it "checks if the game has been tied"
    (should (sut/is-there-tie? [[:x :o :x] [:x :o :x] [:o :x :o]]))
    (should (sut/is-there-tie? [[:x :o :x :o][:x :o :x :o][:x :o :x :o][:o :x :o :x]]))
    (should-not (sut/is-there-tie? [[:x :x :x] [:x :x :x] [:x :x :X]]))))

