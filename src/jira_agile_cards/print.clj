(ns jira-agile-cards.print
  (:require [clj-pdf.core]))

(import 'org.apache.commons.lang.StringUtils)

(defn ^:private generate-cell
  [api-issue]
    [:cell {:border true :color [51 204 51]}
      [:paragraph
       [:heading {:styles [:underline :bold]} (api-issue "key")]
       [:chunk {:styles [:italic]} (StringUtils/abbreviate (api-issue "summary") 60)]
       [:spacer]]])

(defn ^:private generate-cells
  [api-response]
  (map generate-cell api-response))

(defn ^:private get-rows
  [rows]
  (for [row rows]
    [(first row) (last row)]))

(defn ^:private get-table
  [api-response]
  (let [rows (partition-all 2 (generate-cells api-response))]
    (concat [:table {:width 100 :border true :cell-border true}]
      (get-rows rows))))

(defn print-pdf
  [api-response target]
  (pdf [{:pages true
         :size :a4
         :left-margin   10
         :right-margin  10
         :top-margin    20
         :bottom-margin 25}
        (get-table api-response)]
       target))
