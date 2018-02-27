(ns mchan.utils-test
  (:require [mchan.utils :refer [format-date]]
            [cljs.test :refer-macros [deftest is testing run-tests]]))

(deftest test-format-date
  (let [date "20171107T131016Z"
        formated "07/11/17 (Tue) 13:10:16"]
        (is (= (format-date date) formated))))