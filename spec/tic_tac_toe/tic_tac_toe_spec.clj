(ns tic-tac-toe.tic-tac-toe-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.terminal-ui.terminal-ui :as terminal]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.tic-tac-toe :as sut]
            [tic-tac-toe.computer-player :as cpu]))

(describe "Tic Tac Toe"
  (with-stubs)

  (it "starts a new game"
    (with-redefs [terminal/print-new-game-alert (stub :new-game-alert)
                  terminal/print-input-format (stub :input-format)
                  terminal/get-game-type (stub :get-game-type)
                  sut/game-loop (stub :game-loop)]
      (sut/tic-tac-toe)
      (should-have-invoked :new-game-alert)
      (should-have-invoked :input-format)))

  (it "asks the player if they want to play against a computer"
    (with-redefs [terminal/print-new-game-alert (stub :new-game-alert)
                  terminal/print-input-format (stub :input-format)
                  terminal/get-game-type (stub :get-game-type)
                  sut/game-loop (stub :game-loop)]
      (sut/tic-tac-toe)
      (should-have-invoked :get-game-type)))

  (it "starts the core game loop"
    (with-redefs [sut/game-loop (stub :game-loop)
                  terminal/get-game-type (stub :get-game-type)
                  println (stub :print-ln)]
      (sut/tic-tac-toe)
      (should-have-invoked :game-loop {:with [(board/new-board) nil]})))

  (context "Core Game Loop"

    (it "prints the game board"
      (with-redefs [terminal/print-board (stub :print-board)
                    sut/player-turn (stub :turn {:return [[:x :o :x]
                                                   [:x :o :o]
                                                   [:o :x :x]]})
                    println (stub :println)]
        (sut/game-loop (board/new-board) nil)
        (should-have-invoked :print-board {:with [(board/new-board)]})))

    (it "ends the game if there is a tie"
      (with-redefs [terminal/print-tie (stub :print-tie)
                    sut/player-turn (stub :turn {:return [[:x :o :x]
                                                   [:x :o :o]
                                                   [:o :x :x]]})
                    println (stub :println)]
        (sut/game-loop [[:x :o :x]
                        [:x :o :o]
                        [:o :x :x]] nil)
        (should-have-invoked :print-tie)))

    (it "ends the game if there is a win"
      (with-redefs [terminal/print-win (stub :print-win)
                    sut/player-turn (stub :turn {:return [[:x :o :x]
                                                   [:x :o :o]
                                                   [:o :x :x]]})
                    println (stub :println)]
        (sut/game-loop [[:x :x :x]
                        [:x :x :x]
                        [:x :x :x]] nil)
        (should-have-invoked :print-win {:with [[[:x :x :x]
                                                 [:x :x :x]
                                                 [:x :x :x]]]})))

    (it "has the player complete their turn"
      (with-redefs [sut/player-turn (stub :turn {:return [[:x :o :x]
                                                   [:x :o :o]
                                                   [:o :x :x]]})
                    println (stub :println)]
        (sut/game-loop (board/new-board) nil)
        (should-have-invoked :turn)))

    (it "has the computer play o's turn if the computer mode is selected"
      (with-redefs [cpu/play-computer-turn (stub :play-computer-turn {:return [[:x :o :x]
                                                                     [:x :o :o]
                                                                     [:o :x :x]]})
                    sut/player-turn (stub :turn {:return [[:x :o :x]
                                                          [:x :o :o]
                                                          [:o :x :x]]})
                    println (stub :println)]
        (sut/game-loop [["" "" ""]
                        ["" :x ""]
                        ["" "" ""]] :versus-computer)
        (should-have-invoked :play-computer-turn {:with [[["" "" ""]
                                                     ["" :x ""]
                                                     ["" "" ""]]]})))

    (it "game-loop where x wins"
      (with-redefs [terminal/print-board (stub :terminal/print-board)
                    board/get-win (stub :board/get-win {:return :x})
                    terminal/print-win (stub :terminal/print-win)]
        (sut/game-loop :some-board :some-game-type)
        (should-have-invoked :terminal/print-board)
        (should-have-invoked :terminal/print-win)))

    (it "game-loop where with full board, nobody wins"
      (with-redefs [terminal/print-board (stub :terminal/print-board)
                    board/get-win (stub :board/get-win {:return nil})
                    board/full-board? (stub :board/full-board? {:return true})
                    terminal/print-win (stub :terminal/print-win)
                    terminal/print-tie (stub :terminal/print-tie)]
        (sut/game-loop :some-board :some-game-type)
        (should-have-invoked :terminal/print-board)
        (should-not-have-invoked :terminal/print-win)
        (should-have-invoked :terminal/print-tie)))
    )

  (context "A player turn"
    (it "asks for the players move"
      (with-redefs [terminal/print-turn (stub :print-turn)
                    terminal/get-player-move (stub :get-player-move {:return {:row 0 :column 0}})
                    board/set-square (stub :set-square)]
        (sut/player-turn (board/new-board))
        (should-have-invoked :print-turn)))

    (it "gets the players move"
      (with-redefs [terminal/print-turn (stub :print-turn)
                    terminal/get-player-move (stub :get-player-move {:return {:row 0 :column 0}})
                    board/set-square (stub :set-square)]
        (sut/player-turn (board/new-board))
        (should-have-invoked :get-player-move)))

    (it "updates the game board with the move"
      (with-redefs [terminal/print-turn (stub :print-turn)
                    terminal/get-player-move (stub :get-player-move {:return {:row 0 :column 0}})
                    board/set-square (stub :set-square)]
        (sut/player-turn (board/new-board))
        (should-have-invoked :set-square {:with [{:row 0 :column 0} (board/get-current-turn (board/new-board)) (board/new-board)]}))))
  )