(ns todo-app.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [todo-app.todo-model :as m :refer [model]]
    [todo-app.todo-view :as view :refer [main-screen-component]]
    ))

(enable-console-print!)

(println "This text is printed from src/todo-app/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload


(defn app []
  [main-screen-component @model]
  )

(reagent/render-component [app]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
