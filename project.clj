(defproject cqrs-event-sourcing-sandbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.taoensso/carmine "2.12.2"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[com.jakemccrary/lein-test-refresh "0.13.0"]]
                   :plugins [[com.jakemccrary/lein-test-refresh "0.12.0"]]}})
