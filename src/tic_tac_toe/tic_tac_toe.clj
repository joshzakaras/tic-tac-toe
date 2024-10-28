(ns tic-tac-toe.tic-tac-toe
  (:require [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal-ui.terminal-ui :as terminal]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.gui :as gui]))

(defn player-turn [board]
  (terminal/print-turn board)
  (let [player-move (terminal/get-player-move board)
        current-turn (board/get-current-turn board)]
    (board/set-square player-move current-turn board)))

(defn play-turn [board {:keys [game-type difficulty player-token]}]
  (if (or (= :versus-player game-type) (= player-token (board/get-current-turn board)))
    (player-turn board)
    (cpu/play-computer-turn board difficulty)))

(defn handle-win [board]
  (terminal/print-win board)
  (db/clear-save))

(defn handle-tie []
  (terminal/print-tie)
  (db/clear-save))

(defn game-loop [board game-settings]
  (db/store-game board game-settings)
  (terminal/print-board board)
  (cond
    (board/get-win board) (handle-win board)
    (board/full-board? board) (handle-tie)
    :else (recur (play-turn board game-settings) game-settings)))

(defn play-new-game []
  (terminal/print-new-game-alert)
  (terminal/print-input-format)
  (let [game-type (terminal/get-game-type)
        game-settings {
                       :game-type    game-type
                       :difficulty   (when (= :versus-computer game-type)
                                       (terminal/get-difficulty))
                       :player-token (when (= :versus-computer game-type)
                                       (terminal/get-player-token))}]
    (if (terminal/use-gui?)
      (gui/start-gui (board/new-board) game-settings)
      (game-loop (board/new-board) game-settings))))

(defn play-existing-game []
  (let [game-save (db/read-stored-game)]
    (if (terminal/use-gui?)
      (gui/start-gui (:board game-save) (:game-settings game-save))
      (game-loop (:board game-save) (:game-settings game-save)))))

(defn tic-tac-toe []
  (if (= true (db/existing-save?))
    (if (= true (terminal/load-database?))
      (play-existing-game)
      (play-new-game))
    (play-new-game)))

(defn -main []
  (tic-tac-toe))