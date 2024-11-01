(ns tic-tac-toe.gui.game-screen
  (:require [quil.core :as q]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.database :as database]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]))

(def light-red [255 47 76])
(def black [0 0 0])
(def light-blue [173 215 230])

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

(defn finish-game [board]
  (cond
    (board/get-win board) #_(handle-win board)
    (board/full-board? board) #_(handle-tie))
  (q/exit))

(defn draw-screen [state]
  (q/background 100)
  (q/fill nil)
  (q/stroke-weight 10)
  (draw-board-outline (:board state))
  (draw-board-tokens (:board state)))

(defn computer-turn-ok? [board game-settings]
  (and (= (:game-type game-settings) :versus-computer) (not (board/get-win board)) (not (board/full-board? board))))

(defn try-computer-turn [board game-settings]
  (if (computer-turn-ok? board game-settings)
    (cpu/play-computer-turn board (:difficulty game-settings))
    board))

(defn update-screen [state]
  (if (or (board/get-win (:board state)) (board/full-board? (:board state)))
    (finish-game (:board state))
    state))

(defn on-mouse-clicked [{:keys [board] :as state} event]
  #_(database/store-game! (:board state) (:game-settings state))
  (let [potential-move (mouse-helper/mouse-to-tile-coords (:x event) (:y event))]
    (if (board/valid-turn? potential-move (:board state))
      (assoc state :board (-> (board/set-square potential-move (board/get-current-turn (:board state)) (:board state))
                              (try-computer-turn (:game-settings state))))
      state)))