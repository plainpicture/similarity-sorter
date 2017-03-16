package com.flakks.SimilaritySorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Sorter {
    private void sort(List<Document> docs, List<Double> scores) {
        int count = docs.size();
        int b;

        for(int i = 1; i < count; i++) {
            Document reference = docs.get(i - 1);
            List<Integer> referenceKeywordsList = reference.getKeywordsList();

            int maxIndex = i;
            double maxValue = -1;

            for(int u = i; u < count; u++) {
                Document current = docs.get(u);
                Set<Integer> currentKeywordsSet = current.getKeywordsSet();
                double value = 0;

                for(Integer keyword : referenceKeywordsList) {
                    if(currentKeywordsSet.contains(keyword))
                        value += scores.get(keyword);
                }

                if(value > maxValue) {
                    maxIndex = u;
                    maxValue = value;
                }
            }

            Document oldDocument = docs.get(i);
            docs.set(i, docs.get(maxIndex));
            docs.set(maxIndex, oldDocument);
        }
    }
    
    private Object getSourceField(JSONObject source, String[] path) {
        Object res = source;
        int i;
        
        for(i = 0; i < path.length; i++)
            res = ((JSONObject)res).get(path[i]);
        
        return res;
    }
    
    public void sort(JSONObject jsonResponse, String field, long from, long newFrom, long size) {
        JSONArray hits = (JSONArray)((JSONObject)jsonResponse.get("hits")).get("hits");

        int total = hits.size();
        Map<String, Integer> map = new HashMap();
        List<Double> scores = new ArrayList();
        List<Document> docs = new ArrayList(total);
        String[] path = field.split("\\.");
        int i;

        for(Object object : hits) {
            JSONObject hit = (JSONObject)object;
            JSONObject source = (JSONObject)hit.get("_source");
            JSONArray keywords = (JSONArray)getSourceField(source, path);

            List<Integer> mappedKeywords = new ArrayList(keywords.size());

            for(Object keyword : keywords) {
                Integer index;

                index = map.get((String)keyword);

                if(index == null) {
                    index = map.size();

                    scores.add(1.0);
                    map.put((String)keyword, index);
                } else {
                    scores.set(index, scores.get(index) + 1);
                }

                mappedKeywords.add(index);
            }

            docs.add(new Document(hit, mappedKeywords));
        }

        for(i = 0; i < scores.size(); i++)
            scores.set(i, Math.log(total / scores.get(i)) + 1.0);

        sort(docs, scores);

        hits.clear();

        int start = (int)(from - newFrom);
        int stop = Math.min(docs.size(), start + (int)size);

        for(i = start; i < stop; i++)
            hits.add(docs.get(i).getDoc());
    }
}
