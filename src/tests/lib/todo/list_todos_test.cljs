(ns tests.lib.todo.list-todos-test
  (:require [app.lib.todo.core :as todo]
            [cljs.test :refer [async deftest testing is use-fixtures]]
            [cljs.core.async :refer [go] :include-macros true]
            [cljs.core.async.interop :refer-macros [<p!]]))

(use-fixtures :each
  {:before
   #(async done
           (go
             (<p! (todo/remove-all))
             (done)))
   })

(deftest empty-todos
  (testing "Should get a empty list of todos"
    (async done
           (go
             (let [result (<! (todo/all))]
               (is (= [] result))
               (done))))))

(deftest list-one-todo
  (testing "Should create one todo"
    (async done
           (go
             (let [todo (<! (todo/create {:value "todo text here"}))
                   todos (<! (todo/all))]
               (is (= "todo text here" (:value todo)))
               (is (= "TODO" (:state todo)))
               (is (= todo (get todos 0)))
               (is (= 1 (count todos)))
               (done))))))
