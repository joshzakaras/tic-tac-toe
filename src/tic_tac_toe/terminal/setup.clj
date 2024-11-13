(ns tic-tac-toe.terminal.setup
  (:require [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.ui :as terminal]))

(defmulti load-type-specific-settings :game-type)

(defn create-new-game [game]
  (-> game
      (merge {:board (terminal/get-board-size)})
      (merge {:game-type (terminal/get-game-type)})
      load-type-specific-settings))

(defn set-starting-turn [game]
  (merge game {:current-turn (if (= :x (:player-token game))
                               :player
                               :computer)}))

(defmethod load-type-specific-settings :versus-computer [game]
  (-> game
      (merge {:difficulty (terminal/get-difficulty)})
      (merge {:player-token (terminal/get-player-token)})
      set-starting-turn))

(defmethod load-type-specific-settings :default [game]
  (merge game {:current-turn :player}))

(defn handle-save [game]
  (if (terminal/load-database?)
    (merge game (db/read-stored-game))
    (create-new-game game)))

(defmethod core/setup :terminal [game]
  (if (db/existing-save?)
    (handle-save game)
    (create-new-game game)))