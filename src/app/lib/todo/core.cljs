(ns app.lib.todo.core
  (:require ["@prisma/client" :refer [PrismaClient]]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

(def prisma (new PrismaClient))

(defn create [todo]
  "Creates a to-do item using with default state as TODO"
  (go
    (let [js-todo (<p!
                    (.. prisma -todo (create (clj->js {:data
                                                       {:value (:value todo)
                                                        :state "TODO"}}))))
          item (js->clj js-todo :keywordize-keys true)]
      item)))

(defn all []
  (go
    (let [js-list (<p! (.. prisma -todo (findMany #js {})))
          items (js->clj js-list :keywordize-keys true)]
      items)))

(defn remove-by-id [id]
  (.. prisma -todo (delete (clj->js {:where
                                      {:id id}}))))

(defn remove-all []
  (.. prisma -todo (deleteMany #js {})))

(defn toggle-done [todo]
  (when (= "TODO" (:state todo))
    (assoc todo :state "DONE")))
