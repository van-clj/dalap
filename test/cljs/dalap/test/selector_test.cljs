;; This file was generated with dalap-cljsbuild from
;;
;; test/clj/dalap/test/selector_test.clj @ Tue Nov 13 07:51:19 UTC 2012
;;
(ns dalap.test.selector-test (:require [buster-cljs.core :refer [is]] [dalap.selector :refer [-gen-decorator gen-visitor]] [dalap.walk :refer [walk]]) (:require-macros [buster-cljs.macros :refer [deftest it]]))
(defrecord CustomType [a b])
(defrecord CustomType2 [a b])
(defrecord CustomType3 [a b])
(deftype CustomType4 [a b])
(defn visit-clj-form [form w] (letfn [(filter-map [f form] (remove (fn* [p1__5450#] (= p1__5450# (keyword "dalap/form"))) (map f form)))] (cond (list? form) (apply list (filter-map w form)) (seq? form) (doall (filter-map w form)) (coll? form) (into (empty form) (filter-map w form)) :else form)))
(defn assert-walk [visitor input expected & [msg]] (let [result (walk input visitor)] (is (= result expected) (str (and msg (str msg " -- ")) "expected: " (pr-str expected) " -- " "got: " (pr-str result)))))
(deftest test-walk-with-no-rules (it "without any rules on visit" (let [sample-form (quote (let [hello "hola"] (str hello))) visitor (gen-visitor [] visit-clj-form)] (assert-walk visitor sample-form sample-form "should be same value"))))
(deftest test-symbol-as-a-selector (it "with symbol as a selector on rules" (let [transform-rules [(quote hello) (quote hallo)] visitor (gen-visitor transform-rules visit-clj-form)] (assert-walk visitor (quote (let [hello "hola"] (str hello))) (quote (let [hallo "hola"] (str hallo)))))))
(deftest test-function-as-a-selector (it "with functions as a selector on rules" (let [selector-fn (fn [o w] (vector? o)) replacement-value "Something Else" transform-rules [selector-fn replacement-value] visitor (gen-visitor transform-rules visit-clj-form)] (assert-walk visitor (clojure.core/seq (clojure.core/concat (clojure.core/list (clojure.core/apply clojure.core/vector (clojure.core/seq (clojure.core/concat (clojure.core/list "uno") (clojure.core/list 2))))) (clojure.core/list (clojure.core/seq (clojure.core/concat (clojure.core/list (quote user/foobar))))))) (clojure.core/seq (clojure.core/concat (clojure.core/list replacement-value) (clojure.core/list (clojure.core/seq (clojure.core/concat (clojure.core/list (quote user/foobar)))))))))))
(deftest test-walking-over-a-set (it "with set as the collection we are visiting" (let [selector (quote foo) replacement-value 999 transform-rules [selector replacement-value] visitor (gen-visitor transform-rules visit-clj-form)] (assert-walk visitor #{(quote foo) (quote hello)} #{999 (quote hello)} "visitor should be able to walk on sets"))))
(deftest test-vector-as-a-selector (it "with vectors as selector on rules" (let [selector [(fn [n _] (vector? n)) (quote foobar)] replacement-value (quote replacement) transform-rules [selector replacement-value] visitor (gen-visitor transform-rules visit-clj-form)] (assert-walk visitor [1 2 [(quote foobar)] "other value"] [1 2 [(quote replacement)] "other value"]))))