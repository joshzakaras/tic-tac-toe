(ns tic-tac-toe.gui.gui
  [:require [quil.core :as q]
            [quil.middleware :as m]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.db-load]
            [tic-tac-toe.gui.screens.active-game]
            [tic-tac-toe.gui.screens.game-type-select]
            [tic-tac-toe.gui.screens.game-difficulty-select]
            [tic-tac-toe.gui.screens.token-select]
            [tic-tac-toe.gui.screens.game-over]
            [tic-tac-toe.gui.screens.board-select]])

(defn set-starting-screen [game]
  (merge game {:screen (if (db/existing-save?)
                         :db-load
                         :board-select)}))

(defmethod core/setup :gui [game]
  (-> game
      (merge {:state :setup})
      set-starting-screen))

(defmethod core/update :gui [game] (screens/update game))

(defmethod core/draw :gui [game]
  (screens/draw game))

(defn on-mouse-click [game event]
  (-> game
      (merge {:mouse event})
      screens/on-click
      (dissoc :mouse)))

(defmethod core/start-game! :gui [game]
  (q/sketch
    :title "Tic Tac Toe"
    :size [600 150]
    :setup #(core/setup game)
    :update core/update
    :draw core/draw
    :mouse-clicked on-mouse-click
    :middleware [m/fun-mode]))