(ns tic-tac-toe.gui.screens.game-over-screen-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.game-over-screen :as sut]))

(def black [0 0 0])

(describe "The Game Over Screen"
  (with-stubs)

  (it "draws the O win"
    (with-redefs [q/text (stub :text)]
      (sut/draw-result {:result :o})
      (should-have-invoked :text {:with ["O has won the game!" 20 20]})))

  (it "draws the X win"
    (with-redefs [q/text (stub :text)]
      (sut/draw-result {:result :x})
      (should-have-invoked :text {:with ["X has won the game!" 20 20]})))

  (it "draws the tie"
    (with-redefs [q/text (stub :text)]
      (sut/draw-result {:result :tie})
      (should-have-invoked :text {:with ["Game results in a tie..." 20 20]})))

  (it "passes the game through the update function"
    (let [sample-game {:screen :game-over}]
      (should= sample-game (screens/update sample-game))))

  (context "Drawing the game over screen"
    (it "calls all of the proper methods to draw the game over screen"
      (with-redefs [q/fill (stub :fill)
                    q/background (stub :background)
                    q/text-size (stub :text-size)
                    sut/draw-result (stub :draw-result)]
        (screens/draw {:screen :game-over})
        (should-have-invoked :fill {:with [black]})
        (should-have-invoked :background {:with [100]})
        (should-have-invoked :text-size {:with [20]})))

    (it "draws the tie text when the game is a tie"
      (with-redefs [q/fill (stub :fill)
                    q/background (stub :background)
                    q/text-size (stub :text-size)
                    sut/draw-result (stub :draw-result)]
        (screens/draw {:screen :game-over :state :game-tied})
        (should-have-invoked :draw-result {:with [{:result :tie}]})))

    (it "draws the x won text when x won the game"
      (with-redefs [q/fill (stub :fill)
                    q/background (stub :background)
                    q/text-size (stub :text-size)
                    sut/draw-result (stub :draw-result)
                    board/get-win (stub :get-win {:return :x})]
        (screens/draw {:screen :game-over :state :game-won})
        (should-have-invoked :draw-result {:with [{:result :x}]})))

    (it "draws the o won text when o won the game"
      (with-redefs [q/fill (stub :fill)
                    q/background (stub :background)
                    q/text-size (stub :text-size)
                    sut/draw-result (stub :draw-result)
                    board/get-win (stub :get-win {:return :o})]
        (screens/draw {:screen :game-over :state :game-won})
        (should-have-invoked :draw-result {:with [{:result :o}]})))))