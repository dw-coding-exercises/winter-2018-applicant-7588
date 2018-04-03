(ns my-exercise.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [my-exercise.ocd :as ocd]
            [my-exercise.home :as home]
            ))



(defn handle-search
  "adds a :results map with :elections from api.turbovote.org to the request for
  rendering later"
  [req]
  (let [{:keys [:street :street-2 :city :state :zip] :as p} (:params req)
        elections (ocd/request-elections p)
        results {:elections elections
                 :count (count elections)}]
    (assoc req :results results)))

(defroutes app
  (GET "/" [] home/page)
  (GET "/search" _ (home/search-results {}))
  (POST "/search" req #(home/search-results (handle-search %)))
  (route/resources "/")
  (route/not-found "Not found"))

(def handler
  (-> app
      (wrap-defaults site-defaults)
      wrap-reload))
