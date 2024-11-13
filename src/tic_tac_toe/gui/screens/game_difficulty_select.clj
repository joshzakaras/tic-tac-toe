(ns tic-tac-toe.gui.screens.game-difficulty-select
  (:require [quil.core :as q]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

(def black [0 0 0])
(def white [255 255 255])
(def gray [128 128 128])

(defn draw-difficulty-question []
  (q/fill black)
  (q/text-size 20)
  (q/text "Would you like to play Easy, Medium, or Hard?" 20 20))

(defn draw-easy-box []
  (q/fill white)
  (q/rect 10 40 100 50))

(defn within-easy-box? [x y]
  (mouse-helper/within-rect? [10 40 100 50] x y))

(defn within-med-box? [x y]
  (mouse-helper/within-rect? [120 40 100 50] x y))

(defn within-hard-box? [x y]
  (mouse-helper/within-rect? [230 40 100 50] x y))

(defn draw-med-box []
  (q/fill gray)
  (q/rect 120 40 100 50))

(defn draw-hard-box []
  (q/fill black)
  (q/rect 230 40 100 50))

(defmethod screens/draw :difficulty-select [game]
  (q/background 100)
  (draw-easy-box)
  (draw-med-box)
  (draw-hard-box)
  (draw-difficulty-question)
  game)

(defmethod screens/on-click :difficulty-select [game]
  (let [mouse (:mouse game)]
    (cond
      (within-easy-box? (:x mouse) (:y mouse)) (merge game {:difficulty :easy})
      (within-med-box? (:x mouse) (:y mouse)) (merge game {:difficulty :med})
      (within-hard-box? (:x mouse) (:y mouse)) (merge game {:difficulty :hard})
      :else game)))

(defmethod screens/update :difficulty-select [game]
  (if (:difficulty game)
    (assoc game :screen :token-select)
    game))