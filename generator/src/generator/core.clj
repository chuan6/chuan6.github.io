(ns generator.core
  (:require [clojure.string :as str]
            [clojure.test :as t]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all])
  (:gen-class))

(def sample-text
  "实现了Tiger语言的编译器前端，包括词法器、simple LR语法生成器、抽象语法树转化、类型系统，以及相关用于上下文无关文法的一些函数。主要特点有，simple LR语法生成器能为任何属于simple LR的语法自动生成语法器（类似于yacc的作用）；完整实现了Tiger语言特性，包括递归函数声明、递归类型声明等；使用Clojure语言。")

(defn- wrap-span [options s]
  (html [:span options s]))

(defn tag-english-content
  {:test
   #(let [hello (wrap-span {:lang "en"} "hello")
          world (wrap-span {:lang "en"} "world")]
      (every? true?
              [(t/is (= ""
                        (tag-english-content "")))
               (t/is (= hello
                        (tag-english-content "hello")))
               (t/is (= (str hello "世界")
                        (tag-english-content "hello世界")))
               (t/is (= (str "你好" hello "世界")
                        (tag-english-content "你好hello世界")))
               (t/is (= (str "你好" hello "世界" world)
                        (tag-english-content "你好hello世界world")))]))}
  [input-string]
  (letfn [(tag [cs] (wrap-span {:lang "en"} (str/join cs)))]
    (loop [cs input-string
           tv []
           en-buf []]
      (if (empty? cs)
        (str/join
         (if (empty? en-buf)
           tv
           (conj tv (tag en-buf))))
        (let [c (first cs)
              i (int c)]
          (cond (or (and (>= i (int \a)) (<= i (int \z)))
                    (and (>= i (int \A)) (<= i (int \Z)))
                    (and (seq en-buf) (Character/isWhitespace c)))
                (recur (rest cs) tv (conj en-buf c))

                (seq en-buf)
                (recur cs (conj tv (tag en-buf)) [])

                :else
                (recur (rest cs) (conj tv c) [])))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (spit "../index.html"
        (html
         (html5
          [:head {:lang "zh"}
           [:meta {:charset "utf-8"}]
           [:title "chuan6"]
           [:link {:rel "icon" :href "favicon.png"}]
           [:link {:rel "stylesheet" :href "index.css"}]]
          [:body
           [:div
            [:div
             [:a {:href "https://github.com/chuan6/tiger-compiler"}
              [:span {:lang "en"} "Tiger"]
              "语言的编译器前端"]]
            [:div
             [:span (tag-english-content sample-text)]]]]))))
