(ns tictactoe-clojure.core)

; Private, generic.

(defn- equal-to [item]
  (fn [x] (= item x)))

(defn- count-in
  "Counts the occurances of item in a collection. Includes any nested occurances."
  [item coll]
  (->> coll
       flatten
       (filter (equal-to item))
       count))

(defn- every-item-is?
  "Returns a predicate that returns true if every item passes to is matches item.

   The predicate may be called with a single collection, or a number of items."
  [item]
  (fn
    ([coll]     (every? (equal-to item) coll))
    ([x & args] (every? (equal-to item) (cons x args)))))

; Private, specific.

(defn- next-mover
  "Figures out whose turn it is."
  [board]
  (if (> (count-in :X board)
         (count-in :O board))
    :O
    :X))

(defn- horizontal-win [player board]
  (->> board
       (map (every-item-is? player))
       (some true?)))

(defn- vertical-win [player board]
  (->> board
       (apply map (every-item-is? player))
       (some true?)))

(defn- diagonal-win-left [player board]
  (let [diagonal (map (fn [i _] (get-in board [i i])) (range) board)]
    ((every-item-is? player) diagonal)))

(defn- diagonal-win-right [player board]
  (diagonal-win-left player (vec (reverse board))))

(defn- win-for [player board]
  (or (horizontal-win     player board)
      (vertical-win       player board)
      (diagonal-win-left  player board)
      (diagonal-win-right player board)))

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
  (cond (win-for :X board) :win-x
        (win-for :O board) :win-o

        (empty? (remove (equal-to :_) (flatten board))) :empty

        (zero? (count-in :_ board)) :draw

        :else :ongoing))
