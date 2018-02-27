(ns mchan.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [mchan.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]
            [mchan.db :as db]
            [mchan.layout :as layout]
            [cheshire.core :refer :all]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))

(defn markdown-page []
  (layout/render "index.html"))

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    (generate-string data)})

(defn messages [board & [thread]]
  (if thread
    (json-response (db/by-thread board thread))
    (json-response (db/by-board board))))
 
(defroutes routes
  ; (GET "/" [] (loading-page))
  (GET "/" [] (markdown-page))
  (GET "/about" [] (loading-page))
  (GET "/board/:board" [] (loading-page))
  (GET "/messages/:board" [board] (messages board))
  (GET "/messages/:board/:thread" [board thread] (messages board thread))
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
