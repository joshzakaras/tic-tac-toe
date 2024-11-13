(ns tic-tac-toe.gui.screens.active-game-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.game-state-changers :as game-state]
            [tic-tac-toe.gui.screen-core :as screens]
            [tic-tac-toe.gui.screens.active-game :as sut]))

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

  (it "does the computers turn and puts it into the board"
    (should= (cpu/play-computer-turn (board/new-board) :hard) (:board (sut/do-computer-turn {:board (board/new-board) :difficulty :hard :current-turn :computer}))))

  (it "updates the current turn when the computers turn is played"
    (should= :player (:current-turn (sut/do-computer-turn {:board (board/new-board) :difficulty :hard :current-turn :computer}))))

  (it "plays a computer turn if the game state is playing and the current-turn is computer"
    (with-redefs [sut/do-computer-turn (stub :do-computer-turn)]
      (sut/maybe-do-computer-turn {:state :playing :current-turn :computer})
      (should-have-invoked :do-computer-turn)))

  (it "does not play a computer turn if the current-turn is not computer"
    (with-redefs [sut/do-computer-turn (stub :do-computer-turn)]
      (sut/maybe-do-computer-turn {:state :playing :current-turn :not-computer})
      (should-not-have-invoked :do-computer-turn)))

  (it "does not play a computer turn if the state is not playing"
    (with-redefs [sut/do-computer-turn (stub :do-computer-turn)]
      (sut/maybe-do-computer-turn {:state :not-playing :current-turn :computer})
      (should-not-have-invoked :do-computer-turn)))

  (it "updates the game screen if the state is not playing"
    (should= :game-over (:screen (sut/maybe-end-game {:screen :active-game :state :not-playing}))))

  (it "does not update the game screen if the state is playing"
    (should= :active-game (:screen (sut/maybe-end-game {:screen :active-game :state :playing}))))

  (it "clears the save data if the state is not playing"
    (with-redefs [db/clear-save! (stub :clear-save!)]
      (sut/maybe-end-game {:state :not-playing})
      (should-have-invoked :clear-save!)))

  (it "does not clear save data if the state is playing"
    (with-redefs [db/clear-save! (stub :clear-save!)]
      (sut/maybe-end-game {:state :playing})
      (should-not-have-invoked :clear-save!)))

  (context "Drawing the Active Game Screen"
    (it "Calls all of the draw and format functions for the game screen"
      (with-redefs [sut/fit-sketch-to-board (stub :fit-sketch-to-board)
                    q/background (stub :background)
                    q/fill (stub :fill)
                    q/stroke-weight (stub :stroke-weight)
                    sut/draw-board-outline (stub :draw-board-outline)
                    sut/draw-board-tokens (stub :draw-board-tokens)]
        (screens/draw {:screen :active-game})
        (should-have-invoked :fit-sketch-to-board)
        (should-have-invoked :background)
        (should-have-invoked :fill)
        (should-have-invoked :stroke-weight)
        (should-have-invoked :draw-board-outline)
        (should-have-invoked :draw-board-tokens))))

  (context "Clicking on the Active Game Screen"
    (it "Updates the board with the inputted move if the move is valid"
      (should= [[:x "" ""] ["" "" ""]["" "" ""]]
               (:board (screens/on-click {:mouse {:x 50 :y 50} :screen :active-game :board (board/new-board) :current-turn :player :game-type :versus-computer}))))

    (it "does not update the board with the inputted move if the move is invalid"
      (should= [[:x "" ""]["" "" ""]["" "" ""]]
               (:board (screens/on-click {:mouse {:x 50 :y 50} :screen :active-game :board [[:x "" ""]["" "" ""]["" "" ""]] :current-turn :player :game-type :versus-computer}))))

    (it "Updates the current turn when a valid move is played"
      (should= :computer
               (:current-turn (screens/on-click {:mouse {:x 50 :y 50} :screen :active-game :board (board/new-board) :current-turn :player :game-type :versus-computer})))))

  (context "Updating the active game screen"
    (it "saves the game"
      (with-redefs [db/store-game! (stub :store-game!)
                    sut/maybe-do-computer-turn (stub :maybe-do-computer-turn)
                    game-state/maybe-update-game-state (stub :maybe-update-game-state)
                    sut/maybe-end-game (stub :maybe-update-screen)]
        (screens/update {:screen :active-game})
        (should-have-invoked :store-game!)))

    (it "updates the game state if needed"
      (with-redefs [db/store-game! (stub :store-game!)
                    sut/maybe-do-computer-turn (stub :maybe-do-computer-turn)
                    game-state/maybe-update-game-state (stub :maybe-update-game-state)
                    sut/maybe-end-game (stub :maybe-update-screen)]
        (screens/update {:screen :active-game})
        (should-have-invoked :maybe-update-game-state)))

    (it "plays the computer turn if needed"
      (with-redefs [db/store-game! (stub :store-game!)
                    game-state/maybe-update-game-state (stub :maybe-update-game-state {:return {:some-game-keys :some-game-values}})
                    sut/maybe-do-computer-turn (stub :maybe-computer-move)
                    sut/maybe-end-game (stub :maybe-update-screen)]
        (screens/update {:screen :active-game})
        (should-have-invoked :maybe-computer-move)))

    (it "ends the game if needed"
      (with-redefs [db/store-game! (stub :store-game!)
                    game-state/maybe-update-game-state (stub :maybe-update-game-state {:return {:some-game-keys :some-game-values}})
                    sut/maybe-do-computer-turn (stub :maybe-computer-move)
                    sut/maybe-end-game (stub :maybe-update-screen)]
        (screens/update {:screen :active-game})
        (should-have-invoked :maybe-update-screen)))))