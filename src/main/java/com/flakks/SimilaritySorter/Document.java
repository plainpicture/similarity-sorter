
package com.flakks.SimilaritySorter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;

public class Document {
    private JSONObject doc;
    private List<Integer> keywordsList;
    private Set<Integer> keywordsSet;

    public Document(JSONObject doc, List<Integer> keywordsList) {
        this.doc = doc;
        this.keywordsList = keywordsList;

        this.keywordsSet = new HashSet(keywordsList.size());

        for(int i = 0; i < keywordsList.size(); i++)
            keywordsSet.add(keywordsList.get(i));
    }

    public JSONObject getDoc() {
        return doc;
    }

    public List<Integer> getKeywordsList() {
        return keywordsList;
    }

    public Set<Integer> getKeywordsSet() {
        return keywordsSet;
    }
}
