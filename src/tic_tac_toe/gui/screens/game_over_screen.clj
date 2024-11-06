(ns tic-tac-toe.gui.screens.game-over-screen
  (:require [quil.core :as q]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.screen-core :as screens]))

(def black [0 0 0])

(defmulti draw-result :result)

(defmethod draw-result :o [state]
  (q/text "O has won the game!" 20 20))

(defmethod draw-result :x [state]
  (q/text "X has won the game!" 20 20))

(defmethod draw-result :tie [state]
  (q/text "Game results in a tie..." 20 20))

(defmethod screens/draw :game-over [game]
  (q/fill black)
  (q/background 100)
  (q/text-size 20)
  (if (= :game-tied (:state game))
    (draw-result {:result :tie})
    (draw-result {:result (board/get-win (:board game))})))

(defmethod screens/update :game-over [game] game)