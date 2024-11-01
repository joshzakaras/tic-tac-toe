(ns tic-tac-toe.terminal.draw
  (:require [tic-tac-toe.core :as core]
            [tic-tac-toe.terminal.ui :as ui]))

(defmulti draw-screen :state)

(defmethod draw-screen :game-won [game]
  (ui/print-win (:board game)))

(defmethod draw-screen :game-tied [game]
  (ui/print-tie))

(defmethod draw-screen :playing [game]
  (when (= :player (:current-turn game))
    (ui/print-board (:board game))
    (ui/print-turn (:board game))))

(defmethod core/draw :terminal [game]
  (draw-screen game))