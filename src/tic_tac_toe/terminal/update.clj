(ns tic-tac-toe.terminal.update
  (:require [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.ui :as ui]
            [tic-tac-toe.game-state-changers :as game-state]))

(defmulti play-turn :current-turn)

(defmethod play-turn :player [game]
  (let [player-move (ui/get-player-move (:board game))
        current-turn (board/get-current-turn (:board game))]
      (assoc game :board (board/set-square player-move current-turn (:board game)))))

(defmethod play-turn :computer [game]
  (assoc game :board (cpu/play-computer-turn (:board game) (:difficulty game))))

(defn maybe-play-turn [game]
  (if (not (= :playing (:state game)))
    game
    (-> game
        play-turn
        game-state/update-current-turn)))

(defmethod core/update :terminal [game]
  (-> game
      game-state/maybe-update-game-state
      maybe-play-turn))