(ns generator.core
  (:require [clojure.java.io :refer [as-url]]
            [clojure.string :as str]
            [clojure.test :as t]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all])
  (:gen-class))

(def samples
  [{:title "《网页打印 ：在文本左右放置内容编码》"
    :link "https://zhuanlan.zhihu.com/p/24398027"}
   {:title "法律文本富网页化"
    :link "https://github.com/chuan6/structured-law-document"
    :content "将纯文本的法律条文自动转化为易阅览、跳转、分享的网页文档。已有的功能包括标记编、章、节、条、款、项、目，实现文本内精细跳转，以及方便的片段分享。并加入了完善的文档打印排版功能，生成网页可以轻松打印成册。使用Clojure语言以及前端相关的技术。"
    :more-links {"structured-law-document" "页面展示"}}
   {:title "Tiger语言的编译器前端"
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
    :more-links {"wusetu-bbs-regpage" "页面展示"}}
   {:title "中农大五色土BBS主页重构"
    :link "https://github.com/chuan6/wusetu-bbs-mainpage"
    :content "历时约四个月，重构的内容包括从技术（HTML&CSS+JS），到排版布局，再到页面模块增删。期间，获得来自热心网友，Carol、 爱琴海蓝、老猫、水晶男孩等，以及BBS管理员，的持续反馈、帮助和建议。重构的技术需求来自由当时逐渐流行的Firefox和Chrome浏览器推进的标准Web技术逐步代替IE6的现实。未上线。"
    :more-links {"wusetu-bbs-mainpage" "页面展示"}}])

(defn- wrap-span [options s]
  (html [:span options s]))

(defn tag-english-content
  {:test
   #(let [hello (wrap-span {:lang "en"} " hello ")
          world (wrap-span {:lang "en"} " world ")]
      (every? true?
              [(t/is (= ""
                        (tag-english-content "")))
               (t/is (= hello
                        (tag-english-content "hello")))
               (t/is (= (wrap-span {:lang "en"} " C++ ")
                        (tag-english-content "C++")))
               (t/is (= (wrap-span {:lang "en"} " 1+x ")
                        (tag-english-content "1+x")))
               (t/is (= (str hello "世界")
                        (tag-english-content "hello世界")))
               (t/is (= (str "你好" hello "世界")
                        (tag-english-content "你好hello世界")))
               (t/is (= (str "你好" hello "世界" world)
                        (tag-english-content "你好hello世界world")))]))}
  [input-string]
  (letfn [(space-before-after [cs]
            (str " " (str/join cs) " "))
          (tag [cs]
            (wrap-span {:lang "en"} (space-before-after cs)))]
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

(defn id-by-link
  {:test
   #(let [prefix (partial str "https://github.com/chuan6")]
      (every? true?
              [(t/is (= "" (id-by-link (prefix "/"))))
               (t/is (= "" (id-by-link (prefix "/?query"))))
               (t/is (= "webXi" (id-by-link (prefix "/webXi"))))]))}
  [link-str]
  (let [path (.getPath (as-url link-str))
        delimit-at (.lastIndexOf path (int \/))]
    (.substring path (inc delimit-at))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (spit "../index.html"
        (html
         (html5
          [:head {:lang "zh"}
           [:meta {:charset "utf-8"}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           [:title "chuan6"]
           [:link {:rel "icon" :href "favicon.png"}]
           [:link {:rel "stylesheet" :href "index.css"}]
           [:script {:src "main.js"}]]
          [:body
           [:div {:id "entries-container"}
            (for [{:keys [title link content more-links]} samples]
              [:div {:id (id-by-link link)
                     :class "entry"}
               [:div {:class "title"}
                [:a {:href link}
                 (tag-english-content title)]]
               (when content
                 [:div
                  [:p (tag-english-content content)]])
               (when-let [links more-links]
                 [:ul
                  (for [[ref txt] links]
                    [:li
                     [:a {:href ref} txt]])])])]]))))

(-main)
