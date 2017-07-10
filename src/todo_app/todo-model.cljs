(ns todo-app.todo-model
  (:require [reagent.core :as r :refer [atom]]))

(defonce model (atom {
                      :context "list" ;; context [list, add, edit, filtered]
                      :display :all ;; #{:all :active :completed}
                      :selected {:todo nil
                                 :filter-done nil}
                      :todos {
                               1 {:id 1
                                  :name "abc"
                                  :done? false
                                  :editing? false}

                               2 {:id 2
                                  :name "def"
                                  :done? false
                                  :editing? false}

                               3 {:id 3
                                  :name "qwe"
                                  :done? false
                                  :editing? false}}}))




(defn jsmodel []
  (clj->js @model))
