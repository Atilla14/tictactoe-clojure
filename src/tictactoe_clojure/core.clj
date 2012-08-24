(ns tictactoe-clojure.core)

(def empty-board [[:_ :_ :_]
                  [:_ :_ :_]
                  [:_ :_ :_]])

(defn- next-mover [board]
  (if (> (count (filter #(= :X %) (flatten board)))
         (count (filter #(= :O %) (flatten board))))
    :O
    :X))

(defn move [board row column]
  (update-in board [row column] (fn [_] (next-mover board))))
