(ns nubank.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as json]
            [ring.util.response :refer [response]]))

(def grid (atom (vec (replicate 250 0))))

(defn robot [request]
  (response {:robot "bip"}))

(defn space [request]
  (response {:space "uon"}))

(defn matrix [request]
  (assoc grid 1 5)
  (response {:value (get grid 1 100)}))

(defroutes app-routes
  (GET "/robot" [] robot)
  (GET "/space" [] space)
  (GET "/matrix" [] matrix)
  (route/not-found "Not Found"))

(def app 
  (-> (handler/api app-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)))
