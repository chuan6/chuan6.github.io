(ns generator.core
  (:require [clojure.string :as str]
            [clojure.test :as t]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all])
  (:gen-class))

(def samples
  [{:title "Tiger语言的编译器前端"
    :link "https://github.com/chuan6/tiger-compiler"
    :content "实现了Tiger语言的编译器前端，包括词法器、simple LR语法生成器、抽象语法树转化、类型系统，以及相关用于上下文无关文法的一些函数。主要特点有，simple LR语法生成器能为任何属于simple LR的语法自动生成语法器（类似于yacc的作用）；完整实现了Tiger语言特性，包括递归函数声明、递归类型声明等；使用Clojure语言。"}
   {:title "把玩浏览历史的Chrome插件：webXi"
    :link "https://github.com/chuan6/webXi"
    :content "制作了一个Chrome浏览器插件，webXi。可以帮助Chrome重度用户通过动态的操作流程，把玩、分析自己的网页浏览历史。"}
   {:title "M/M/c/K队列系统模拟库"
    :link "https://github.com/chuan6/mmck-simul-lib"
    :content "设计并实现了M/M/c/K队列系统模拟器函数库。主要特点有，可以灵活模拟非简单FIFO的队列、非简单先空闲先上岗的服务单元，以及处理任务速率不同的服务单元；用户只需提供满足简单接口的队列结构或服务单元结构，便能利用库里的模拟算法；分别用Go语言和C++11语言实现了两个符合各自语言特点的版本；Go语言版本通过pprof进行性能调优，将随机数生成之外的计算成本降到极低，测试中达到与C++11版本通过gcc -o2优化编译所得到的性能。"}
   {:title "LC-3的单文件汇编器"
    :link "https://github.com/chuan6/LC-3-assembler"
    :content "实现了LC-3汇编语言的单文件汇编器。主要特点有，为了适用于教育环境，提供充分的报错信息；使用Clojure语言。"}
   {:title "一个基于Linux系统的简单shell"
    :link "https://github.com/chuan6/toyshell"
    :content "实现了多管道式命令，输入输出重定向，以及aliasing（命令别名定义机制）。"}
   {:title "中农大五色土BBS注册页重构"
    :link "https://github.com/chuan6/wusetu-bbs-regpage"
    :content "重构的背景是在2009年左右，同学中越来越多的人开始使用Firefox和Chrome作为主力浏览器，代替IE6。 而原页面存在着的一些与HTML标准不太符合的实现，在当时刚开始学习网页技术的我看来，可以成为重构的对象。未上线。"
    :more-links {"wusetu-bbs-regpage/reg.html" "页面展示"}}])

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
               (t/is (= (wrap-span {:lang "en"} "C++")
                        (tag-english-content "C++")))
               (t/is (= (wrap-span {:lang "en"} "1+x")
                        (tag-english-content "1+x")))
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
          (cond (or (and (>= i (int \!)) (<= i (int \~)))
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
           (for [entry samples]
             [:div {:class "entry"}
              [:div {:class "title"}
               [:a {:href (:link entry)}
                (tag-english-content (:title entry))]]
              [:div
               [:p (tag-english-content (:content entry))]]
              (when-let [links (:more-links entry)]
                [:ul
                 (for [[ref txt] links]
                   [:li
                    [:a {:href ref} txt]])])])]))))

(-main)
