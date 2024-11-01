(ns tic-tac-toe.gui.game-screen-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.game-screen :as sut]))

(def light-red [255 47 76])
(def black [0 0 0])
(def light-blue [173 215 230])

(describe "GUI Functions for the game screen"
  (with-stubs)

  (it "draws a game board outline"
    (with-redefs [q/rect (stub :rect)
                  q/fill (stub :fill)
                  q/stroke (stub :stroke)]
      (sut/draw-board-outline (board/new-board))
      (should-have-invoked :stroke {:with black})
      (should-have-invoked :rect {:times 9})))

  (it "can draw an X"
    (with-redefs [q/line (stub :line)
                  q/fill (stub :fill)
                  q/stroke (stub :stroke)]
      (sut/draw-x-at-location 0 0)
      (should-have-invoked :stroke {:with [light-red]})
      (should-have-invoked :line {:with [10 10 90 90]})
      (should-have-invoked :line {:with [10 90 90 10]})))

  (it "can draw an 0"
    (with-redefs [q/ellipse (stub :ellipse)
                  q/stroke (stub :stroke)]
      (sut/draw-o-at-location 100 100)
      (should-have-invoked :stroke {:with [light-blue]})
      (should-have-invoked :ellipse {:with [150 150 80 80]})))

  (it "can draw all of the tokens on the game board"
    (with-redefs [sut/draw-x-at-location (stub :draw-x)
                  sut/draw-o-at-location (stub :draw-o)]
      (sut/draw-board-tokens [[:x :o ""] ["" "" ""] ["" "" ""]])
      (should-have-invoked :draw-x (:with [0 0]))
      (should-have-invoked :draw-o (:with [100 0]))))

  (it "uses the mouse click as a trigger to play the turn"
    (should= {:board [[:x "" ""] ["" :o ""] ["" "" ""]] :game-settings {:game-type :versus-computer :difficulty :hard :player-token :x}}
             (sut/on-mouse-clicked {:board (board/new-board) :game-settings {:game-type :versus-computer :difficulty :hard :player-token :x}} {:x 50 :y 50}))))