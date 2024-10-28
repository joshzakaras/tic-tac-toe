(ns tic-tac-toe.gui.gui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.gui.gui :as sut]
            [quil.core :as q]
            [tic-tac-toe.game-board :as board]))

(def light-red [255 47 76])

(def black [0 0 0])

(def light-blue [173 215 230])

(describe "A Tic Tac Toe GUI using Quil"
  (with-stubs)

  (it "creates a default state in setup with a supplied board and game settings"
    (should= {:board :some-board :game-settings :some-settings} (sut/setup :some-board :some-settings)))

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
  (it "converts mouse coordinates to potential moves on the board"
    (should= {:row 0 :column 0} (sut/mouse-to-tile-coords 50 50)))
  (it "uses the mouse click as a trigger to play the turn"
    (should= {:board [[:x "" ""] ["" :o ""] ["" "" ""]] :game-settings {:game-type :versus-computer :difficulty :hard :player-token :x}}
             (sut/mouse-clicked {:board (board/new-board) :game-settings {:game-type :versus-computer :difficulty :hard :player-token :x}} {:x 50 :y 50})))
  (it "ends the game and closes the gui"
    (with-redefs [q/exit (stub :exit)
                  sut/handle-win (stub :handle-win)
                  sut/handle-tie (stub :handle-tie)
                  board/get-win (stub :get-win {:return :x})
                  sut/finish-game (stub :finish-game)]
      (sut/update :some-state)
      (should-have-invoked :finish-game))
    (with-redefs [q/exit (stub :exit)
                  sut/handle-win (stub :handle-win)
                  sut/handle-tie (stub :handle-tie)
                  board/get-win (stub :get-win {:return nil})
                  board/full-board? (stub :get-tie {:return true})
                  sut/finish-game (stub :finish-game)]
      (sut/update :some-state)
      (should-have-invoked :finish-game))
    (with-redefs [q/exit (stub :exit)
                  board/get-win (stub :get-win {:return :x})
                  sut/handle-win (stub :handle-win)]
      (sut/finish-game :winning-board)
      (should-have-invoked :handle-win)
      (should-have-invoked :exit))
    (with-redefs [q/exit (stub :exit)
                  board/get-win (stub :get-win {:return nil})
                  board/full-board? (stub :full-board {:return true})
                  sut/handle-tie (stub :handle-tie)]
      (sut/finish-game :winning-board)
      (should-have-invoked :handle-tie)
      (should-have-invoked :exit))))