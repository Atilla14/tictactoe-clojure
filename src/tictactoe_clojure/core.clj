(ns tictactoe-clojure.core)

; Private, generic.

(defn- third [coll] (get coll 2))

(defn- equal-to [x]
  (fn [y] (= x y)))

(defn- count-in
  "Counts the occurances of item in a collection. Includes any nested occurances."
  [item coll]
  (->> coll
       flatten
       (filter (equal-to item))
       count))

(defn- count-is [value item coll]
  (= value
     (count-in item coll)))

; Private, specific.

(defn- next-mover [board]
  (if (> (count-in :X board)
         (count-in :O board))
    :O
    :X))

(defn- horizontal-win [board player]
  (or (count-is 3 player (first  board))
      (count-is 3 player (second board))
      (count-is 3 player (third  board))))

(defn- vertical-win [board player]
  (or (count-is 3 player (map first  board))
      (count-is 3 player (map second board))
      (count-is 3 player (map third  board))))

(defn- diagonal-win [board player]
  (or (count-is 3 player [(get-in board [0 0])
                          (get-in board [1 1])
                          (get-in board [2 2])])
      (count-is 3 player [(get-in board [0 2])
                          (get-in board [1 1])
                          (get-in board [2 0])])))

(defn- win-for [player board]
  (or (horizontal-win board player)
      (vertical-win   board player)
      (diagonal-win   board player)))

; Public

(def empty-board [[:_ :_ :_]
                  [:_ :_ :_]
                  [:_ :_ :_]])

(defn move [board row column]
  (update-in board [row column] (fn [_] (next-mover board))))

(defn status [board]
  (cond (count-is 9 :_ board) :empty

        (or (win-for :X board)
            (win-for :O board)) :win

        (count-is 0 :_ board) :draw

        :else :ongoing))
