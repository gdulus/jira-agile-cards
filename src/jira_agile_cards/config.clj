(ns jira-agile-cards.config
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn ^:private load-config-file
  [path]
  (let [config (io/as-file path)]
    (if-not (.exists config)
      (throw (IllegalArgumentException. (string/join ["File not exists:" path])))
      (load-file path))))

(defn ^:private get-url
  [config endpoint]
  (string/join [(get-in config [:external :url]) (get-in config [:endpoints endpoint])]))

(defn load-config
  [path]
  {:endpoints {:rapidview "rest/greenhopper/1.0/rapidview"
               :sprintquery "rest/greenhopper/1.0/sprintquery/@0"
               :sprintreport "/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=@0&sprintId=@1"}
   :external (load-config-file path)})

(defn get-auth
  [config]
  (get-in config [:external :auth]))

(defn get-rapidview-url
  [config]
  (get-url config :rapidview))

(defn get-sprintquery-url
  [config rapidview-id]
  (string/replace (get-url config :sprintquery) #"@0" (str rapidview-id)))

(defn get-sprintreport-url
  [config rapid-view-id sprint-id]
  (string/replace (get-url config :sprintreport) #"@0|@1" {"@0" (str rapid-view-id) "@1" (str sprint-id)}))


