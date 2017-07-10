(ns todo-app.todo-controller
  (:require
    [todo-app.todo-model :as m :refer [model]]))


  ;;model swap
(defn swapm! [new-value model-atom]
  (swap! model-atom (fn [x] (assoc new-value :old-value @model-atom))))


(defn todos-for-display [display todos]
  (case display
    :all (vals todos)
    :completed (filter :done? (vals todos))
    :active (filter (comp not :done?) (vals todos))))

(defn set-active-display [display]
  (-> @model
      (assoc :display display)
      (swapm! model)))


(defn- get-new-id [todos]
  (let [xform (comp (map second)
                    (map :id)
                    (map inc))]
    (transduce xform max 0 todos)))


(defn- new-todo [id name]
  {:id id
   :name name
   :done? false
   :editing? false})

(defn add-todo [todo-name]
  (let [new-id (get-new-id (:todos @model))]
    (-> @model
        (assoc-in [:todos new-id] (new-todo new-id todo-name))
        (swapm! model))))


(defn delete-todo [todo-id]
  (-> @model
      (update-in [:todos] dissoc todo-id)
      (swapm! model)))


(defn todo-edit-state [state]
  (fn [id]
    (-> @model
      (assoc-in [:todos id :editing?] state)
      (swapm! model))))

(def edit-todo (todo-edit-state true))
(def stop-edit-todo (todo-edit-state false))


(defn update-todo [id name]
  (-> @model
    (assoc-in [:todos id :name] name)
    (swapm! model)))



(defn undo []
  (-> @model
      :old-value
      (swapm! model)))


(defn toggle-todo [todo-id]
  (-> @model
    (update-in [:todos todo-id :done?] not)
    (swapm! model)))


(defn toggle-all [done]
  (-> @model
      (update :todos #(into {} (map (fn [[k v]] [k (assoc v :done? done)]) %)))
      (swapm! model)))
