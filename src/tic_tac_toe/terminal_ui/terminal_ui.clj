(ns tic-tac-toe.terminal-ui.terminal-ui
  (:require [clojure.string :as str]
            [tic-tac-toe.game-board :as board]))

(defn print-input-format []
  (println "Player Input Format: RC where R is the row index and C is the column index."))

(defn ask-for-game-type []
  (println "Would you like to play against a computer? (y/n)"))

(defn print-new-game-alert []
  (println "Starting a new game of tic tac toe..."))

(defn convert-tile [tile] (if (keyword? tile) (name tile) "☐"))

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
  (println (str "Player " (convert-tile (board/get-current-turn board)) " please input your turn: ")))

(defn print-win [board]
  (printf "Player %s has won the game!\n" (convert-tile (board/get-win board))))

(defn right-size? [input]
  (= 2 (count input)))

(defn in-bounds? [input]
  (->> (map int input)
       (every? #(and (>= % (int \0)) (<= % (int \2))))))

(defn invalid-coordinate-input? [input]
  (not (and
         (right-size? input)
         (in-bounds? input))))

(declare get-player-move)

(defn print-invalid-coordinate-input-alert []
  (println "Invalid input, please reselect a coordinate: "))

(defn handle-invalid-coordinate-input [board]
  (print-invalid-coordinate-input-alert)
  (get-player-move board))

(defn print-invalid-turn-alert []
  (println "Invalid turn, please select different coordinates: "))

(defn handle-invalid-turn [board]
  (print-invalid-turn-alert)
  (get-player-move board))

(defn input-to-coords [input]
  (let [unformatted-coords (map #(- (int %) (int \0)) input)]
    {:row (first unformatted-coords) :column (last unformatted-coords)}))

(defn get-player-move [board]
  (let [input (read-line)]
    (cond
      (invalid-coordinate-input? input) (handle-invalid-coordinate-input board)
      (not (board/valid-turn? (input-to-coords input) board)) (handle-invalid-turn board)
      :else (input-to-coords input))))

(defn get-game-type []
  (ask-for-game-type)
  (let [input (str/lower-case (read-line))]
    (cond
      (= "y" input) :versus-computer
      (= "n" input) :versus-player
      :else (get-game-type))))

(defn print-tie []
  (println "The game results in a tie..."))