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

(deftest test-subsequent-moves
  (is (= (-> empty-board
             (move 0 0)
             (move 1 1)
             (move 0 2))
         [[:X :_ :X]
          [:_ :O :_]
          [:_ :_ :_]]))

  (is (= (move [[:X :X :_]
                [:O :O :_]
                [:_ :_ :_]]
               0 2)
         [[:X :X :X]
          [:O :O :_]
          [:_ :_ :_]])))

(deftest test-no-duplicate-moves
  (is (thrown-with-msg? AssertionError #"not empty"
                        (-> empty-board
                            (move 0 0)
                            (move 0 0)))))

(deftest test-status-empty
  (is (= :empty (status empty-board))))

(deftest test-status-ongoing
  (is (= :ongoing (-> empty-board
                      (move 0 0)
                      status)))

  (is (= :ongoing (-> empty-board
                      (move 0 0)
                      (move 0 1)
                      (move 0 2)
                      status)))

  (is (= :ongoing (status [[:X :X :_]
                           [:O :O :_]
                           [:X :O :_]]))))

(deftest test-status-draw
  (is (= :draw (status [[:X :X :O]
                        [:O :X :X]
                        [:X :O :O]]))))

(deftest test-status-win-horizontal
  (is (= :win (status [[:O :O :_]
                       [:X :X :X]
                       [:_ :_ :_]])))

  (is (= :win (status [[:O :O :_]
                       [:_ :_ :_]
                       [:X :X :X]]))))

(deftest test-status-win-vertical
  (is (= :win (status [[:X :X :O]
                       [:X :X :O]
                       [:X :O :O]])))

  (is (= :win (status [[:X :X :X]
                       [:O :O :_]
                       [:_ :_ :_]]))))

(deftest test-status-win-diagonal
  (is (= :win (status [[:X :O :_]
                       [:_ :X :O]
                       [:_ :_ :X]])))

  (is (= :win (status [[:X :_ :O]
                       [:_ :O :X]
                       [:O :X :_]]))))

(deftest test-status-win-diagonal
  (is (= :ongoing (status [[:X :O :_]
                           [:_ :X :O]
                           [:_ :_ :_]]))))

(deftest test-status-win-4-horizontal
  (is (= :win (status [[:O :O :_ :_]
                       [:_ :O :_ :O]
                       [:X :X :X :X]
                       [:_ :_ :_ :X]]))))

(deftest test-status-win-4-vertical
  (is (= :win (status [[:O :O :X :_]
                       [:_ :O :X :O]
                       [:X :O :X :X]
                       [:_ :O :_ :X]]))))

(deftest test-status-win-4-diagonal
  (is (= :win (status [[:O :O :X :O]
                       [:_ :O :O :_]
                       [:X :O :X :X]
                       [:O :X :X :X]]))))

(deftest test-status-no-win-3-out-of-4-diagonal
  (is (= :ongoing (status [[:O :O :_ :_]
                           [:_ :O :O :_]
                           [:X :O :X :X]
                           [:O :X :X :X]]))))
