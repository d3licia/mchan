(ns mchan.utils
  (:require [cljs-time.format :refer [parse unparse formatter]]))

(defn format-date [str]
  (let [date (parse str)
        format (formatter "dd/MM/yy (E) HH:mm:ss")]
    (unparse format date)))