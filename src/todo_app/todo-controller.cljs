(ns todo-app.todo-controller
  (:require
    [todo-app.todo-model :as m :refer [model]]
    ))

  ;;model swap
  (defn swapm! [new-value model-atom]
    (swap! model-atom (fn [x] (assoc new-value :old-value @model-atom)))
    new-value)


  (defn filter-todos [selected-filter]
;;     (print "filter-todos" @model selected-filter)
    (-> @model
        (assoc :context (if (nil? selected-filter) "list" "filtered"))
        (assoc-in [:selected :filter-done] selected-filter)
        (swapm! model)
        ))


  (defn add-todo [todo-name]
;;     (print "add-name" todo-name)
    (let [new-id (inc (count (:todos @model)))]
      (-> @model
          (assoc-in [:todos (keyword (str new-id))] {:id new-id :name todo-name :done? false})
          (swapm! model))))

  (defn edit-todo [todo-id]
    (-> @model
        (assoc :context "edit")
        (assoc-in [:selected :todo] (get-in @model [:todos (keyword (str todo-id))]))
        (swapm! model)
        ))


  ;;edit helper
  (defn save-todo []
    (let [todo (get-in @model [:selected :todo])]
      (-> @model
          (assoc :context "list")
          (assoc-in [:todos (keyword (str (:id todo)))] todo)
          (assoc-in [:selected :todo] nil)
          (swapm! model))
      ))

  ;;edit helper
  (defn change-property [property value]
    (let [todo (get-in @model [:selected :todo])]
      (-> @model
          (assoc-in [:selected :todo (keyword property)] value)
          (swapm! model))
      ))


  (defn delete-todo [todo-id]
    (-> @model
        (update-in [:todos] (fn delete-todo [all-todos] (dissoc all-todos (keyword (str todo-id)))))
        (swapm! model))
    )


  (defn undo []
;;     (print "undo")
    (-> @model
        ((fn return-old-value [state]
           (:old-value state)))
        (swapm! model))
    )

  (defn on-change []
    (print "a")
    )


  (defn toggle-todo [todo-id]
    ((edit-todo todo-id)
     (let [todo-done (get-in @model [:selected :todo :done?])]
;;        (print "todo-done" todo-done)
       (if (= todo-done true)
         (change-property :done? false)
         ;;else
         (change-property :done? true)
         ))
     (save-todo))
    )


  (defn select-all []
    (let [done (-> @model :todos vals first :done? not)]
      (-> @model
          (update :todos #(into {} (map (fn [[k v]] [k (assoc v :done? done)]) %)))
          (swapm! model))
      ))



