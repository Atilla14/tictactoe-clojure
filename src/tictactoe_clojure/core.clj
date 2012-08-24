(ns tictactoe-clojure.core)

(defn- count-in [coll item]
  (count (filter #(= item %) (flatten coll))))

(defn- next-mover [board]
  (if (> (count-in board :X)
         (count-in board :O))
    :O
    :X))

(defn- horizontal-win [board player]
  (or (= 3 (count-in (get board 0) player))
      (= 3 (count-in (get board 1) player))
      (= 3 (count-in (get board 2) player))))

(defn- third [coll] (get coll 2))

(defn- vertical-win [board player]
  (or (= 3 (count-in (map first  board) player))
      (= 3 (count-in (map second board) player))
      (= 3 (count-in (map third  board) player))))

(defn- diagonal-win [board player]
  (or
    (= 3 (count-in [(get-in board [0 0])
                    (get-in board [1 1])
                    (get-in board [2 2])] player))
    (= 3 (count-in [(get-in board [0 2])
                    (get-in board [1 1])
                    (get-in board [2 0])] player))))

; Public

(def empty-board [[:_ :_ :_]
                  [:_ :_ :_]
                  [:_ :_ :_]])

(defn move [board row column]
  (update-in board [row column] (fn [_] (next-mover board))))

(defn status [board]
  (cond (= 9 (count-in board :_)) :empty

        (or (horizontal-win board :X)
            (horizontal-win board :O)
            (vertical-win board :X)
            (vertical-win board :O)
            (diagonal-win board :X)
            (diagonal-win board :O)) :win

        (= 0 (count-in board :_)) :draw

        :else :ongoing))
