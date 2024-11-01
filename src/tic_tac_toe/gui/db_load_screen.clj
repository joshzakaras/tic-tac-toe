(ns tic-tac-toe.gui.db-load-screen
  (:require [quil.core :as q]
            [tic-tac-toe.database :as db]
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

(defn draw-screen [state]
  (draw-confirmation-box)
  (draw-denial-box)
  (draw-db-load-question)
  state)

(defn within-confirmation-box? [x y]
  (mouse-helper/within-rect? [10 40 60 50] x y))

(defn within-denial-box? [x y]
  (mouse-helper/within-rect? [200 40 60 50] x y))

(defn load-save-game-into-state [state]
  (let [game-save (db/read-stored-game)]
    (assoc state :board (:board game-save))
    (assoc state :game-settings (:game-settings game-save))
    (assoc state :gui-screen :game-screen)))

(defn load-new-game-into-state [state]
  (assoc state :gui-screen :game-setup))

(defn on-mouse-clicked [state event]
  (cond
    (within-confirmation-box? (:x event) (:y event)) (load-save-game-into-state state)
    (within-denial-box? (:x event) (:y event)) (load-new-game-into-state state)
    :else state))