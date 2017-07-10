(ns todo-app.todo-view
  (:require
    [reagent.core :as reagent]
    [todo-app.todo-controller :as controller]
    [todo-app.todo-model :as m]))


(defn todo-edit-box [todo]
  (let [input (atom nil)]
    (reagent/create-class
      {:reagent-render
        (fn [todo] [:input.edit {:ref #(reset! input %)
                                 :value (:name todo)
                                 :on-key-press #(when (= 13 (.-charCode %))
                                                    (controller/stop-edit-todo (:id todo)))
                                 :on-change #(controller/update-todo (:id todo) (-> % .-target .-value))
                                 :on-blur #(controller/stop-edit-todo (:id todo))}])
                                 
       :component-did-mount #(some-> @input .focus)
       :component-will-unmount #(reset! input nil)})))





(defn main-screen-component [state]
  [:section.todoapp
   [:div
    [:header.header
     [:h1 "todos"]
     [:div.header-div
      [:input.new-todo {:placeholder "What needs to be done?"
                        :on-key-press (fn [e]
                                        (if (= 13 (.-charCode e))
                                          (do
                                            (controller/add-todo (-> e .-target .-value))
                                            (set! (-> e .-target .-value) ""))))}]]]




    [:section.main
     [:input.toggle-all {:type "checkbox"
                         :on-click #(controller/toggle-all (-> % .-target .-checked))}]
     [:ul.todo-list
      (let [todos (controller/todos-for-display (:display @m/model) (:todos @m/model))]

        (for [todo todos]
          ^{:key (:id todo)}
          [:li {:class (str (when (:done? todo) "completed") " " (when (:editing? todo) "editing"))}
            (if-not (:editing? todo)
              [:div.view
                [:input.toggle {:type "checkbox"
                                :on-change #(controller/toggle-todo (:id todo))}]
                [:label {:on-double-click #(controller/edit-todo (:id todo))} (:name todo)]
                [:button.destroy {:on-click #(controller/delete-todo (:id todo))}]]
              [todo-edit-box todo])]))]]





    [:footer.footer
     [:div
      {
        :class "todo-count undo-redo"
        :on-click controller/undo}]
     (let [active-display (:display @m/model)]
       [:ul.filters
        [:li [:a {:class (when (= :all active-display) "selected") :on-click #(controller/set-active-display :all)} "All"]]
        [:span]
        [:li [:a {:class (when (= :active active-display) "selected") :on-click #(controller/set-active-display :active)} "Active"]]
        [:span]
        [:li [:a {:class (when (= :completed active-display) "selected") :on-click #(controller/set-active-display :completed)} "Completed"]]
        [:span]])]]])
