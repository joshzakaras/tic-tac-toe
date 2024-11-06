(ns tic-tac-toe.gui.gui-spec
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [speclj.core :refer :all]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.db-load-screen :as db-load-screen]
            [tic-tac-toe.gui.gui :as sut]))

(describe "A Tic Tac Toe GUI using Quil"
  (with-stubs)

  (it "starts a quil sketch to handle the GUI process"
    (with-redefs [q/sketch (stub :sketch)]
      (core/start-game! {:console :gui})
      (should-have-invoked :sketch)))

  (context "Setup Method"
    (it "sets the state of the game to setup"
      (with-redefs [db/existing-save? (stub :existing-save? {:return true})]
        (should= :setup (:state (core/setup {:console :gui})))))

    (it "sets the default screen to load db if a database is found"
      (with-redefs [db/existing-save? (stub :existing-save {:return true})]
        (should= :db-load (:screen (core/setup {:console :gui})))))

    (it "sets the starting screen to new-game if a database is not found"
      (with-redefs [db/existing-save? (stub :existing-save? {:return false})]
        (should= :game-type-select (:screen (core/setup {:console :gui}))))))

  (context "Draw Method"
    (it "draws the currently selected screen"
      (with-redefs [screens/draw (stub :screen-draw)]
        (core/draw {:console :gui :screen :some-screen})
        (should-have-invoked :screen-draw))))

  (context "Mouse Click Event"

    (it "calls the screens on click event with the mouse values attached to the game"
      (with-redefs [screens/on-click (stub :on-click {:return {:some-new-keys :some-new-values}})]
        (sut/on-mouse-click {:some-game-keys :some-game-values} {:some-mouse-keys :some-mouse-values})
        (should-have-invoked :on-click {:with [{:some-game-keys :some-game-values :mouse {:some-mouse-keys :some-mouse-values}}]})))

    (it "disposes the mouse event when returning the game state"
      (with-redefs [screens/on-click (stub :on-click {:return {:some-new-keys :some-new-values :mouse :some-mouse-event}})]
        (should-not (:mouse (sut/on-mouse-click {:some-game-keys :some-game-values} {:some-mouse-keys :some-mouse-values})))))))