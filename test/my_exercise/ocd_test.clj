(ns my-exercise.ocd-test
  (:require  [clojure.test :refer [deftest testing is are]]
             [clojure.string :as string]
             [my-exercise.ocd :as ocd]))

;; example:
;; curl 'https://api.turbovote.org/elections/upcoming?district-divisions=ocd-division/country:us/state:or,ocd-division/country:us/state:or/place:portland'

(deftest string-cleaner
  (testing "various inputs"
    (are [in out] (= out (ocd/string-cleaner in))
      "" nil
      "abc" "abc"
      "abc def" "abc_def"
      "Abc!de()F" "Abc_de__F")))

(deftest render-ocd-ids
  (testing "with city"
    (let [params {:state "FL"
                  :city "West Miami"}]
      (is (= "ocd-division/country:us/state:fl/place:west_miami" (ocd/ocd-id params)))))
  (testing "without city"
    (let [params {:state "FL"}]
      (is (= "ocd-division/country:us/state:fl" (ocd/ocd-id params)))))
  (testing "empty string"
    (let [params {:state "FL"
                  :city ""}]
      (is (= "ocd-division/country:us/state:fl" (ocd/ocd-id params)))))
  (testing "both state and city"
    (let [params {:state "FL"
                  :city "West Miami"}]
      (is (= "ocd-division/country:us/state:fl,ocd-division/country:us/state:fl/place:west_miami"
             (ocd/ocd-multiple params))))))


(deftest elections
  (testing "make a request"
    (let [place-params {:state "FL"
                        :city "West Miami"}]
      ;; blocking
      ;; TODO: use example in test/resources
      #_(is (= '() (ocd/request-elections place-params))))))
