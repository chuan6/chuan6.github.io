(ns generator.core
  (:require [clojure.java.io :refer [as-url]]
            [clojure.string :as str]
            [clojure.test :as t]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all])
  (:gen-class))

(def samples
  [{:title "Privacy Policy 「Buzz 声声」"
    :anchor "ios-app-buzz-privacy"
    :content ["The app does NOT collect any data off device."]}
   {:title "App Support 「Buzz 声声」"
    :anchor "ios-app-buzz"
    :content ["Hi,"
              "If you have any questions about the iOS app, please email at:"
              "chuan6 DOT dev AT qq DOT com"
              "Thank you!"
              "Best regards"]}
   {:title "歌词翻译：Daughters - John Mayer"
    :anchor "daughters-john-mayer-transcript"
    :content ["I know a girl"
              "我认识的一个女孩"
              "She puts the color inside of my world"
              "她让我的世界有了色彩"
              "But she's just like a maze"
              "可她又像是迷宫"
              "Where all of the walls all continually change"
              "一个所有隔墙都在不断挪动的迷宫"
              "And I've done all I can"
              "我已经尽力"
              "To stand on her steps with my heart in my hand"
              "提心吊胆摸索她给的暗示"
              "Now I'm starting to see"
              "我开始想通"
              "Maybe it's got nothing at all to do with me"
              "可能问题并不在自己身上"

              "Fathers be good to your daughters"
              "当爸的 善待自己的女儿"
              "Daughters will love like you do"
              "她们会模仿着你们的方式恋爱"
              "Girls become lovers who turn into mother"
              "女孩恋爱了 会成为母亲"
              "So mothers be good to your daughters too"
              "所以当妈的 也请善待你们的女儿"

              "Oh now I trust you've seen that skin"
              "你看她"
              "It's the same she's been standing in"
              "其实还是那个女孩"
              "Since the day she saw him walking away"
              "那个眼睁睁 看着他抛下父亲角色 的女孩"
              "Now I'm left cleaning up the mess he made"
              "现在轮到我 不得不填补他的缺失"

              "So fathers be good to your daughters"
              "所以当爸的 善待自己的女儿吧"
              "Daughters will love like you do"
              "她们会模仿着你们的方式恋爱"
              "Girls become lovers who turn into mothers"
              "而女孩恋爱了 会成为母亲"
              "So mothers be good to your daughters, too"
              "所以当妈的 也请善待你们的女儿"

              "Boys, you can break"
              "对男孩们 你可以不一样"
              "You'll find out how much they can take"
              "打击他们，能看他们能承受多少"
              "Boys will be strong"
              "他们从中学会坚强"
              "they'll soldier on"
              "不顾风雨"
              "But boys would be gone without warmth"
              "但他们的童心"
              "From a woman, from a, from a woman"
              "需要，需要由"
              "From a woman's good, good heart"
              "一位善良的姑娘守卫"

              "On behalf of every man"
              "就当是为了以后的每一个小伙"
              "Looking out for every girl"
              "照顾现在的每一个女孩"
              "You are the God and the weight of her world"
              "当爸的 你是她现在不可或缺的人"

              "On behalf of every man"
              "为了以后的每一个小伙"
              "Looking out for every girl"
              "照顾好现在的每一个女孩"
              "You are the God and the weight of her world"
              "当妈的 你是她现在最重要的人"

              "So fathers be good to your daughters"
              "所以当爸的 善待自己的女儿"
              "Daughters will love like you do"
              "她们会模仿着你们的方式恋爱"
              "Girls become lovers who turn into mothers"
              "而女孩恋爱了 会成为母亲"
              "So mothers be good to your daughters, too"
              "所以当妈的 也请善待你们的女儿"
              "So mothers be good to your daughters, too"
              "所以当妈的 也请善待你们的女儿"
              "So mothers be good to your daughters, too"
              "所以当妈的 也请善待你们的女儿"]}
   {:title "一个话题：集成化与模块化"
    :anchor "integration-modularization-wondering"
    :content ["之前在 Stratechery 上读到integration（集成化）/ modularization（模块化）的介绍，印象很深——产品的性能和功能，随市场竞争和用户需求的刺激而提升，往往能观察到前后两个阶段：不够用、够用了。"
              "在不够用的阶段，生产者需要拼命优化，将所有部分集成，方便自己灵活调整任何一个部分。而一旦够用了，用户则开始追求性能和功能之外的一些特性，比如：购买便捷、价格低廉、更新频繁、维修容易、替换简单等等。此时，模块化的行业形态往往更优。（类比到一个人自己做事的准则上就是，常有人说的，『要想做得快，自己做；要想做得久，一帮人做』。）"
              "另一个观察是，一个在不够用的阶段优胜的生产者，很难在该类产品转变到够用了的过程中避免自己被边缘化，甚至淘汰。就如法拉利是汽车技术突飞猛进时代的优胜者，在勒芒这样考验汽车性能和耐力的极限竞赛下脱颖而出，整车从铝材铸造开始完全自己搭建；但在随后几十年汽车技术止步不前而销量大增的时代里，只能蜷缩于超跑的圈子，没有收获与当初地位相符的汽车市场份额。当然人家可能不屑于市场份额，从一而终追求极致性能；但这正说明，阻拦不够用阶段优胜者适应接下来够用阶段的，价值观、企业文化、组织结构等等，都是因素。"
              "现在中国互联网大公司的追求基本上都是，无论市场和行业怎么变换，保持强大。这就要求，一方面不能在一个产品上吊死，另一方面，一个产品要能适应从不够用的阶段走进够用了的阶段，抓住原有的市场地位不放手。"
              "从生产方式的角度，就有了一个问题——什么时候选择集中化，而什么时候要切换到模块化？"
              "过早模块化而失败的例子，比如Google的Project Ara模块化手机。在集成狂魔 iPhone 都还没有满足用户对性能、续航、轻薄的追求时候，模块化手机带来的布局设计局限、模块接口维护负担，以及没有任何现存的模块供应商，这一系列问题带来了巨大成本，却没有显著实际价值能为之买单。"
              "作为一个工程师，能感知到自己每天倾心优化的产品，是处于哪个阶段吗？"
              "当工期由客户说了算，项目负责人由产品经理包办，价值交付相比于功能更在乎概念、文案；是否能得出结论，产品的性能和功能已经到了够用的阶段，该从集成化走向模块化？"]}
   {:title "网页打印 ：在文本左右放置内容编码"
    :anchor "web-page-print-content"
    :link "https://zhuanlan.zhihu.com/p/24398027"}
   {:title "法律文本富网页化"
    :link "https://github.com/chuan6/structured-law-document"
    :content ["将纯文本的法律条文自动转化为易阅览、跳转、分享的网页文档。"
              "已有的功能包括标记编、章、节、条、款、项、目，实现文本内精细跳转，以及方便的片段分享。并加入了完善的文档打印排版功能，生成网页可以轻松打印成册。"
              "使用Clojure语言以及前端相关的技术。"]
    :more-links {"structured-law-document" "页面展示"}}
   {:title "Tiger语言的编译器前端"
    :link "https://github.com/chuan6/tiger-compiler"
    :content ["实现了Tiger语言的编译器前端，包括词法器、simple LR语法生成器、抽象语法树转化、类型系统，以及相关用于上下文无关文法的一些函数。"
              "主要特点有，simple LR语法生成器能为任何属于simple LR的语法自动生成语法器（类似于yacc的作用）；完整实现了Tiger语言特性，包括递归函数声明、递归类型声明等；使用Clojure语言。"]}
   {:title "把玩浏览历史的Chrome插件：webXi"
    :link "https://github.com/chuan6/webXi"
    :content "制作了一个Chrome浏览器插件，webXi。可以帮助Chrome重度用户通过动态的操作流程，把玩、分析自己的网页浏览历史。"}
   {:title "M/M/c/K队列系统模拟库"
    :link "https://github.com/chuan6/mmck-simul-lib"
    :content ["设计并实现了M/M/c/K队列系统模拟器函数库。主要特点有，"
              "可以灵活模拟非简单FIFO的队列、非简单先空闲先上岗的服务单元，以及处理任务速率不同的服务单元；"
              "用户只需提供满足简单接口的队列结构或服务单元结构，便能利用库里的模拟算法；"
              "分别用Go语言和C++11语言实现了两个符合各自语言特点的版本；Go语言版本通过pprof进行性能调优，将随机数生成之外的计算成本降到极低，测试中达到与C++11版本通过gcc -o2优化编译所得到的性能。"]}
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
    :content ["历时约四个月，重构的内容包括从技术（HTML&CSS+JS），到排版布局，再到页面模块增删。期间，获得来自热心网友，Carol、 爱琴海蓝、老猫、水晶男孩等，以及BBS管理员，的持续反馈、帮助和建议。"
              "重构的技术需求来自由当时逐渐流行的Firefox和Chrome浏览器推进的标准Web技术逐步代替IE6的现实。未上线。"]
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
            (for [{:keys [title anchor link content more-links]} samples]
              [:div {:id (or anchor (id-by-link link))
                     :class "entry"}
               [:div {:class "title"}
                (if (nil? link)
                  [:span (tag-english-content title)]
                  [:a {:href link} (tag-english-content title)])]
               (when content
                 (let [ps (if (vector? content) content [content])
                       p (fn [paragraph] [:p (tag-english-content paragraph)])]
                   [:div
                    (map p ps)]))
               (when-let [links more-links]
                 [:ul
                  (for [[ref txt] links]
                    [:li
                     [:a {:href ref} txt]])])])]]))))

(-main)
