(ns tic-tac-toe.gui.screen-core)

(defmulti draw :screen)

(defmulti on-click :screen)

(defmulti update :screen)