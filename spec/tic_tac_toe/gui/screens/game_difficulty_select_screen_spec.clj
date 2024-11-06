(ns tic-tac-toe.gui.screens.game-difficulty-select-screen-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.game-difficulty-select-screen :as sut]))

(def black [0 0 0])
(def white [255 255 255])
(def gray [128 128 128])

(describe "The Game Difficulty Select Screen"
  (with-stubs)

  (it "draws the difficulty question"
    (with-redefs [q/fill (stub :fill)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-difficulty-question)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :text {:with ["Would you like to play Easy, Medium, or Hard?" 20 20]})))

  (it "draws a white box for easy"
    (with-redefs [q/fill (stub :fill)
                  q/rect (stub :rect)]
      (sut/draw-easy-box)
      (should-have-invoked :fill {:with [white]})
      (should-have-invoked :rect {:with [10 40 100 50]})))

  (it "draws a gray box for med"
    (with-redefs [q/fill (stub :fill)
                  q/rect (stub :rect)]
      (sut/draw-med-box)
      (should-have-invoked :fill {:with [gray]})
      (should-have-invoked :rect {:with [120 40 100 50]})))

  (it "draws a black box for hard"
    (with-redefs [q/fill (stub :fill)
                  q/rect (stub :rect)]
      (sut/draw-hard-box)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :rect {:with [230 40 100 50]})))

  (it "runs all of the draw functions for the difficulty question prompt"
    (with-redefs [sut/draw-difficulty-question (stub :question)
                  sut/draw-easy-box (stub :draw-easy-box)
                  sut/draw-hard-box (stub :draw-hard-box)
                  sut/draw-med-box (stub :draw-med-box)
                  q/background (stub :background)]
      (screens/draw {:screen :difficulty-select})
      (should-have-invoked :background)
      (should-have-invoked :draw-easy-box)
      (should-have-invoked :draw-med-box)
      (should-have-invoked :draw-hard-box)
      (should-have-invoked :question)))

  (context "Clicking on the difficulty prompt"
    (it "sets the difficulty to easy when in the easy box"
      (should= :easy (:difficulty (screens/on-click {:screen :difficulty-select :state :setup :mouse {:x 15 :y 45}}))))

    (it "sets the difficulty to medium when in the med box"
      (should= :med (:difficulty (screens/on-click {:screen :difficulty-select :state :setup :mouse {:x 125 :y 45}}))))

    (it "sets the difficulty to hard when in the hard box"
      (should= :hard (:difficulty (screens/on-click {:screen :difficulty-select :state :setup :mouse {:x 235 :y 45}}))))

    (it "returns the same game state when mouse is not in any box"
      (let [test-game {:screen :difficulty-select :state :setup :mouse {:x 700 :y 700}}]
        (should= test-game (screens/on-click test-game)))))

  (context "The Update Function"
    (it "sets the screen to the token-select when a difficulty has been selected"
      (should= :token-select (:screen (screens/update {:screen :difficulty-select :difficulty :some-difficulty}))))

    (it "does not set the screen to token-select when a difficulty has not been selected"
      (should= :difficulty-select (:screen (screens/update {:screen :difficulty-select}))))))