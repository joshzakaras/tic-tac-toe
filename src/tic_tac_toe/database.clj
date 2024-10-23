(ns tic-tac-toe.database)

(def database-path "./database.txt")

(defn store-game [board game-settings]
  (spit database-path {:board board :game-settings game-settings}))

(defn clear-save []
  (spit database-path ""))

(defn read-stored-game []
  (try
  (read-string (slurp database-path))
  (catch Exception e "")))

(defn existing-save? []
  (not (= "" (read-stored-game))))