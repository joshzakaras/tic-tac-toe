(ns tic-tac-toe.gui.screens.board-select
  (:require [quil.core :as q]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

(def black [0 0 0])
(def white [255 255 255])
(def gray [128 128 128])

(defn draw-board-size-question []
  (q/fill black)
  (q/text-size 20)
  (q/text "Would you like to play on a 3x3 or 4x4 board?" 20 20))

(defn draw-3x3-box []
  (q/fill gray)
  (q/text-size 20)
  (q/rect 10 40 100 50)
  (q/fill black)
  (q/text "3x3" 40 75))

(defn draw-4x4-box []
  (q/fill gray)
  (q/text-size 20)
  (q/rect 120 40 100 50)
  (q/fill black)
  (q/text "4x4" 150 75))

(defmethod screens/draw :board-select [game]
  (q/background 100)
  (draw-board-size-question)
  (draw-3x3-box)
  (draw-4x4-box))

(defn within-3x3-box? [x y]
  (mouse-helper/within-rect? [10 40 100 50] x y))

(defn within-4x4-box? [x y]
  (mouse-helper/within-rect? [120 40 100 50] x y))

(defmethod screens/on-click :board-select [game]
  (let [mouse (:mouse game)]
    (cond
      (within-3x3-box? (:x mouse) (:y mouse)) (merge game {:board (board/new-board 3 3)})
      (within-4x4-box? (:x mouse) (:y mouse)) (merge game {:board (board/new-board 4 4)})
      :else game)))

(defmethod screens/update :board-select [game]
  (if (:board game)
    (assoc game :screen :game-type-select)
    game))