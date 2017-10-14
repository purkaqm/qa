package com.powersteeringsoftware.libs.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 10.10.13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public interface SearchResult {
    boolean verifyData(String txt, SearchEngine.Type type);

    List<String> getResultList();

    List<Result> getResults();

    class Result {
        private Map<String, String> map = new HashMap<String, String>();

        public String toString() {
            return map.toString();
        }

        public String get(String k) {
            return map.get(k);
        }

        public void put(String k, String v) {
            map.put(k, v);
        }

        public List<String> values() {
            return new ArrayList<String>(map.values());
        }

        public List<String> keys() {
            return new ArrayList<String>(map.keySet());
        }


    }
}
