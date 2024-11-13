(ns tic-tac-toe.gui.screens.board-select-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.board-select :as sut]))

(def black [0 0 0])
(def white [255 255 255])
(def gray [128 128 128])

(describe "Board Select Screen"
  (with-stubs)

  (it "draws text on the screen that asks if the user wants a 3x3 or 4x4 board"
    (with-redefs [q/fill (stub :fill)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-board-size-question)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :text {:with ["Would you like to play on a 3x3 or 4x4 board?" 20 20]})))

  (it "draws the 3x3 board select box"
    (with-redefs [q/fill (stub :fill)
                  q/rect (stub :rect)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-3x3-box)
      (should-have-invoked :fill {:with [gray]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :rect {:with [10 40 100 50]})
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text {:with ["3x3" 40 75]})))

  (it "draws the 4x4 board select box"
    (with-redefs [q/fill (stub :fill)
                  q/rect (stub :rect)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-4x4-box)
      (should-have-invoked :fill {:with [gray]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :rect {:with [120 40 100 50]})
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text {:with ["4x4" 150 75]})))

  (it "checks if the mouse is within the bounds of the 3x3 box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-3x3-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[10 40 100 50] :some-x :some-y]})))

  (it "checks if the mouse is within the bounds of the 4x4 box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-4x4-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[120 40 100 50] :some-x :some-y]})))

  (it "creates an empty 3x3 board when 3x3 is selected"
    (should= (board/new-board 3 3) (:board (screens/on-click {:screen :board-select :mouse {:x 12 :y 42}}))))

  (it "creates an empty 4x4 board when 4x4 is selected"
    (should= (board/new-board 4 4) (:board (screens/on-click {:screen :board-select :mouse {:x 122 :y 42}}))))

  (it "doesn't do anything when neither box is selected"
    (should= {:screen :board-select :mouse {:x 0 :y 0}} (screens/on-click {:screen :board-select :mouse {:x 0 :y 0}})))

  (it "updates the screen to type select when a board is found"
    (should= :game-type-select (:screen (screens/update {:screen :board-select :board :some-board}))))

  (it "does not update the screen when a board is not found"
    (should= :board-select (:screen (screens/update {:screen :board-select})))))