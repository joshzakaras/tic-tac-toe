(ns tic-tac-toe.computer-player
  (:require [tic-tac-toe.game-board :as board]))

(defn generate-random-coords [board]
  {:row (rand-int (count (first board))) :column (rand-int (count board))})

(defn format-coords [unformatted-coords]
  {:row (first unformatted-coords) :column (last unformatted-coords)})

(defn generate-unformatted-coords [board]
  (->> (for [x (range (count (first board)))]
         (map #(vector x %) (range (count board))))
       flatten
       (partition 2)))

(defn generate-coords [board]
  (->> (generate-unformatted-coords board)
       (map #(format-coords %))))

(defn generate-legal-moves [board]
  (->> (generate-coords board)
       (filter #(board/valid-turn? % board))))


(defn score-board [board token]
  (cond
    (board/is-there-tie? board) 0
    (= token (board/get-win board)) 1
    :else -1))

(defn next-step [step]
  (if (= max step)
    min
    max))

(defn maybe-alpha-beta [step board token]
  (let [alpha-beta (->> (generate-legal-moves board)
                        (map #(board/set-square % (board/get-current-turn board) board))
                        (filter #(board/game-over? %))
                        (map #(score-board % token)))]
    (when (> (count alpha-beta) 0)
      (apply step alpha-beta))))

(def max-depth 3)

(defn mini-max [step board token move depth]
  (let [current-turn (board/get-current-turn board)
        projected-board (board/set-square move current-turn board)]
    (cond
      (board/game-over? projected-board) (/ (score-board projected-board token) depth)
      (maybe-alpha-beta step projected-board token) (/ (maybe-alpha-beta step projected-board token) depth)
      (= max-depth depth) 0
      :else (->> (generate-legal-moves projected-board)
           (map #(mini-max (next-step step) projected-board token % (inc depth)))
           (apply step)))))

(defn score-move [board token move]
  (mini-max max board token move 1))

(defn pair-score-with-move [board token move]
  {:score (score-move board token move) :move move})

(defn generate-best-move [board]
  (cond
    (= (board/new-board (count board) (count board)) board) {:row 0 :column 0}
    (and (board/count-values board (board/get-current-turn board)) (board/valid-turn? {:row 1 :column 1} board)) {:row 1 :column 1}
    :else (->> (generate-legal-moves board)
         (map #(pair-score-with-move board (board/get-current-turn board) %))
         (sort-by :score >)
         first
         :move)))

(defn generate-random-move [board]
  (let [random-coords (generate-random-coords board)]
    (if (board/valid-turn? random-coords board)
      random-coords
      (generate-random-move board))))

(defn random-generation-type []
  (if (= 0 (rand-int 9))
    generate-random-move
    generate-best-move))

(defn play-computer-turn [board difficulty]
  (let [play (cond
               (= :hard difficulty) (generate-best-move board)
               (= :med difficulty) ((random-generation-type) board)
               :else (generate-random-move board))]
    (board/set-square play (board/get-current-turn board) board)))