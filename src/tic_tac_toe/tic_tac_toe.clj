(ns tic-tac-toe.tic-tac-toe
  (:require [tic-tac-toe.game-board :as board])
  (:require [tic-tac-toe.terminal-ui.terminal-ui :as terminal])
  (:require [tic-tac-toe.computer-player :as cpu]))

(defn player-turn [board]
  (terminal/print-turn board)
  (let [player-move (terminal/get-player-move board)
        current-turn (board/get-current-turn board)]
    (board/set-square player-move current-turn board)))

(defn play-turn [board {:keys [game-type difficulty player-token]}]
  (if (or (= :versus-player game-type) (= player-token (board/get-current-turn board)))
    (player-turn board)
    (cpu/play-computer-turn board difficulty)))

(defn game-loop [board game-settings]
  (terminal/print-board board)
  (cond
    (board/get-win board) (terminal/print-win board)
    (board/full-board? board) (terminal/print-tie)
    :else (recur (play-turn board game-settings) game-settings)))

(defn tic-tac-toe []
  (terminal/print-new-game-alert)
  (terminal/print-input-format)
  (let [game-type (terminal/get-game-type)
        game-settings {
                       :game-type game-type
                       :difficulty   (when (= :versus-computer game-type)
                                       (terminal/get-difficulty))
                       :player-token (when (= :versus-computer game-type)
                                       (terminal/get-player-token))}]
    (game-loop (board/new-board) game-settings)))

(defn -main []
  (tic-tac-toe))