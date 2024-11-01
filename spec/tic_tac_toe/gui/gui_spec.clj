(ns tic-tac-toe.gui.gui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.gui :as sut]
            [tic-tac-toe.gui.db-load-screen :as db-load-screen]
            [tic-tac-toe.gui.game-screen :as game-screen]
            [tic-tac-toe.gui.game-setup-screen :as setup-screen]))



(describe "A Tic Tac Toe GUI using Quil"
  (with-stubs)

  (it "has a setup function that either sets the starting screen to the db
  load screen or the new game setup screen depending on if a game save is found."
    (with-redefs [db/existing-save? (stub :existing-save? {:return true})]
      (should= {:gui-screen :db-load-screen :board nil :game-settings nil} (sut/setup)))
    (with-redefs [db/existing-save? (stub :existing-save? {:return false})]
      (should= {:gui-screen :setup-screen :board nil :game-settings nil} (sut/setup))))

  (it "has a draw function that calls a screens draw function based on the state"
    (with-redefs [db-load-screen/draw-screen (stub :db-load-draw-screen)]
      (sut/draw {:gui-screen :db-load-screen})
      (should-have-invoked :db-load-draw-screen))

    (with-redefs [game-screen/draw-screen (stub :game-draw-screen)]
      (sut/draw {:gui-screen :game-screen})
      (should-have-invoked :game-draw-screen))

    (with-redefs [setup-screen/draw-screen (stub :setup-draw-screen)]
      (sut/draw {:gui-screen :setup-screen})
      (should-have-invoked :setup-draw-screen)))

  (it "has a mouse clicked function that calls a screens mouse clicked function based on the state"
    (with-redefs [db-load-screen/on-mouse-clicked (stub :db-on-mouse-clicked)
                  game-screen/on-mouse-clicked (stub :game-on-mouse-clicked)
                  setup-screen/on-mouse-clicked (stub :setup-on-mouse-clicked)]
      (sut/mouse-clicked {:gui-screen :db-load-screen} :some-event)
      (should-have-invoked :db-on-mouse-clicked))

    (with-redefs [db-load-screen/on-mouse-clicked (stub :db-on-mouse-clicked)
                  game-screen/on-mouse-clicked (stub :game-on-mouse-clicked)
                  setup-screen/on-mouse-clicked (stub :setup-on-mouse-clicked)]
      (sut/mouse-clicked {:gui-screen :game-screen} :some-event)
      (should-have-invoked :game-on-mouse-clicked))

    (with-redefs [db-load-screen/on-mouse-clicked (stub :db-on-mouse-clicked)
                  game-screen/on-mouse-clicked (stub :game-on-mouse-clicked)
                  setup-screen/on-mouse-clicked (stub :setup-on-mouse-clicked)]
      (sut/mouse-clicked {:gui-screen :setup-screen} :some-event)
      (should-have-invoked :setup-on-mouse-clicked)))

  )