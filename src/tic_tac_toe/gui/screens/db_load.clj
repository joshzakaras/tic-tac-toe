(ns tic-tac-toe.gui.screens.db-load
  (:require [quil.core :as q]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]))

(def confirmation-green [35 101 51])
(def denial-red [178 22 22])
(def black [0 0 0])

(defn draw-db-load-question []
  (q/fill black)
  (q/text-size 20)
  (q/text "The program has found a save file, would you like to load it?" 20 20))

(defn draw-confirmation-box []
  (q/fill confirmation-green)
  (q/rect 10 40 100 50))

(defn draw-denial-box []
  (q/fill denial-red)
  (q/rect 120 40 100 50))

(defmethod screens/draw :db-load [game]
  (q/background 100)
  (draw-confirmation-box)
  (draw-denial-box)
  (draw-db-load-question)
  game)

(defn within-confirmation-box? [x y]
  (mouse-helper/within-rect? [10 40 100 50] x y))

(defn within-denial-box? [x y]
  (mouse-helper/within-rect? [120 40 100 50] x y))

(defmethod screens/on-click :db-load [game]
  (let [mouse (:mouse game)]
    (cond
      (within-confirmation-box? (:x mouse) (:y mouse))
      (-> game
          (merge (db/read-stored-game))
          (assoc :state :playing)
          (assoc :screen :active-game))
      (within-denial-box? (:x mouse) (:y mouse))
      (-> game
          (assoc :screen :board-select))
      :else game)))

(defmethod screens/update :db-load [game] game)