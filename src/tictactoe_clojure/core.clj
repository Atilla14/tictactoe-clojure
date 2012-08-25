(ns tictactoe-clojure.core)

; Private, generic.

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

(defn equal-to [item]
  (fn [x] (= x item)))

(defn- horizontal-win [board player]
  (let [is-match #(= % player)]
    (some true? (map #(every? is-match %) board))))

(defn- vertical-win [board player]
  (let [is-match #(= % player)]
    (some true? (apply map (fn [& args] (every? is-match args)) board))))

(defn diagonal-win-left [board player]
  (let [is-match #(= % player)]
    (every? is-match (map (fn [_ i] (get-in board [i i])) board (iterate inc 0)))))


(defn- diagonal-win-right [board player]
  (diagonal-win-left (vec (reverse board))
                     player))

(defn- diagonal-win [board player]
  (or (diagonal-win-left  board player)
      (diagonal-win-right board player)))

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

        (empty? (remove #(= :_ %) (flatten board))) :empty

        (count-is 0 :_ board) :draw

        :else :ongoing))
