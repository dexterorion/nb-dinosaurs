(ns nubank.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as json]
            [ring.util.response :refer [response]]))

(def tbsize (atom 50))

(def dinosaurs (atom {}))

(def robots (atom {}))

; (def find-x [request]
;   (print (get-in request[:body]))
;   (+ (/ pos @tbsize) 1))

; (def find-y [request]
;   (print (get-in request[:body]))
;   (rem pos @tbsize) 1)

; (def find-pos [request]
;   (print (get-in request[:body]))
;   (+ (* (- x 1) @tbsize) y))

(defn onlypost [req]
    (print "aaa"))

(def grid (atom nil))

; (defn robot [request]
;   (response {:robot "bip"}))

; (defn matrix [request]
;   (swap! grid assoc 1 5)
;   (response {:value (get @grid 1)}))

; (defn reset-space [request] 
;   (reset! grid (vec (replicate 250 0)))
;   (response (apply str @grid)))

; (defn show-space [request] 
;   (response (apply str @grid)))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(defn create-dinosaur [request] (
      let [position (or (get-in request [:body :position]) 1)
          id (uuid)]
      (swap! dinosaurs assoc id {:position position})
      (response @dinosaurs)))

(defn create-robot [request] (
      let [position (or (get-in request [:body :position]) 1)
          id (uuid)
          direction (or (get-in request [:body :direction]) 1)]
      (swap! robots assoc id {:position position :direction direction})
      (response @robots)))

(defroutes app-routes
  (POST "/dinosaur" request create-dinosaur)
  (POST "/robot" request create-robot)
  (POST "/" request
    (let [name (or (get-in request [:params :name])
                   (get-in request [:body :name])
                   "John Doe")]
      {:status 200
       :body {:name name
       :desc (str "The name you sent to me was " name)}}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app 
  (-> (handler/api app-routes)
      (json/wrap-json-body {:keywords? true})
      (json/wrap-json-params)
      (json/wrap-json-response)))
