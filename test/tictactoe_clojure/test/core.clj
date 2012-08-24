(ns tictactoe-clojure.test.core
  (:use [clojure.test]
        [tictactoe-clojure.core]))

(deftest test-empty-board
  (is (= empty-board
         [[:_ :_ :_]
          [:_ :_ :_]
          [:_ :_ :_]])))

(deftest test-basic-opening-moves
  (is (= (move empty-board 0 0) [[:X :_ :_]
                                 [:_ :_ :_]
                                 [:_ :_ :_]]))
  (is (= (move empty-board 2 1) [[:_ :_ :_]
                                 [:_ :_ :_]
                                 [:_ :X :_]])))
