(ns tic-tac-toe.terminal.ui
  (:require [clojure.string :as str]
            [tic-tac-toe.game-board :as board]))

(defn print-input-format []
  (println "Player Input Format: RC where R is the row index and C is the column index."))

(defn ask-for-game-type []
  (println "Would you like to play against a computer? (y/n)"))

(defn ask-for-difficulty []
  (println "What difficulty would you like to set the computer at? (easy/med/hard)"))

(defn ask-for-player-token []
  (println "Would you like to play as X, or O? (x/o)"))

(defn print-new-game-alert []
  (println "Starting a new game of tic tac toe..."))

(defn ask-for-database-load []
  (println "The program has found a save file, would you like to load it? (y/n)"))

(defn ask-for-gui []
  (println "Would you like to play with a GUI? (y/n)"))

(defn ask-for-board-size []
  (println "Would you like to play on a 3x3 or 4x4 board? (3/4)"))

(defn print-tie []
  (println "The game results in a tie..."))

(defn convert-tile [tile] (if (keyword? tile) (name tile) "☐"))

(defn board-rows-to-string [board]
  (->> (flatten board)
       (map convert-tile)
       (partition (count board))
       (map #(str/join " " %))))

(defn add-coordinates-to-rows [board]
  (map #(str % " " (nth board %)) (range (count board))))

(defn board-to-string [board]
  (->> (str/join "\n" board)))

(defn add-coordinates-to-columns [board]
  (str "  "(->> (range 0 (count (first board)))
            (str/join " ")) "\n"))

(defn print-board [board]
  (println (str (add-coordinates-to-columns board)
                (->> (board-rows-to-string board)
                     add-coordinates-to-rows
                     board-to-string))))

(defn print-turn [board]
  (println (str "Player " (convert-tile (board/get-current-turn board)) " please input your turn: ")))

(defn print-win [board]
  (printf "Player %s has won the game!\n" (convert-tile (board/get-win board))))

(defn right-size? [input]
  (= 2 (count input)))

(defn in-bounds? [input board]
  (->> (map int input)
       (every? #(and (>= % (int \0)) (<= % (+ 47 (count board)))))))

(defn invalid-coordinate-input? [input board]
  (not (and
         (right-size? input)
         (in-bounds? input board))))

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
      (invalid-coordinate-input? input board) (handle-invalid-coordinate-input board)
      (not (board/valid-turn? (input-to-coords input) board)) (handle-invalid-turn board)
      :else (input-to-coords input))))

(defn get-formatted-user-input []
  (str/lower-case (read-line)))

(defn get-game-type []
  (ask-for-game-type)
  (let [input (get-formatted-user-input)]
    (cond
      (= "y" input) :versus-computer
      (= "n" input) :versus-player
      :else (get-game-type))))

(defn get-difficulty []
  (ask-for-difficulty)
  (let [input (get-formatted-user-input)]
    (cond
      (= "easy" input) :easy
      (= "med" input) :med
      (= "hard" input) :hard
      :else (get-difficulty))))

(defn get-player-token []
  (ask-for-player-token)
  (let [input (get-formatted-user-input)]
    (cond
      (= "o" input) :o
      (= "x" input) :x
      :else (get-player-token))))

(defn load-database? []
  (ask-for-database-load)
  (let [input (get-formatted-user-input)]
    (cond
      (= "y" input) true
      (= "n" input) false
      :else (load-database?))))

(defn use-gui? []
  (ask-for-gui)
  (let [input (get-formatted-user-input)]
    (cond
      (= "y" input) true
      (= "n" input) false
      :else (use-gui?))))

(defn get-board-size []
  (ask-for-board-size)
  (let [input (get-formatted-user-input)]
    (cond
      (= "3" input) (board/new-board 3 3)
      (= "4" input) (board/new-board 4 4)
      :else (get-board-size))))

