(ns tic-tac-toe.game-state-changers-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.game-state-changers :as sut]))

(describe "A set of tools to update the state of the game"
  (with-stubs)

  (it "updates the board state if there is a win"
    (with-redefs [board/is-there-win? (stub :win? {:return true})])
    (should= :game-won (:state (sut/maybe-update-game-state {:console :terminal :state :playing}))))

  (it "updates the board state if there is a tie"
    (with-redefs [board/is-there-win? (stub :win? {:return false})
                  board/is-there-tie? (stub :tie? {:return true})]
      (should= :game-tied (:state (sut/maybe-update-game-state {:console :terminal :state :playing})))))

  (it "does not update the board state if there is not a win or a tie"
    (with-redefs [board/is-there-win? (stub :win? {:return false})
                  board/is-there-tie? (stub :tie? {:return false})]
      (should= :playing (:state (sut/maybe-update-game-state {:console :terminal :state :playing})))))

  (it "sets the current turn of the game to player when the computer just went during versus-computer"
    (should= :player (:current-turn (sut/update-current-turn {:game-type :versus-computer :current-turn :computer}))))

  (it "sets the current turn of the game to computer when the player just went during versus-computer"
    (should= :computer (:current-turn (sut/update-current-turn {:game-type :versus-computer :current-turn :player}))))

  (it "always sets the turn to player when the game type is versus-player"
    (should= :player (:current-turn (sut/update-current-turn {:game-type :versus-player :current-turn :player}))))
  )