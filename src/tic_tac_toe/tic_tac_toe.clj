(ns tic-tac-toe.tic-tac-toe
  (:require [tic-tac-toe.game-board :as board])
  (:require [tic-tac-toe.terminal-ui.terminal-ui :as terminal])
  (:require [tic-tac-toe.computer-player :as cpu]))

(defn player-turn [board]
  (terminal/print-turn board)
  (let [player-move (terminal/get-player-move board)
        current-turn (board/get-current-turn board)]
    (board/set-square player-move current-turn board)))

(defn current-turn [board game-type]
  (if (or (= :versus-player game-type) (= :x (board/get-current-turn board)))
    (player-turn board)
    (cpu/play-computer-turn board)))

(defn game-loop [board game-type]
  (terminal/print-board board)
  (cond
    (board/get-win board) (terminal/print-win board)
    (board/full-board? board) (terminal/print-tie)
    :else (recur (current-turn board game-type) game-type)))

(defn tic-tac-toe []
  (terminal/print-new-game-alert)
  (terminal/print-input-format)
  (game-loop (board/new-board) (terminal/get-game-type)))

(defn -main []
  (tic-tac-toe))