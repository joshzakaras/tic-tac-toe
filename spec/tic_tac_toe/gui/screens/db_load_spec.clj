(ns tic-tac-toe.gui.screens.db-load-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.gui.gui :as gui]
            [tic-tac-toe.gui.screens.db-load :as sut]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]))

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
                  sut/draw-db-load-question (stub :db-load-question)
                  q/background (stub :background)]
      (screens/draw {:screen :db-load})
      (should-have-invoked :background)
      (should-have-invoked :denial-box)
      (should-have-invoked :confirmation-box)
      (should-have-invoked :db-load-question)))

  (it "checks if the mouse is within the bounds of the confirmation box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-confirmation-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[10 40 100 50] :some-x :some-y]})))

  (it "checks if the mouse is within the bounds of the denial box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-denial-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[120 40 100 50] :some-x :some-y]})))

  (it "Loads the stored game when accept button is clicked on the load-db screen"
    (with-redefs [db/read-stored-game (stub :read-stored-game {:return {:some-game-keys :some-game-values}})]
      (should= :some-game-values (:some-game-keys (screens/on-click {:screen :db-load :state :setup :mouse {:x 15 :y 45}})))))

  (it "Sets the state to playing when the accept button is clicked on the load-db screen"
    (with-redefs [db/read-stored-game (stub :read-stored-game {:return {:some-game-keys :some-game-values}})]
      (should= :playing (:state (screens/on-click {:screen :db-load :state :setup :mouse {:x 15 :y 45}})))))

  (it "Sets the screen to the game screen when the accept button is clicked on the db load screen"
    (with-redefs [db/read-stored-game (stub :read-stored-game {:return {:some-game-keys :some-game-values}})]
      (should= :active-game (:screen (screens/on-click {:screen :db-load :state :setup :mouse {:x 15 :y 45}})))))

  (it "Does not load the stored game when the accept button is not clicked on the load-db screen"
    (with-redefs [db/read-stored-game (stub :read-stored-game {:return {:some-game-keys :some-game-values}})]
      (screens/on-click {:screen :db-load :state :setup :mouse {:x 0 :y 0}})
      (should-not-have-invoked :read-stored-game)))

  (it "Sets the screen to new game when the decline button is clicked on the load-db screen"
    (with-redefs [db/read-stored-game (stub :read-stored-game {:return {:some-game-keys :some-game-values}})]
      (should= :board-select (:screen (screens/on-click {:screen :db-load :state :setup :mouse {:x 125 :y 45}}))))))