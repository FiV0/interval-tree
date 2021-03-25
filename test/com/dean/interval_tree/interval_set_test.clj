(ns com.dean.interval-tree.interval-set-test
  (:require [clojure.test                :refer :all]
            [com.dean.interval-tree.tree :refer [interval-set]]))

;; TODO: more

(deftest smoke-check
  (let [x (interval-set [[1 3] [2 4] [5 9] [3 6]])]
    (is (= [[1 3] [2 4] [3 6] [5 9]] (seq x)))
    (is (= nil (x 0)))
    (is (= [[1 3]] (x 1)))
    (is (= [[1 3]] (x 1.99999)))
    (is (= [[1 3] [2 4]] (x [1 2])))
    (is (= [[1 3] [2 4] [3 6]] (x [1 3])))
    (is (= [[5 9]] (x 7))))
  (let [y (interval-set (range 5))]
    (is (= [[0 0] [1 1] [2 2] [3 3] [4 4]] (seq y)))
    (is (= [[0 0] [1 1] [2 2] [3 3]] (y [0 3.1415926])))
    (is (= [[1 1] [2 2]] (y [1 2.5])))
    (is (= nil (y 1.5)))
    (is (= [[1 1]] (y 1)))
    (is (= [[2 2]] (y 2)))))

(defn random-segments [n]
  (->> n (* 2) range shuffle (partition-all 2) (map sort) (map vec)))

(defn random-interval-set [n]
  (-> n random-segments interval-set))

(deftest interval-set-lookup-check
  (doseq [size [10 100 1000 10000 100000]]
    (let [greatest (-> size (* 2) dec)
          x (random-interval-set size)]
      (is (= size (count x)))
      (is (= 0 (count (x -1))))
      (is (= 0 (count (x (inc greatest)))))
      (is (= 1 (count (x 0))))
      (is (= 1 (count (x 0.5))))
      (is (= 1 (count (x greatest))))
      (is (= 1 (count (x (- greatest 0.5)))))
      (is (= (seq x) (x [0 greatest])))
      (is (= size (count (x [0 greatest])))))))
