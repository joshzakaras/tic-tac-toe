(ns terminal-ui.terminal-ui
  (:require [clojure.string :as str]
            [tic-tac-toe.game-board :refer :all]))

(defn print-input-format []
  (println "Player Input Format: RC where R is the row index and C is the column index."))

(defn start-game []
  (println "Starting a new game of tic tac toe...")
  (print-input-format))

(defn convert-tile [tile]
  (cond
    (= :x tile) "x"
    (= :o tile) "o"
    :other "☐"))

(defn board-rows-to-string [board]
  (->> (flatten board)
       (map convert-tile)
       (partition 3)
       (map #(str/join " " %))))

(defn add-coordinates-to-rows [board]
  (map #(str % " " (nth board %)) (range 3)))

(defn board-to-string [board]
  (->> (str/join "\n" board)))

(defn print-board [board]
  (println (->> (board-rows-to-string board)
                add-coordinates-to-rows
                board-to-string
                (str "  0 1 2\n"))))

(defn print-turn [board]
  (println (str "Player " (convert-tile (get-current-turn board)) " please input your turn: ")))
(defn print-win [board]
  (printf "Player %s has won the game!\n" (convert-tile (get-win board))))

(defn get-player-turn [board]
    (let [turn (read-line)
          coords (->> (str/split turn #"")
                      (map #(Integer/valueOf %)))
          potential-turn {:row (first coords) :column (last coords)}]
      (if (valid-turn? potential-turn board)
        potential-turn
        (do
          (println "Your turn is invalid, please select again: ")
          (get-player-turn board)))))

(defn print-tie []
  (println "The game results in a tie..."))