(ns todo-app.todo-model
  (:require [reagent.core :as r :refer [atom]]))

  (defonce model (atom {
                     :context "list" ;; context [list, add, edit, filtered]
                     :selected {:todo nil
                                :filter-done nil}
                     :todos {
                              :1 {:id 1
                                  :name "abc"
                                  :done? false
                                  }
                              :2 {:id 2
                                  :name "def"
                                  :done? false
                                  }
                              :3 {:id 3
                                  :name "qwe"
                                  :done? false
                                  }
                              }
                     }))


  (defn jsmodel []
    (clj->js @model))
