(ns tictactoe-clojure.core)

; Private, generic.

(defn- select-all [item coll]
  (filter #(= item %) (flatten coll)))

(defn- select-not [item coll]
  (remove #(= item %) (flatten coll)))

(defn- every-item-is?
  "Returns a predicate that returns true if every item passes to is matches item."
  [item]
  (fn [coll]
    (empty? (select-not item coll))))

(defn- rows-to-columns
  "Turns rows to columns in a square 2d array.

   The array must be square."
  [grid]
  (apply map list grid))

(defn- nested-array-is-square [coll]
  (apply = (count coll) (map count coll)))

; Private, specific.

(defn- next-mover
  "Figures out whose turn it is."
  [board]
  (if (> (count (select-all :X board))
         (count (select-all :O board)))
    :O
    :X))

(defn- empty-board? [board] (empty? (select-not :_ board)))
(defn- full-board?  [board] (empty? (select-all :_ board)))

; Win checks.
(defn- horizontal-win? [player board]
  (->> board
       (map (every-item-is? player))
       (some true?)))

(defn- vertical-win? [player board]
  (horizontal-win? player (rows-to-columns board)))

(defn- diagonal-win-left? [player board]
    ((every-item-is? player)
       (map get board (range))))

(defn- diagonal-win-right? [player board]
  (diagonal-win-left? player (reverse board)))

; Simply combine the above.
(defn- win-for? [player board]
  (or (horizontal-win?     player board)
      (vertical-win?       player board)
      (diagonal-win-left?  player board)
      (diagonal-win-right? player board)))

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
  (cond (win-for? :X board) :win-x

        (win-for? :O board) :win-o

        (empty-board? board) :empty

        (full-board?  board) :draw

        :else :ongoing))

(defn valid-board [board]
  (and (every? #{:_ :X :O} (set (flatten board)))
       (nested-array-is-square board)
       (let [
             x-count (count (select-all :X board))
             o-count (count (select-all :O board))
             ]
         (or (= x-count o-count)
             (= x-count (inc o-count))))))
