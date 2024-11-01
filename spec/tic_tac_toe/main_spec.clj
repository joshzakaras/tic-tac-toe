(ns tic-tac-toe.main-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.main :as sut]))

(describe "Starting the program"
  (with-stubs)

  (it "starts the game in the terminal as the default"
    (with-redefs [core/start-game! (stub :play-game)]
      (sut/-main)
      (should-have-invoked :play-game {:with [{:console :terminal}]})))

  (it "starts the game in the gui when specified"
    (with-redefs [core/start-game! (stub :play-game)]
      (sut/-main ":gui")
      (should-have-invoked :play-game {:with [{:console :gui}]}))))