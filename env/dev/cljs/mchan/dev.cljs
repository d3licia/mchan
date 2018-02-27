(ns ^:figwheel-no-load mchan.dev
  (:require
    [mchan.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
