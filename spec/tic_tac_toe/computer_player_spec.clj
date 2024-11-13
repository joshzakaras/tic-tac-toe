(ns tic-tac-toe.computer-player-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-player :as sut]
            [tic-tac-toe.game-board :as board]))

(describe "an artificial tic tac toe player"
  (with-stubs)

  (it "plays a calculated move on hard difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-best-move (stub :generate-calculated-move {:return :some-coords})]
      (sut/play-computer-turn :some-board :hard)
      (should-have-invoked :generate-calculated-move)
      (should-have-invoked :get-current-turn {:with [:some-board]})
      (should-have-invoked :set-square {:with [:some-coords :some-player :some-board]})))

  (it "plays a random move on easy difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-random-move (stub :generate-random-move {:return :some-coords})
                  sut/generate-best-move (stub :generate-calculated-move)]
      (sut/play-computer-turn :some-board :easy)
      (should-have-invoked :generate-random-move)
      (should-have-invoked :get-current-turn {:with [:some-board]})
      (should-have-invoked :set-square {:with [:some-coords :some-player :some-board]})))

  (it "uses a random generation type on med difficulty"
    (with-redefs [board/set-square (stub :set-square)
                  board/get-current-turn (stub :get-current-turn {:return :some-player})
                  sut/generate-random-move (stub :generate-random-move {:return :some-coords})
                  sut/generate-best-move (stub :generate-calculated-move)
                  sut/random-generation-type (stub :random-generation-type {:return :some-generator})]
      (sut/play-computer-turn :some-board :med)
      (should-have-invoked :random-generation-type)))

  (context "on hard difficulty"
    (it "generates a list of moves"
      (should= [{:row 0 :column 0} {:row 0 :column 1} {:row 1 :column 0} {:row 1 :column 1}] (sut/generate-coords (board/new-board 2 2)))
      (should= [{:row 0 :column 0} {:row 0 :column 1} {:row 0 :column 2} {:row 0 :column 3}
                {:row 1 :column 0} {:row 1 :column 1} {:row 1 :column 2} {:row 1 :column 3}
                {:row 2 :column 0} {:row 2 :column 1} {:row 2 :column 2} {:row 2 :column 3}
                {:row 3 :column 0} {:row 3 :column 1} {:row 3 :column 2} {:row 3 :column 3}] (sut/generate-coords (board/new-board 4 4))))

    (it "filters for only legal moves"
      (should= [{:row 0 :column 0}] (sut/generate-legal-moves [["" :x] [:x :x]]))
      (should= [{:row 0 :column 0} {:row 1 :column 1}] (sut/generate-legal-moves [["" :x :x] [:x "" :x] [:x :x :X]])))

    (it "scores using alpha beta correctly"
      (should= 1 (sut/maybe-alpha-beta min [["" :x :x]
                                            ["" :o :o]
                                            ["" "" ""]] :x)))

    (context "Scoring Final Boards"
      (it "gives a board a score of 100 when the provided token won"
        (should= 1 (sut/score-board [[:x :x :x] ["" "" ""] ["" "" ""]] :x)))

      (it "gives a board a score of 0 when there is a tie"
        (should= 0 (sut/score-board [[:x :o :x]
                                     [:x :o :x]
                                     [:o :x :o]] :x)))

      (it "gives a board a score of -100 when the provided token lost"
        (should= -1 (sut/score-board [[:o :o :o] ["" "" ""] ["" "" ""]] :x))))

    (it "wins the game when possible"
      (should= {:row 0 :column 0} (sut/generate-best-move [["" :x :x]
                                                           [:x :o :o]
                                                           [:o :x :o]]))

      (should= {:row 0 :column 0} (sut/generate-best-move [["" :o :o :o]
                                                           [:o :x :x :x]
                                                           [:x :o :x :o]
                                                           [:o :x :x :x]])))
    (it "blocks the opponent from winning"
      (should= {:row 0 :column 0} (sut/generate-best-move [["" :o :o]
                                                           ["" "" :x]
                                                           ["" :x ""]])))

    (it "blocks an opponents fork"
      (should= {:row 0 :column 1} (sut/generate-best-move [[:x "" ""]
                                                           ["" :o ""]
                                                           ["" "" :x]]))

      (should= {:row 3 :column 3} (sut/generate-best-move [[:x :o :o ""]
                                                           ["" :x "" ""]
                                                           ["" "" :x ""]
                                                           ["" "" "" ""]])))

    (it "starts in the corner if it goes first"
      (should= {:row 0 :column 0} (sut/generate-best-move (board/new-board)))))
  (context "on easy mode"

    (it "generates a random move, move is valid"
      (with-redefs [rand-int (stub :rand-int)
                    board/valid-turn? (stub :valid-turn? {:return true})
                    board/set-square (stub :set-square)]
        (sut/generate-random-move (board/new-board))
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
        (should= sut/generate-best-move (sut/random-generation-type))))))