(ns tic-tac-toe.computer-player
  (:require [tic-tac-toe.game-board :as board]))

(defn format-coords [unformatted-coords]
  {:row (first unformatted-coords) :column (last unformatted-coords)})

(defn generate-unformatted-coords []
  (->> (for [x (range 3)]
         (map #(vector x %) (range 3)))
       flatten
       (partition 2)))

(defn generate-coords []
  (->> (generate-unformatted-coords)
       (map #(format-coords %))))

(defn generate-legal-moves [board]
  (->> (generate-coords)
       (filter #(board/valid-turn? % board))))

(defn generate-moves-from-win [board token]
  (->> (generate-legal-moves board)
       (map #(vector % (board/set-square % token board)))
       (filter #(board/did-player-win? (last %) token))
       first
       first))

(defn generate-winning-move [board]
  (generate-moves-from-win board (board/get-current-turn board)))

(defn generate-blocking-move [board]
  (generate-moves-from-win board (board/get-next-turn board)))

(defn center-move-valid? [board]
  (board/valid-turn? {:row 1 :column 1} board))

(defn generate-opposing-corner-move [board]
  (->> (for [row [0 2]
             column [0 2]]
         (when (= (board/get-next-turn board) (board/get-square {:row row :column column} board))
           {:row (- 2 row) :column (- 2 column)}))
       (filter some?)
       (filter #(board/valid-turn? % board))
       first))

(defn generate-empty-corner-move [board]
  (->> (for [row [0 2]
             column [0 2]]
         {:row row :column column})
       (filter #(board/valid-turn? % board))
       first))

(defn generate-empty-side-move [board]
  (->> (vector {:row 0 :column 1} {:row 1 :column 0} {:row 1 :column 2} {:row 2 :column 1})
       (filter #(board/valid-turn? % board))
       first))

(defn generate-move [board]
  (cond
    (generate-winning-move board) (generate-winning-move board)
    (generate-blocking-move board) (generate-blocking-move board)
    (center-move-valid? board) {:row 1 :column 1}
    (generate-opposing-corner-move board) (generate-opposing-corner-move board)
    (generate-empty-corner-move board) (generate-empty-corner-move board)
    :else (generate-empty-side-move board)))

(defn play-computer-turn [board]
  (board/set-square (generate-move board) (board/get-current-turn board) board))