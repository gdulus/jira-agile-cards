(ns jira-agile-cards.utils
   (:require [clojure.string :as string]))

(defn log
  [& args]
  (do
    (println (string/join args))
    true))
