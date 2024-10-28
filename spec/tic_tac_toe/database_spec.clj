(ns tic-tac-toe.database-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.database :as sut]
            [tic-tac-toe.game-board :as board]))

(describe "A tic tac toe database"
  (with-stubs)

  (it "stores the database txt in the home directory of the program"
    (should= "./database.txt" sut/database-path))

  (it "stores a game board in the database file"
    (with-redefs [spit (stub :spit)]
      (sut/store-game :some-game-board :some-game-settings)
      (should-have-invoked :spit {:with [sut/database-path {:board :some-game-board :game-settings :some-game-settings}]} ) ) )

  (it "reads the game board from the database file"
    (with-redefs [slurp (stub :slurp {:return "{:board [[\"\" \"\" \"\"] [\"\" \"\" \"\"] [\"\" \"\" \"\"]], :game-settings {:game-type :some-type, :difficulty :some-difficulty, :player-token :some-token}}"})]
      (sut/read-stored-game)
      (should-have-invoked :slurp {:with [sut/database-path]})
      (should= {:board (board/new-board), :game-settings {:game-type :some-type, :difficulty :some-difficulty, :player-token :some-token}} (sut/read-stored-game))))

  (it "clears a save from the database file"
    (with-redefs [spit (stub :spit)]
      (sut/clear-save)
      (should-have-invoked :spit {:with [sut/database-path ""]})))

  (it "checks the database for an existing save"
    (with-redefs [sut/read-stored-game (stub :read-stored-game {:return ""})]
      (should-not (sut/existing-save?)))
    (with-redefs [sut/read-stored-game (stub :read-stored-game {:return :some-data})]
      (should (sut/existing-save?))))

  (it "returns \"\" if it cannot read from the database file"
    (with-redefs [slurp (stub :slurp {:throw Exception})]
      (should= "" (sut/read-stored-game)))))
