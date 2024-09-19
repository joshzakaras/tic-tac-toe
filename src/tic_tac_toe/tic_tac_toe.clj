(ns tic-tac-toe.tic-tac-toe)

(defn insert-in-row [index row value]
  (-> (take index row)
      (concat [value])
      (concat (take-last (- 3 (inc index)) row))))

(defn replace-row [index board new-row]
  (-> (take index board)
      (concat [new-row])
      (concat (take-last (- 3 (inc index)) board))))

(defn set-square [coords board value]
  (let [row (nth board (:row coords))]
    (->> (insert-in-row (:column coords) row value)
         (replace-row (:row coords) board))))

(defn play-x [coords board]
  (set-square coords board :x))

(defn play-o [coords board]
  (set-square coords board :o))

(defn new-board []
  [["" "" ""] ["" "" ""] ["" "" ""]])

(defn count-values [coll value]
  (->> (flatten coll)
       (filter #(= value %))
       count))

(defn full-board? [board]
  (->> (flatten board)
       (filter #(= "" %))
       empty?))

(defn get-square [coords board]
  (nth (nth board (:row coords)) (:column coords)))

(defn valid-turn? [coords board]
  (= "" (get-square coords board)))

(defn check-current-turn [board]
  (if (= 0 (compare (count-values board :x) (count-values board :o)))
    :x
    :o))