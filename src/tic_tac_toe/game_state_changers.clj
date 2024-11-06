(ns tic-tac-toe.game-state-changers
  (:require [tic-tac-toe.game-board :as board]))

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