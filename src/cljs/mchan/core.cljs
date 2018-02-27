(ns mchan.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [ajax.core :refer [GET]]
              [mchan.utils :refer [format-date]]))

;; -------------------------
;; Views

(def messages (atom []))

(defn messages-handler [resp]
  (reset! messages resp))

(defn message-view [message]
  [:div.message
   [:div.head
    [:span.subject (:suject message)]
    [:span.author "Anônimo"]
    [:span (format-date (:timestamp message))]
    [:span "No." (:id message)]
    [:a  {:href (str "/thread/" (:board message) "/" (:id message))} "[View]"]]
   [:div.body (:content message)]])

(defn home-page []
  [:div [:h2 "Welcome to mchan"]])

(defn catalog-page []
  [:div [:a {:href "/about"} "About"]
   [:div (for [message @messages]
                    [message-view message])]])

(defn thread-page []
  [:div [:a {:href "/"} "Back"]
   [:div (for [message @messages]
                    [message-view message])]])

(defn about-page []
  [:div [:h2 "About mchan"]
   [:div [:a {:href "/"} "go to the home page"]]])

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])


(secretary/defroute "/board/:board" {:as params}
  (do 
    (go (GET (str "/messages/" (:board params)) {:handler messages-handler :response-format :json :keywords? true}))
    (reset! page #'catalog-page)))

(secretary/defroute "/thread/:board/:id" {:as params}
  (do 
    (go (GET (str "/messages/" (:board params) "/" (:id params)) {:handler messages-handler :response-format :json :keywords? true}))
    (reset! page #'thread-page)))

(secretary/defroute "/about" []
  (reset! page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
