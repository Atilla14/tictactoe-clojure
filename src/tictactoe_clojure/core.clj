(ns tictactoe-clojure.core)

; Private, generic.

(defn- third
  "Like the built-in convenience functions first & second."
  [coll] (get coll 2))

(defn- count-in
  "Counts the occurances of item in a collection. Includes any nested occurances."
  [item coll]
  (->> coll
       flatten
       (filter #(= item %))
       count))

(defn- count-is [value item coll]
  (= value
     (count-in item coll)))

; Private, specific.

(defn- next-mover
  "Figures out whose turn it is."
  [board]
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

(defn move
  "Make the next move at the named position."
  [board row column]
  (update-in board
             [row column]
             (fn [current]
               (assert (= :_ current) "Square not empty.")
               (next-mover board))))

(defn status
  "Returns the state of the game."
  [board]
  (cond (or (win-for :X board)
            (win-for :O board)) :win

        (count-is 9 :_ board) :empty

        (count-is 0 :_ board) :draw

        :else :ongoing))
