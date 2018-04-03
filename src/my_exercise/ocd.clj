(ns my-exercise.ocd
  (:require [clojure.string :as string]))

(def turbovote "https://api.turbovote.org/elections/upcoming")

(defn string-cleaner [s]
  (if (empty? s)
    nil
    ;; going out on a limb here with full underscore replacement:
    ;; TODO: look up ocd definition for this
    (string/replace s #"[^a-zA-Z]" "_")))

(defn ocd-id [m]
  (let [cleaned (into {} (map (fn [[k v]] [k (string-cleaner v)]) m))
        {:keys [street street-2 city state zip country] :as m
         :or {country "us"}} cleaned]
    ;; TODO: street? zip code? validatation? multiple ocd-ids?
    (string/lower-case
     (cond-> "ocd-division"
       country
       (str "/country:" country)
       state
       (str "/state:" state)
       city
       (str "/place:" (string/replace city " " "_"))))))

(defn ocd-multiple
  "forms a query parameter with two ocd-ids one for the state, and one for the city"
  [m]
  (string/join ","
               [(ocd-id (dissoc m :city))
                (ocd-id m)]))

(defn turbourl [s]
  (str turbovote "?" "district-divisions=" s))


;; TODO: make non-blocking and more testable
(defn request-elections [params]
  (-> (ocd-multiple params)
      turbourl
      slurp
      read-string))
