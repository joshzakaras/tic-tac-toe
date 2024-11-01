(ns tic-tac-toe.gui.gui
  [:require [quil.core :as q]
            [quil.middleware :as m]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.db-load-screen :as db-load-screen]
            [tic-tac-toe.gui.game-screen :as game-screen]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.game-setup-screen :as setup-screen]])

(defn setup []
  (if (db/existing-save?)
    {:gui-screen :db-load-screen :board nil :game-settings nil}
    {:gui-screen :setup-screen :board nil :game-settings nil}))

(defn draw [state]
  (case (:gui-screen state)
    (:db-load-screen) (db-load-screen/draw-screen state)
    (:game-screen) (game-screen/draw-screen state)
    (:setup-screen) (setup-screen/draw-screen state)
    :else (q/exit)))

(defn mouse-clicked [state event]
  (case (:gui-screen state)
    (:db-load-screen) (db-load-screen/on-mouse-clicked state event)
    (:game-screen) (game-screen/on-mouse-clicked state event)
    (:setup-screen) (setup-screen/on-mouse-clicked state event)
    :else state))

(defn update [])

(defn start-gui []
  (q/sketch
    :title "Tic Tac Toe"
    :size [600 150]
    :setup  setup
    :update update
    :draw draw
    :mouse-clicked mouse-clicked
    :middleware [m/fun-mode]))

(defmethod core/start-game! :gui []
  (start-gui))