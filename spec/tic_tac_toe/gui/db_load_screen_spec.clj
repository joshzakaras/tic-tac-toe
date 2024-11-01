(ns tic-tac-toe.gui.db-load-screen-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.db-load-screen :as sut]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]))

(def confirmation-green [35 101 51])
(def black [0 0 0])

(def denial-red [178 22 22])

(describe "GUI Functions for the database load screen"
  (with-stubs)

  (it "draws text on the screen that asks if the user wants to load a saved game"
    (with-redefs [q/fill (stub :fill)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-db-load-question)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :text {:with ["The program has found a save file, would you like to load it?" 20 20]})))

  (it "draws a green box on the screen to say yes to the question"
    (with-redefs [q/rect (stub :rect)
                  q/stroke (stub :stroke)
                  q/fill (stub :fill)]
      (sut/draw-confirmation-box)
      (should-have-invoked :fill {:with [confirmation-green]})
      (should-have-invoked :rect {:with [10 40 100 50]})))

  (it "draws a red box on the screen to say no to the question"
    (with-redefs [q/rect (stub :rect)
                  q/stroke (stub :stroke)
                  q/fill (stub :fill)]
      (sut/draw-denial-box)
      (should-have-invoked :fill {:with [denial-red]})
      (should-have-invoked :rect {:with [120 40 100 50]})))

  (it "runs all draw calls when asked to draw the screen"
    (with-redefs [sut/draw-denial-box (stub :denial-box)
                  sut/draw-confirmation-box (stub :confirmation-box)
                  sut/draw-db-load-question (stub :db-load-question)]
      (sut/draw-screen :some-state)
      (should-have-invoked :denial-box)
      (should-have-invoked :confirmation-box)
      (should-have-invoked :db-load-question)))

  (it "checks if the mouse is within the bounds of the confirmation box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-confirmation-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[10 40 60 50] :some-x :some-y]})))

  (it "checks if the mouse is within the bounds of the denial box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-denial-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[200 40 60 50] :some-x :some-y]})))

  (it "loads or skips the saved game depending on option selected when mouse clicked"
    (with-redefs [sut/load-new-game-into-state (stub :new-game)
                  sut/load-save-game-into-state (stub :save-game)
                  sut/within-confirmation-box? (stub :in-confirm? {:return true})
                  sut/within-denial-box? (stub :in-denial? {:return false})]
      (sut/on-mouse-clicked :some-state :some-event)
      (should-have-invoked :save-game {:with [:some-state]}))

    (with-redefs [sut/load-new-game-into-state (stub :new-game)
                  sut/load-save-game-into-state (stub :save-game)
                  sut/within-confirmation-box? (stub :in-confirm? {:return false})
                  sut/within-denial-box? (stub :in-denial? {:return true})]
      (sut/on-mouse-clicked :some-state :some-event)
      (should-have-invoked :new-game {:with [:some-state]})))

  (it "can load a game from a save and start it"
    (with-redefs [assoc (stub :assoc)
                  db/read-stored-game (stub :read-stored-game {:return {:board :some-board :game-settings :some-settings}})]
      (sut/load-save-game-into-state :some-state)
      (should-have-invoked :assoc {:with [:some-state :board :some-board]})
      (should-have-invoked :assoc {:with [:some-state :game-settings :some-settings]})
      (should-have-invoked :assoc {:with [:some-state :gui-screen :game-screen]})))

  (it "can start a new game"
    (with-redefs [assoc (stub :assoc)]
      (sut/load-new-game-into-state :some-state)
      (should-have-invoked :assoc {:with [:some-state :gui-screen :game-setup]})))
)