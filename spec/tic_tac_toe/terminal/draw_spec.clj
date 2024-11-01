(ns tic-tac-toe.terminal.draw-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.draw :as sut]
            [tic-tac-toe.terminal.ui :as ui]))

(describe "Terminal Draw"
  (with-stubs)

  (it "prints a win when in the :game-won state"
    (with-redefs [ui/print-win (stub :print-win)]
      (core/draw {:console :terminal :state :game-won :board (board/new-board)})
      (should-have-invoked :print-win)))

  (it "prints a tie when in the :game-tied state"
    (with-redefs [ui/print-tie (stub :print-tie)]
      (core/draw {:console :terminal :state :game-tied :board (board/new-board)})
      (should-have-invoked :print-tie)))

  (it "prints the game board when in the :playing state and it's the users turn"
    (with-redefs [ui/print-board (stub :print-board)
                  ui/print-turn (stub :print-turn)]
      (core/draw {:console :terminal :state :playing :board (board/new-board) :current-turn :player})
      (should-have-invoked :print-board)))

  (it "does not print the board when it's the computers turn"
    (with-redefs [ui/print-board (stub :print-board)
                  ui/print-turn (stub :print-turn)]
      (core/draw {:console :terminal :state :playing :board (board/new-board) :current-turn :computer})
      (should-not-have-invoked :print-board)))

  (it "prints the prompt for the player to enter their turn"
    (with-redefs [ui/print-board (stub :print-board)
                  ui/print-turn (stub :print-turn)]
      (core/draw {:console :terminal :state :playing :board (board/new-board) :current-turn :player})
      (should-have-invoked :print-turn)))

  (it "does not print the turn prompt on the computers turn"
    (with-redefs [ui/print-board (stub :print-board)
                  ui/print-turn (stub :print-turn)]
      (core/draw {:console :terminal :state :playing :board (board/new-board) :current-turn :computer})
      (should-not-have-invoked :print-turn))))