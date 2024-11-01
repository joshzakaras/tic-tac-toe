(ns tic-tac-toe.main
  (:require
    [tic-tac-toe.core :as core]
    [tic-tac-toe.gui.gui]
    [tic-tac-toe.terminal.terminal]))

(defn -main [& args]
  (let [terminal-type (when args (read-string (first args)))]
    (core/start-game! {:console (or terminal-type :terminal)})))