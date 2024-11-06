(ns tic-tac-toe.database)

(def database-path "./database.txt")

(defn remove-console [game]
  (dissoc game :console))

(defn remove-screen [game]
  (dissoc game :screen))

(defn format-game [game]
  (-> game
      remove-console
      remove-screen))

(defn store-game! [game]
  (spit database-path (format-game game)))

(defn clear-save! []
  (spit database-path ""))

(defn read-stored-game []
  (try
  (read-string (slurp database-path))
  (catch Exception e {})))

(defn existing-save? []
  (not (= {} (read-stored-game))))