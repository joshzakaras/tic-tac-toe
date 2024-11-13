(ns tic-tac-toe.gui.screens.game-type-select
  (:require [quil.core :as q]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

(def confirmation-green [35 101 51])
(def black [0 0 0])
(def denial-red [178 22 22])

(defn draw-game-type-question []
  (q/fill black)
  (q/text-size 20)
  (q/text "Would you like to play against a computer?" 20 20))

(defn draw-confirmation-box []
  (q/fill confirmation-green)
  (q/rect 10 40 100 50))

(defn draw-denial-box []
  (q/fill denial-red)
  (q/rect 120 40 100 50))

(defn within-confirmation-box? [x y]
  (mouse-helper/within-rect? [10 40 100 50] x y))

(defn within-denial-box? [x y]
  (mouse-helper/within-rect? [120 40 100 50] x y))

(defmethod screens/draw :game-type-select [game]
  (q/background 100)
  (draw-game-type-question)
  (draw-confirmation-box)
  (draw-denial-box)
  game)

(defn do-confirmed-selection [game]
  (-> game
      (merge {:game-type :versus-computer})
      (assoc :screen :difficulty-select)))

(defn do-denied-selection [game]
  (-> game
      (merge {:game-type :versus-player})
      (assoc :screen :active-game)
      (assoc :state :playing)
      (merge {:current-turn :player})))

(defmethod screens/on-click :game-type-select [game]
  (let [mouse (:mouse game)]
    (cond
      (within-confirmation-box? (:x mouse) (:y mouse)) (do-confirmed-selection game)
      (within-denial-box? (:x mouse) (:y mouse)) (do-denied-selection game)
      :else game)))

(defmethod screens/update :game-type-select [game] game)