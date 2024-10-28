(ns tic-tac-toe.gui.gui
  [:require [quil.core :as q]
            [quil.middleware :as m]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.database :as database]
            [tic-tac-toe.terminal-ui.terminal-ui :as terminal]])

(def light-red [255 47 76])

(def black [0 0 0])

(def light-blue [173 215 230])

(defn setup [board game-settings]
  {:board board :game-settings game-settings})

(defn draw-board-outline [board]
  (q/stroke 0 0 0)
  (doseq [row (range (count (first board)))
        column (range (count board))]
        (q/rect (* 100 row) (* 100 column) 100 100)))

(defn draw-x-at-location [x y]
  (q/stroke light-red)
  (q/line (+ 10 x) (+ 10 y) (+ 90 x) (+ 90 y))
  (q/line (+ 10 x) (+ 90 y) (+ 90 x) (+ 10 y)))

(defn draw-o-at-location [x y]
  (q/stroke light-blue)
  (q/ellipse (+ 50 x) (+ 50 y) 80 80))

(defn draw-board-tokens [board]
  (doseq [row (range (count (first board)))
          column (range (count board))]
    (let [current-square (board/get-square {:row row :column column} board)]
      (cond
        (= :x current-square) (draw-x-at-location (* 100 column) (* 100 row))
        (= :o current-square) (draw-o-at-location (* 100 column) (* 100 row))))))

(defn draw [state]
  (q/background 100)
  (q/fill nil)
  (q/stroke-weight 10)
  (draw-board-outline (:board state))
  (draw-board-tokens (:board state)))

(defn mouse-to-tile-coords [x y]
  {:row (int (Math/floor (/ y 100))) :column (int (Math/floor (/ x 100)))})

(defn computer-turn-ok? [board game-settings]
  (and (= (:game-type game-settings) :versus-computer) (not (board/get-win board)) (not (board/full-board? board))))

(defn try-computer-turn [board game-settings]
  (if (computer-turn-ok? board game-settings)
    (cpu/play-computer-turn board (:difficulty game-settings))
    board))

(defn mouse-clicked [state event]
  (database/store-game (:board state) (:game-settings state))
  (let [potential-move (mouse-to-tile-coords (:x event) (:y event))]
    (if (board/valid-turn? potential-move (:board state))
      (assoc state :board (-> (board/set-square potential-move (board/get-current-turn (:board state)) (:board state))
                              (try-computer-turn (:game-settings state))))
      state)))

(defn handle-win [board]
  (terminal/print-win board)
  (db/clear-save))

(defn handle-tie []
  (terminal/print-tie)
  (db/clear-save))

(defn finish-game [board]
  (cond
    (board/get-win board) (handle-win board)
    (board/full-board? board) (handle-tie))
  (q/exit))

(defn update [state]
  (if (or (board/get-win (:board state)) (board/full-board? (:board state)))
    (finish-game (:board state))
    state))

(defn start-gui [board game-settings]
  (q/sketch
    :title "Tic Tac Toe"
    :size [(* 100 (count (first board))) (* 100 (count board))]
    :setup  #(setup board game-settings)
    :update update
    :draw draw
    :mouse-clicked mouse-clicked
    :middleware [m/fun-mode]))