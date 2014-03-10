(ns jira-agile-cards.api
  (:require [jira-agile-cards.config :as config]
            [clj-http.client :as client]
            [clj-json.core :as json]
            [jira-agile-cards.utils :as utils]))

(defn ^:private query-api
  [url auth]
  (json/parse-string (:body (client/get url {:basic-auth auth}))))

(defn ^:private get-id
  [config url-accessor main-key element-name]
  (let [response (query-api (url-accessor) (config/get-auth config))]
    ((first (filter #(= (% "name") element-name) (response main-key))) "id")))

(defn ^:private get-rapidview-id
  [config rapidview-name]
  {:pre [(utils/log "retrieving rapidview-id for rapidview-name = " rapidview-name)]
   :post [(utils/log "done, rapidview-id = " %)]}
  (get-id config #(config/get-rapidview-url config) "views" rapidview-name))

(defn ^:private get-sprint-id
  [config rapidview-id sprint-name]
  {:pre [(utils/log "retrieving sprint-id for sprint-name = " sprint-name)]
   :post [(utils/log "done, sprint-id = " %)]}
  (get-id config #(config/get-sprintquery-url config rapidview-id) "sprints" sprint-name))

(defn ^:private get-sprint-report
  [config rapidview-id sprint-id]
  {:pre [(utils/log "retrieving tickets for rapidview-id = " rapidview-id " and sprint-id = " sprint-id)]}
  (let [response (query-api (config/get-sprintreport-url config rapidview-id sprint-id) (config/get-auth config))]
    (get-in response ["contents" "completedIssues"])))

(defn get-issues
  [config rapidview-name sprint-name]
  (let [rapidview-id (get-rapidview-id config rapidview-name)
        sprint-id (get-sprint-id config rapidview-id sprint-name)]
    (get-sprint-report config rapidview-id sprint-id)))

