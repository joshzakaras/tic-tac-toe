(ns tic-tac-toe.game-board)

(defn insert-in-row [index row value]
  (-> (take index row)
      (concat [value])
      (concat (take-last (- (count row) (inc index)) row))))

(defn replace-row [index board new-row]
  (-> (take index board)
      (concat [new-row])
      (concat (take-last (- (count board) (inc index)) board))))

(defn set-square [coords value board]
  (let [row (nth board (:row coords))]
    (->> (insert-in-row (:column coords) row value)
         (replace-row (:row coords) board))))

(defn new-row [count]
  (take count (repeat "")))

(defn new-board
  ([] (new-board 3 3))
  ([row-count column-count]
   (take row-count (repeat (new-row column-count)))))

(defn full-board? [board]
  (->> (flatten board)
       (filter #(= "" %))
       empty?))

(defn get-square [coords board]
  (nth (nth board (:row coords)) (:column coords)))

(defn valid-turn? [coords board]
  (= "" (get-square coords board)))

(defn count-values [coll value]
  (->> (flatten coll)
       (filter #(= value %))
       count))

(defn get-current-turn [board]
  (if (= (count-values board :x) (count-values board :o))
    :x
    :o))

(defn get-next-turn [board]
  (if (not (= (count-values board :x) (count-values board :o)))
    :x
    :o))

(defn get-columns [board]
  (->> (range (count board))
       (map #(take-nth (count board) (drop % (flatten board))))))

(defn get-top-to-bottom-diagonal [board]
  (for [index (range 0 (count board))]
    (get-square {:row index :column index} board)))

(defn get-bottom-to-top-diagonal [board]
  (for [index (range 0 (count board))]
    (get-square {:row index :column (- (- (count board) 1) index)} board)))

(defn get-diagonals [board]
   [(get-top-to-bottom-diagonal board) (get-bottom-to-top-diagonal board)])

(defn get-all-sets [board]
  (concat (get-columns board) (get-diagonals board) board))

(defn all-same? [row value]
  (every? #(= value %) row))

(defn did-player-win? [board value]
  (some #(all-same? % value) (get-all-sets board)))

(defn get-win [board]
  (cond
    (did-player-win? board :x) :x
    (did-player-win? board :o) :o))

(defn is-there-win? [board]
  (or (= :x (get-win board)) (= :o (get-win board))))

(defn is-there-tie? [board]
  (and (full-board? board) (not (is-there-win? board))))

(defn game-over? [board]
  (or (is-there-win? board) (is-there-tie? board)))