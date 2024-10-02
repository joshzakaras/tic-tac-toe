(ns tic-tac-toe.game-board)

(defn insert-in-row [index row value]
  (-> (take index row)
      (concat [value])
      (concat (take-last (- 3 (inc index)) row))))

(defn replace-row [index board new-row]
  (-> (take index board)
      (concat [new-row])
      (concat (take-last (- 3 (inc index)) board))))

(defn set-square [coords value board]
  (let [row (nth board (:row coords))]
    (->> (insert-in-row (:column coords) row value)
         (replace-row (:row coords) board))))

(defn new-board []
  [["" "" ""] ["" "" ""] ["" "" ""]])

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
  (->> (range 3)
       (map #(take-nth 3 (drop % (flatten board))))))

(defn get-diagonals [board]
  [[(get-square {:row 0 :column 0} board) (get-square {:row 1 :column 1} board) (get-square {:row 2 :column 2} board)]
   [(get-square {:row 2 :column 0} board) (get-square {:row 1 :column 1} board) (get-square {:row 0 :column 2} board)]])

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