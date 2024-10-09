(ns tic-tac-toe.computer-player-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-player :as sut]
            [tic-tac-toe.game-board :as board]))

(describe "an artificial tic tac toe player"
  (with-stubs)

  (it "plays a calculated move on hard difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-calculated-move (stub :generate-calculated-move {:return :some-coords})
                  sut/generate-random-move (stub :generate-random-move)]
      (sut/play-computer-turn :some-board :hard)
      (should-have-invoked :generate-calculated-move)
      (should-have-invoked :get-current-turn {:with [:some-board]})
      (should-have-invoked :set-square {:with [:some-coords :some-player :some-board]})))

  (it "plays a random move on easy difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-random-move (stub :generate-random-move {:return :some-coords})
                  sut/generate-calculated-move (stub :generate-calculated-move)]
      (sut/play-computer-turn :some-board :easy)
      (should-have-invoked :generate-random-move)
      (should-have-invoked :get-current-turn {:with [:some-board]})
      (should-have-invoked :set-square {:with [:some-coords :some-player :some-board]})))

  (it "uses a random generation type on med difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-random-move (stub :generate-random-move {:return :some-coords})
                  sut/generate-calculated-move (stub :generate-calculated-move)
                  sut/random-generation-type (stub :random-generation-type {:return :some-generator})]
      (sut/play-computer-turn :some-board :med)
      (should-have-invoked :random-generation-type)))

  (context "on hard difficulty"
  (it "plays a winning move if one is available"
    (should= {:row 0 :column 2} (sut/generate-calculated-move [[:o :o ""]
                                                    [:x :x ""]
                                                    [:x "" ""]]))
    (should= {:row 0 :column 1} (sut/generate-calculated-move [[:o "" :o] [:x :x ""] [:x "" ""]])))

  (it "blocks the opponent from winning the game"
    (should= {:row 0 :column 2} (sut/generate-calculated-move [[:x :x ""]
                                                    [:o "" ""]
                                                    [:x "" :o]]))
    (should= {:row 0 :column 1} (sut/generate-calculated-move [[:x "" :x]
                                                    [:o "" ""]
                                                    [:x "" :o]])))
  (it "plays the center tile when available"
    (should= {:row 1 :column 1} (sut/generate-calculated-move (board/new-board))))

  (it "plays a corner opposite the opponent"
    (should= {:row 0 :column 2} (sut/generate-calculated-move [["" "" ""]
                                                    ["" :x ""]
                                                    [:o "" ""]]))
    (should= {:row 2 :column 0} (sut/generate-calculated-move [["" "" :o]
                                                    ["" :x ""]
                                                    ["" "" ""]])))

  (it "plays in an empty corner"
    (should= {:row 0 :column 0} (sut/generate-calculated-move [["" "" ""]
                                                    ["" :x ""]
                                                    ["" "" ""]])))

  (it "plays in an empty side"
    (should= {:row 1 :column 0} (sut/generate-calculated-move [[:o :x :o]
                                                    ["" :x ""]
                                                    [:x :o :x]])))

  (it "plays to prevent a fork"
    (should= {:row 0 :column 1} (sut/generate-calculated-move [[:x "" ""]
                                                    ["" :o ""]
                                                    ["" "" :x]]))))
  (context "on easy mode"

    (it "generates a random move, move is valid"
      (with-redefs [rand-int (stub :rand-int)
                    board/valid-turn? (stub :valid-turn? {:return true})
                    board/set-square (stub :set-square)]
      (sut/generate-random-move :some-board)
      (should-have-invoked :rand-int {:with [3] :times 2})
      (should-have-invoked :valid-turn?))))
  (context "on med difficulty"
    (it "randomly generates a random move 10% of the time"
      (with-redefs [rand-int (stub :rand-int {:return 0})]
        (should= sut/generate-random-move (sut/random-generation-type))))
    (it "randomly generates a calculated move 90% of the time"
      (with-redefs [rand-int (stub :rand-int {:return 9})]
        (sut/random-generation-type)
        (should-have-invoked :rand-int {:with [9]})
        (should= sut/generate-calculated-move (sut/random-generation-type))))))