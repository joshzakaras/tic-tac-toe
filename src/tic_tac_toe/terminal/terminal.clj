(ns tic-tac-toe.terminal.terminal
  (:require [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.terminal.setup]
            [tic-tac-toe.terminal.draw]
            [tic-tac-toe.terminal.update]))

(defmulti run-game :state)

(defmethod run-game :playing [game]
  (db/store-game! game)
  (core/draw game)
  (run-game (core/update game)))

(defmethod run-game :default [game]
  game)

(defn end-game [game]
  (core/draw game)
  (db/clear-save!)
  game)

(defmethod core/start-game! :terminal [game]
  (-> game
      (merge {:state :setup})
      core/setup
      (assoc :state :playing)
      run-game
      end-game))