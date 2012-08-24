(ns tictactoe-clojure.core)

(def empty-board [[:_ :_ :_]
                  [:_ :_ :_]
                  [:_ :_ :_]])

(defn- count-in [board item]
  (count (filter #(= item %) (flatten board))))

(defn- next-mover [board]
  (if (> (count-in board :X)
         (count-in board :O))
    :O
    :X))

(defn move [board row column]
  (update-in board [row column] (fn [_] (next-mover board))))

(defn status [board]
  :empty)
