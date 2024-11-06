(ns tic-tac-toe.gui.screens.token-select-screen
  (:require [quil.core :as q]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

(def black [0 0 0])
(def light-red [255 47 76])
(def light-blue [173 215 230])

(defn draw-x []
  (q/fill nil)
  (q/stroke-weight 10)
  (q/stroke light-red)
  (q/line 20 50 100 130)
  (q/line 20 130 100 50))

(defn within-x? [x y]
  (mouse-helper/within-rect? [20 50 100 130] x y))

(defn draw-o []
  (q/fill nil)
  (q/stroke-weight 10)
  (q/stroke light-blue)
  (q/ellipse 170 90 80 80))

(defn within-o? [x y]
  (mouse-helper/within-rect? [130 50 80 80] x y))

(defn draw-token-question []
  (q/fill black)
  (q/text-size 20)
  (q/text "Would you like to play as X, or O?" 20 20))

(defmethod screens/draw :token-select [game]
  (q/background 100)
  (draw-o)
  (draw-x)
  (draw-token-question))

(defmethod screens/on-click :token-select [game]
  (let [mouse (:mouse game)]
    (cond
      (within-x? (:x mouse) (:y mouse)) (merge game {:current-turn :player})
      (within-o? (:x mouse) (:y mouse)) (merge game {:current-turn :computer})
      :else game)))

(defmethod screens/update :token-select [game]
  (if (:current-turn game)
    (-> game
        (assoc :screen :active-game)
        (assoc :state :playing))
    game))