(ns todo-app.todo-view
  (:require
    [reagent.core :as r :refer [atom]]
    [todo-app.todo-controller :as controller]
    ))


(defn main-screen-component [state]
  [:section.todoapp
   [:div
    [:header.header
     [:h1 "todos"]
     [:input.new-todo {:placeholder "What needs to be done?"
                       :on-key-press (fn [e]
                                       (if (= 13 (.-charCode e))
                                         (do
                                           (controller/add-todo (-> e .-target .-value))
                                           (set! (-> e .-target .-value) "")
                                           )))}]
     ]
    [:section.main
     [:input.toggle-all {:type "checkbox"
                         :on-click #(controller/select-all )}]
     [:ul.todo-list
      (let [todos (if (-> state :selected :filter-done nil?)
                    (-> state :todos vals)
                    ;;else
                    (filter (fn [todo] (= (get-in state [:selected :filter-done]) (:done? todo))) (-> state :todos vals))
                    )
            ]
        (for [todo todos]
          ^{:key (:id todo)}
          [(if (:done? todo)
            :li.completed
             ;;else
            :li)
           [:div.view
            [:input.toggle {:type "checkbox"
                            :on-change #(controller/toggle-todo (:id todo))}]
            [:label {:on-click (fn [e]
                                 (print (-> e))
                                 )}
             (:name todo)]
            [:button.destroy {:on-click #(controller/delete-todo (:id todo))}]
            ]
           [:input.edit {:value (:name todo)}]]))
      ]
     ]
    [:footer.footer
     [:span.todo-count ]
     [:ul.filters
      [:li [:a {:on-click #(controller/filter-todos nil)} "All"]]
      [:span ]
      [:li [:a {:on-click #(controller/filter-todos false)} "Active"]]
      [:span ]
      [:li [:a {:on-click #(controller/filter-todos true)} "Completed"]]
      [:span ]
      ]

     ]]])
