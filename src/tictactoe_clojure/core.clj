(ns tictactoe-clojure.core)

(def empty-board [[:_ :_ :_]
                  [:_ :_ :_]
                  [:_ :_ :_]])

(defn- count-in [coll item]
  (count (filter #(= item %) (flatten coll))))

(defn- next-mover [board]
  (if (> (count-in board :X)
         (count-in board :O))
    :O
    :X))

(defn move [board row column]
  (update-in board [row column] (fn [_] (next-mover board))))

(defn status [board]
  (cond
    (= 9 (count-in board :_)) :empty
    (= 0 (count-in board :_)) :draw
    :else :ongoing))
