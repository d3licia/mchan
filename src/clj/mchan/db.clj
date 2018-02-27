(ns mchan.db
  (:require [clojure.string :as str])
  (:use [korma.db]
  		[korma.core]))

(defdb db (postgres {:db "mlab_development"
                     :user "mlab"
                     :password "mlab"}))

(defentity messages)

(defn by-board [name]
  (select messages
          (where {:board name :op true})
          (limit 50)))

(defn by-thread [board id]
  (select messages
          (where {:board board :thread (read-string id)})
          (order :timestamp)))

