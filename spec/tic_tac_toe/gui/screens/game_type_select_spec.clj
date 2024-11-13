(ns tic-tac-toe.gui.screens.game-type-select-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.mouse-helper-functions :as mouse-helper]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.game-type-select :as sut]))

(def confirmation-green [35 101 51])
(def black [0 0 0])
(def denial-red [178 22 22])

(describe "The New Game Screen"
  (with-stubs)

  (it "draws the game type question"
    (with-redefs [q/fill (stub :fill)
                  q/text-size (stub :text-size)
                  q/text (stub :text)]
      (sut/draw-game-type-question)
      (should-have-invoked :fill {:with [black]})
      (should-have-invoked :text-size {:with [20]})
      (should-have-invoked :text {:with ["Would you like to play against a computer?" 20 20]})))

  (it "draws a confirmation box for the game type question"
    (with-redefs [q/rect (stub :rect)
                  q/stroke (stub :stroke)
                  q/fill (stub :fill)]
      (sut/draw-confirmation-box)
      (should-have-invoked :fill {:with [confirmation-green]})
      (should-have-invoked :rect {:with [10 40 100 50]})))

  (it "draws a denial box for the game type question"
    (with-redefs [q/rect (stub :rect)
                  q/stroke (stub :stroke)
                  q/fill (stub :fill)]
      (sut/draw-denial-box)
      (should-have-invoked :fill {:with [denial-red]})
      (should-have-invoked :rect {:with [120 40 100 50]})))

  (it "checks if the mouse is within the bounds of the confirmation box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-confirmation-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[10 40 100 50] :some-x :some-y]})))

  (it "checks if the mouse is within the bounds of the denial box"
    (with-redefs [mouse-helper/within-rect? (stub :within-rect?)]
      (sut/within-denial-box? :some-x :some-y)
      (should-have-invoked :within-rect? {:with [[120 40 100 50] :some-x :some-y]})))

  (context "The Draw Operation for the screen"
    (it "draws the game type screen"
      (with-redefs [sut/draw-game-type-question (stub :question)
                    sut/draw-confirmation-box (stub :confirm)
                    sut/draw-denial-box (stub :deny)
                    q/background (stub :background)]
        (screens/draw {:screen :game-type-select})
        (should-have-invoked :question)
        (should-have-invoked :confirm)
        (should-have-invoked :deny)
        (should-have-invoked :background))))

  (context "Clicking on the Game Type Screen"
    (it "sets the game type to versus computer when the confirmation box is clicked"
      (should= :versus-computer (:game-type (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 15 :y 45}}))))

    (it "sets the screen to the computer difficulty screen when the confirmation box is clicked"
      (should= :difficulty-select (:screen (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 15 :y 45}}))))

    (it "sets the game type to versus player when the denial box is clicked"
      (should= :versus-player (:game-type (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 125 :y 45}}))))

    (it "sets the screen to active-game when the denial box is clicked"
      (should= :active-game (:screen (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 125 :y 45}}))))

    (it "sets the state to playing when the denial box is clicked"
      (should= :playing (:state (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 125 :y 45}}))))

    (it "sets the current player to :player when the denial box is clicked"
      (should= :player (:current-turn (screens/on-click {:screen :game-type-select :state :setup :mouse {:x 125 :y 45}}))))))