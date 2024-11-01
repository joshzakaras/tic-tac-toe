(ns tic-tac-toe.terminal.terminal-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.database :as db]
            [tic-tac-toe.game-board :as board]
            [tic-tac-toe.terminal.terminal :as sut]
            [tic-tac-toe.core :as core]
            [tic-tac-toe.terminal.ui :as terminal]))

(describe "A Tic Tac Toe game ran in the terminal"
  (with-stubs)

  (it "sets the initial state of the game to setup"
    (with-redefs [core/setup (stub :setup)
                  sut/run-game (stub :run-game)
                  sut/end-game (stub :end-game)
                  merge (stub :merge)]
      (core/start-game! {:console :terminal})
      (should-have-invoked :merge {:with [{:console :terminal} {:state :setup}]})))

  (it "starts setting up the game"
    (with-redefs [core/setup (stub :setup)
                  sut/run-game (stub :run-game)
                  sut/end-game (stub :end-game)]
      (core/start-game! {:console :terminal})
      (should-have-invoked :setup {:with [{:console :terminal :state :setup}]})))

  (let [sample-setup-return {:console :terminal :state :setup :some-game-keys :some-game-values}]
    (it "starts the update loop"
      (with-redefs [core/setup (stub :setup {:return sample-setup-return})
                    sut/run-game (stub :run-game)
                    sut/end-game (stub :end-game)]
        (core/start-game! {:console :terminal})
        (should-have-invoked :run-game {:with [(assoc sample-setup-return :state :playing)]}))))

    (it "ends the game"
      (let [sample-finished-game {:console :terminal :state :game-over :some-game-keys :some-game-values}]
        (with-redefs [core/setup (stub :setup {:return sample-finished-game})
                      sut/run-game (stub :run-game {:return sample-finished-game})
                      sut/end-game (stub :end-game)]
          (core/start-game! sample-finished-game))))

  (it "draws the screen when the game is over"
    (with-redefs [core/draw (stub :draw)
                  db/clear-save! (stub :clear-save!)]
      (sut/end-game {:console :terminal})
      (should-have-invoked :draw {:with [{:console :terminal}]})))

  (it "clears the save data when the game is over"
    (with-redefs [core/draw (stub :draw)
                  db/clear-save! (stub :clear-save!)]
      (sut/end-game {:console :terminal})
      (should-have-invoked :clear-save!)))

  (context "Game Loop"
    (it "returns the game when not in the playing state"
      (should= {:state :not-playing :some-game-keys :some-game-values} (sut/run-game {:state :not-playing :some-game-keys :some-game-values})))
    (it "stores the game when in the playing state"
      (with-redefs [db/store-game! (stub :store-game!)
                    core/draw (stub :draw)
                    core/update (stub :update {:return {:state :not-playing}})]
        (sut/run-game {:state :playing :some-game-keys :some-game-values})
        (should-have-invoked :store-game! {:with [{:state :playing :some-game-keys :some-game-values}]})))

    (it "draws the game when in the playing state"
      (with-redefs [db/store-game! (stub :store-game!)
                    core/draw (stub :draw)
                    core/update (stub :update {:return {:state :not-playing}})]
        (sut/run-game {:state :playing :some-game-keys :some-game-values})
        (should-have-invoked :draw)))

    (it "updates the game when in the playing state"
      (with-redefs [db/store-game! (stub :store-game!)
                    core/draw (stub :draw)
                    core/update (stub :update {:return {:state :not-playing}})]
        (sut/run-game {:state :playing :some-game-keys :some-game-values})
        (should-have-invoked :update)))))