(ns tic-tac-toe.core)

(defmulti start-game! :console)

(defmulti setup :console)

(defmulti update :console)

(defmulti draw :console)

(defmulti update-current-turn :current-turn)