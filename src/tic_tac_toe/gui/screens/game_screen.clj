(ns tic-tac-toe.gui.screens.game-screen
  (:require [quil.core :as q]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.database :as database]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.game-state-changers :as game-state]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

(def light-red [255 47 76])
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

(defn fit-sketch-to-board [game]
  (let [row (count (first (:board game)))
        column (count (:board game))]
    (q/resize-sketch (* 100 row) (* 100 column))))

(defmethod screens/draw :active-game [game]
  (fit-sketch-to-board game)
  (q/background 100)
  (q/fill nil)
  (q/stroke-weight 10)
  (draw-board-outline (:board game))
  (draw-board-tokens (:board game)))

(defn do-player-turn [game potential-move]
  (-> game
      (assoc :board (board/set-square potential-move (board/get-current-turn (:board game)) (:board game)))
      game-state/update-current-turn))

(defn maybe-do-player-turn [game]
  (let [mouse (:mouse game)
        potential-move (mouse-helper/mouse-to-tile-coords (:x mouse) (:y mouse))]
    (if (board/valid-turn? potential-move (:board game))
      (do-player-turn game potential-move)
      game)))

(defn do-computer-turn [game]
  (-> game
      (assoc :board (cpu/play-computer-turn (:board game) (:difficulty game)))
      game-state/update-current-turn))

(defn maybe-do-computer-turn [game]
  (if (and (= :computer (:current-turn game)) (= :playing (:state game)))
    (do-computer-turn game)
    game))

(defn update-screen [game]
  (assoc game :screen :game-over))

(defn end-game [game]
  (db/clear-save!)
  (update-screen game))

(defn maybe-end-game [game]
  (if (= :playing (:state game))
    game
    (end-game game)))

(defmethod screens/on-click :active-game [game]
  (maybe-do-player-turn game))

(defmethod screens/update :active-game [game]
  (db/store-game! game)
  (-> game
      game-state/maybe-update-game-state
      maybe-do-computer-turn
      maybe-end-game))