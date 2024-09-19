(ns tic-tac-toe.game-board)

(defn insert-x-in-row [index row]
  (-> (take index row)
      (concat ["x"])
      (concat (take-last (- 3 (inc index)) row))))

(defn insert-o-in-row [index row]
  (-> (take index row)
      (concat ["o"])
      (concat (take-last (- 3 (inc index)) row))))

(defn replace-row [index board new-row]
  (-> (take index board)
      (concat [new-row])
      (concat (take-last (- 3 (inc index)) board))))

(defn play-x [coords board]
  (let [row (nth board (:row coords))]
    (->> (insert-x-in-row (:column coords) row)
         (replace-row (:row coords) board))))

(defn play-o [coords board]
  (let [row (nth board (:row coords))]
    (->> (insert-o-in-row (:column coords) row)
         (replace-row (:row coords) board))))

(defn new-board []
  [["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"]])