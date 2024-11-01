(ns tic-tac-toe.gui.mouse-helper-functions-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.gui.mouse-helper-functions :as sut]))


(describe "A list of helper functions to help process the mouse events"
  (it "checks if a mouse is within a generated rect given the same parameters"
    (should (sut/within-rect? [0 0 50 50] 25 25))
    (should-not (sut/within-rect? [10 10 10 10] 5 10))
    (should-not (sut/within-rect? [10 10 10 10] 21 10))
    (should-not (sut/within-rect? [10 10 10 10] 10 5))
    (should-not (sut/within-rect? [10 10 10 10] 10 21)))

  (it "converts mouse coordinates to potential moves on the board"
    (should= {:row 0 :column 0} (sut/mouse-to-tile-coords 50 50))))