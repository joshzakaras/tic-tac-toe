(ns tic-tac-toe.terminal.update-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.computer-player :as cpu]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.ui :as ui]
            [tic-tac-toe.terminal.update :as sut]
            [tic-tac-toe.game-state-changers :as game-state]))

(describe "Terminal Update"
  (with-stubs)

  (it "updates the board with the players turn"
    (with-redefs [read-line (stub :read-line {:return "00"})]
      (let [sample-game {:console :terminal, :current-turn :player, :board (board/new-board)}]
        (should= (board/set-square {:row 0 :column 0} :x (board/new-board)) (:board (sut/play-turn sample-game))))))

  (it "updates the board with the computers turn"
    (let [sample-game {:console :terminal, :current-turn :computer, :board (board/new-board) :difficulty :hard}]
      (should= (cpu/play-computer-turn (board/new-board) :hard) (:board (sut/play-turn sample-game)))))

  (it "plays the turn when state is playing"
    (with-redefs [sut/play-turn (stub :play-turn)
                  core/update-current-turn (stub :update-current-turn)]
      (sut/maybe-play-turn {:state :playing})
      (should-have-invoked :play-turn)))

  (it "does not play the turn when the state is not playing"
    (with-redefs [sut/play-turn (stub :play-turn)
                  core/update-current-turn (stub :update-current-turn)]
      (sut/maybe-play-turn {:state :not-playing})
      (should-not-have-invoked :play-turn)))

  (it "updates the current turn when maybe-play-turn succeeds"
    (with-redefs [sut/play-turn (stub :play-turn)
                  core/update-current-turn (stub :update-current-turn)]
      (sut/maybe-play-turn {:state :playing})
      (should-have-invoked :update-current-turn)))

  (context "The Core Update Function"

    (it "Completes an update cycle where a game is won"
      (with-redefs [sut/play-turn (stub :play-turn)]
        (should= :game-won (:state (core/update {:console :terminal :state :playing :board [[:x :x :x] ["" "" ""] ["" "" ""]]})))
        (should-not-have-invoked :play-turn)))

    (it "completes an update cycle where a game is tied"
      (with-redefs [sut/play-turn (stub :play-turn)]
        (should= :game-tied (:state (core/update {:console :terminal :state :playing :board [[:x :o :x][:x :o :x][:o :x :o]]})))
        (should-not-have-invoked :play-turn)))

    (it "completes an update cycle where the player chooses to play in the top left"
      (with-redefs [read-line (stub :read-line {:return "00"})]
        (let [sample-game {:console :terminal :state :playing :board (board/new-board) :game-type :versus-player :current-turn :player}]
          (should= (board/set-square {:row 0 :column 0} :x (:board sample-game)) (:board (core/update sample-game))))))

    (it "completes an update cycle where it's the computers turn"
      (let [sample-game {:console :terminal :state :playing :board (board/new-board) :game-type :versus-computer :current-turn :computer :difficulty :hard}]
        (should= (cpu/play-computer-turn (:board sample-game) (:difficulty sample-game)) (:board (core/update sample-game)))))))