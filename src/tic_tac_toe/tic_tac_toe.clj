(ns tic-tac-toe.tic-tac-toe
  (:require [tic-tac-toe.game-board :refer :all])
  (:require [terminal-ui.terminal-ui :refer :all]))

(defn tic-tac-toe
  ([] (start-game) (tic-tac-toe (new-board)))
  ([board]
   (print-board board)
   (print-turn board)
   (cond
     (not (= "" (get-win board))) (print-win board)
     (full-board? board) (print-tie)
     :else (->
             (get-player-turn board)
             (set-square board (get-current-turn board))
             tic-tac-toe))))

(defn -main []
  (tic-tac-toe))