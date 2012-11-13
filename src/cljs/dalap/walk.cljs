;; This file was generated with dalap-cljsbuild from
;;
;; src/clj/dalap/walk.clj @ Tue Nov 13 19:41:22 UTC 2012
;;
(ns dalap.walk)
(defprotocol IWalkerState "API: Public\n\n  The IWalkerState is used to add a state interface to a\n  Walker type. Initially this protocol is being used only on\n  dalap.walk/Walker; if you need to implement your own walker\n  and you want to hold a state on this walker, you should use this\n  protocol to do so." (get-state [this] "Returns the state of a walker") (update-state [this update-fn] "Updates the state of a walker using `update-fn`") (update-in-state [this keys fn] "Updates a value in the state map pointed by `keys`, it\n      uses the `fn` function to transform the value.") (conj-state [this new-state] "Conjs a value into the state map."))
(deftype Walker [visitor state-map] IFn (invoke [this x] (visitor x this)) IWalkerState (get-state [this] state-map) (conj-state [this new-state] (Walker. visitor (conj state-map new-state))) (update-state [this update-fn] (Walker. visitor (update-fn state-map))) (update-in-state [this ks fn] (let [keys (if (sequential? ks) ks [ks])] (Walker. visitor (update-in state-map keys fn)))) ILookup (-lookup [this key] (state-map key)) (-lookup [this key not-found] (state-map key not-found)))
(defn -gen-walker "Signature: (([^Object ^dalap.walk/Walker] -> Output) -> dalap.walk/Walker)\n\n  Builds a `dalap.walk/Walker` instance using the provided visitor\n  function.  If a state-map is provided it would use it as the walker\n  state-map, otherwise an empty map is used." ([visitor] (-gen-walker visitor {})) ([visitor state-map] (Walker. visitor state-map)))
(defn walk "Builds a `dalap.walk/Walker` instance using the provided visitor\n  function, and then runs the walker on the given input object. If a\n  starte-map is given, it would use it as the internal state, otherwise an\n  empty PersistentMap is used." ([input visitor] (walk input visitor {})) ([input visitor state-map] ((-gen-walker visitor state-map) input)))