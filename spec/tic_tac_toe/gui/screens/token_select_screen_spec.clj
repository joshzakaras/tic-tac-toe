(ns tic-tac-toe.gui.screens.token-select-screen-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.token-select-screen :as sut]))

(def black [0 0 0])
(def light-red [255 47 76])
(def light-blue [173 215 230])


(describe "The Token Select Screen"
  (with-stubs)

  (it "draws the token question"
    (with-redefs [q/fill (stub :fill)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-token-question)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :text {:with ["Would you like to play as X, or O?" 20 20]})))

  (it "draws an X"
    (with-redefs [q/line (stub :line)
                  q/fill (stub :fill)
                  q/stroke (stub :stroke)
                  q/stroke-weight (stub :stroke-weight)]
      (sut/draw-x)
      (should-have-invoked :fill {:with [nil]})
      (should-have-invoked :stroke-weight {:with [10]})
      (should-have-invoked :stroke {:with [light-red]})
      (should-have-invoked :line {:with [20 50 100 130]})
      (should-have-invoked :line {:with [20 130 100 50]})))

  (it "draws an O"
    (with-redefs [q/ellipse (stub :ellipse)
                  q/stroke (stub :stroke)
                  q/stroke-weight (stub :stroke-weight)
                  q/fill (stub :fill)]
      (sut/draw-o)
      (should-have-invoked :fill {:with [nil]})
      (should-have-invoked :stroke-weight {:with [10]})
      (should-have-invoked :stroke {:with [light-blue]})
      (should-have-invoked :ellipse {:with [170 90 80 80]})))

  (it "Calls all of the draws for the screen"
    (with-redefs [sut/draw-x (stub :x)
                  sut/draw-o (stub :o)
                  sut/draw-token-question (stub :question)
                  q/background (stub :background)]
      (screens/draw {:screen :token-select})
      (should-have-invoked :background {:with [100]})
      (should-have-invoked :x)
      (should-have-invoked :o)
      (should-have-invoked :question)))

  (context "Clicking the Token Select Screen"
    (it "sets the current turn to player when they select x"
      (should= :player (:current-turn (screens/on-click {:screen :token-select :mouse {:x 25 :y 55}}))))

    (it "sets the current turn to computer when the player selects o"
      (should= :computer (:current-turn (screens/on-click {:screen :token-select :mouse {:x 135 :y 55}})))))

  (context "Updating the Token Select Screen"

    (it "updates the screen to active game when there is a current turn selected"
      (should= :active-game (:screen (screens/update {:screen :token-select :current-turn :some-turn}))))

    (it "updates the state to playing when there is a current turn selection"
      (should= :playing (:state (screens/update {:screen :token-select :current-turn :some-turn}))))

    (it "doesn't modify the game state when. there is no current turn selection"
      (let [test-game {:screen :token-select :state :setup}]
        (should= test-game (screens/update test-game))))))