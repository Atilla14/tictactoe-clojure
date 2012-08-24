(ns tictactoe-clojure.core)

; Private
;
(defn- count-in [coll item]
  (count (filter #(= item %) (flatten coll))))

(defn- next-mover [board]
  (if (> (count-in board :X)
         (count-in board :O))
    :O
    :X))

(defn- any-equal [value coll]
  (some #(= value %) coll))

(defn- horizontal-win [board player]
  (any-equal 3 [(count-in (get board 0) player)
                (count-in (get board 1) player)
                (count-in (get board 2) player)]))

(defn- third [coll] (get coll 2))

(defn- vertical-win [board player]
  (any-equal 3 [(count-in (map first  board) player)
                (count-in (map second board) player)
                (count-in (map third  board) player)]))

(defn- diagonal-win [board player]
  (any-equal 3 [(count-in [(get-in board [0 0])
                           (get-in board [1 1])
                           (get-in board [2 2])] player)
                (count-in [(get-in board [0 2])
                           (get-in board [1 1])
                           (get-in board [2 0])] player)]))

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
  (cond (= 9 (count-in board :_)) :empty

        (or (win-for :X board)
            (win-for :O board)) :win

        (= 0 (count-in board :_)) :draw

        :else :ongoing))
