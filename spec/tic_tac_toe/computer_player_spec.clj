(ns tic-tac-toe.computer-player-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-player :as sut]
            [tic-tac-toe.game-board :as board]))

(describe "an unbeatable tic tac toe player"

  (it "plays a winning move if one is available"
    (should= {:row 0 :column 2} (sut/generate-move [[:o :o ""]
                                                    [:x :x ""]
                                                    [:x "" ""]]))
    (should= {:row 0 :column 1} (sut/generate-move [[:o "" :o] [:x :x ""] [:x "" ""]])))

  (it "blocks the opponent from winning the game"
    (should= {:row 0 :column 2} (sut/generate-move [[:x :x ""]
                                                    [:o "" ""]
                                                    [:x "" :o]]))
    (should= {:row 0 :column 1} (sut/generate-move [[:x "" :x]
                                                    [:o "" ""]
                                                    [:x "" :o]])))
  (it "plays the center tile when available"
    (should= {:row 1 :column 1} (sut/generate-move (board/new-board))))

  (it "plays a corner opposite the opponent"
    (should= {:row 0 :column 2} (sut/generate-move [["" "" ""]
                                                    ["" :x ""]
                                                    [:o "" ""]]))
    (should= {:row 2 :column 0} (sut/generate-move [["" "" :o]
                                                    ["" :x ""]
                                                    ["" "" ""]])))

  (it "plays in an empty corner"
    (should= {:row 0 :column 0} (sut/generate-move [["" "" ""]
                                                    ["" :x ""]
                                                    ["" "" ""]])))

  (it "plays in an empty side"
    (should= {:row 1 :column 0} (sut/generate-move [[:o :x :o]
                                                    ["" :x ""]
                                                    [:x :o :x]])))

  (it "plays to prevent a fork"
    (should= {:row 0 :column 1} (sut/generate-move [[:x "" ""]
                                                    ["" :o ""]
                                                    ["" "" :x]])))

  (it "plays the generated turn on the board"
    (should= [[:o "" ""]
              ["" :x ""]
              ["" "" ""]] (sut/play-computer-turn [["" "" ""]
                                                   ["" :x ""]
                                                   ["" "" ""]]))))