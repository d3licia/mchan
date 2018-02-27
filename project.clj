(defproject mchan "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[cheshire "5.8.0"]
                 [cljs-ajax "0.7.2"]
                 [com.andrewmcveigh/cljs-time "0.5.0"]
                 [compojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [korma "0.4.3"]
                 [lein-doo "0.1.8"]
                 [markdown-clj "1.0.1"]
                 [metosin/ring-http-response "0.9.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"
                  :scope "provided"]
                 [org.postgresql/postgresql "9.4-1206-jdbc4"]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [ring "1.6.2"]
                 [ring-server "0.5.0"]
                 [ring/ring-defaults "0.3.1"]
                 [secretary "1.2.3"]
                 [selmer "1.11.3"]
                 [venantius/accountant "0.2.0"
                  :exclusions [org.clojure/tools.reader]]
                 [yogthos/config "0.9"]]

  :plugins [[lein-environ "1.0.2"]
            [lein-cljsbuild "1.1.5"]
            [lein-doo "0.1.8"]
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]
            [lein-cljfmt "0.5.7"]]

  :sass {:target-path "resources/public/css" :source-paths "resources/public/css"}

  :ring {:handler mchan.handler/app
         :uberwar-name "mchan.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "mchan.jar"

  :main mchan.server

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to        "target/cljsbuild/public/js/app.js"
              :output-dir       "target/cljsbuild/public/js"
              :source-map       "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:on-jsload "mchan.core/mount-root"}
             :compiler
             {:main "mchan.dev"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}
            :test
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs" "test/cljs" "test/cljc"]
             :compiler
             {:main runners.doo
              :optimizations :none
              :output-to "target/cljsbuild/test/public/js/app.js"
              :output-dir "target/cljsbuild/test/public/js/out"}}}}

  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
   :css-dirs ["resources/public/css"]
   :ring-handler mchan.handler/app}

  :profiles
  {:dev
   {:repl-options {:init-ns mchan.repl
                   :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

    :dependencies [[binaryage/devtools "0.9.7"]
                   [ring/ring-mock "0.3.1"]
                   [ring/ring-devel "1.6.2"]
                   [prone "1.1.4"]
                   [figwheel-sidecar "0.5.14"]
                   [org.clojure/tools.nrepl "0.2.13"]
                   [com.cemerick/piggieback "0.2.2"]
                   [pjstadig/humane-test-output "0.8.3"]
                   [com.andrewmcveigh/cljs-time "0.5.0"]]

    :source-paths ["env/dev/clj"]
    :plugins [[lein-figwheel "0.5.14"]]

    :injections [(require 'pjstadig.humane-test-output)
                 (pjstadig.humane-test-output/activate!)]

    :env {:dev true}}

   :uberjar {:hooks [minify-assets.plugin/hooks]
             :source-paths ["env/prod/clj"]
             :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
             :env {:production true}
             :aot :all
             :omit-source true}})