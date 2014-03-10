(ns jira-agile-cards.core
  (:require [jira-agile-cards.config :as config]
            [jira-agile-cards.api :as api]
            [jira-agile-cards.print :as print]))

(print/print-pdf
 (api/get-issues (config/load-config "conf.clj") "Development" "Sprint 22")
 "tickets.pdf")


;(defn -main
;  "I don't do a whole lot."
;  [x]
;  (println x "Hello, World!"))
