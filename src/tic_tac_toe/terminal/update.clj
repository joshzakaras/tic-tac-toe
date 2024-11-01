(ns tic-tac-toe.terminal.update
  (:require [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.ui :as ui]))

(defmulti play-turn :current-turn)

(defmethod play-turn :player [game]
  (let [player-move (ui/get-player-move (:board game))
        current-turn (board/get-current-turn (:board game))]
      (assoc game :board (board/set-square player-move current-turn (:board game)))))

(defmethod play-turn :computer [game]
  (assoc game :board (cpu/play-computer-turn (:board game) (:difficulty game))))

(defn get-current-game-state [game]
  (cond
    (board/is-there-win? (:board game)) :game-won
    (board/is-there-tie? (:board game)) :game-tied
    :else (:state game)))

(defn maybe-update-game-state [game]
  (assoc game :state (get-current-game-state game)))

(defmulti update-current-turn :current-turn)

(defmethod update-current-turn :computer [game]
  (assoc game :current-turn :player))

(defmethod update-current-turn :player [game]
  (if (= :versus-computer (:game-type game))
    (assoc game :current-turn :computer)
    game))

(defn maybe-play-turn [game]
  (if (not (= :playing (:state game)))
    game
    (-> game
        play-turn
        update-current-turn)))

(defmethod core/update :terminal [game]
  (-> game
      maybe-update-game-state
      maybe-play-turn))