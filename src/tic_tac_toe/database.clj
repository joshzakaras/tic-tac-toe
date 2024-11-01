(ns tic-tac-toe.database)

(def database-path "./database.txt")

(defn remove-console [game]
  (dissoc game :console))

(defn store-game! [game]
  (spit database-path (remove-console game)))

(defn clear-save! []
  (spit database-path ""))

(defn read-stored-game []
  (try
  (read-string (slurp database-path))
  (catch Exception e {})))

(defn existing-save? []
  (not (= {} (read-stored-game))))