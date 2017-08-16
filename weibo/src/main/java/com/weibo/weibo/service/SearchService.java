package com.weibo.weibo.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwc on 2017/8/4.
 */
@Service
public class SearchService {
    private static String SOLR_URL = "http://localhost:8983/solr/weibo";
    private SolrClient solrClient = new HttpSolrClient.Builder(SOLR_URL).build();

    public List<Integer> search(String keyword) throws Exception {
        List<Integer> result = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(keyword);
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<strong>");
        solrQuery.setHighlightSimplePost("</strong>");
        solrQuery.set("df","weibo_content");
        SolrResponse response = solrClient.query(solrQuery);
        NamedList<Object> results = response.getResponse();
        SolrDocumentList list = (SolrDocumentList)results.get("response");
        for(SolrDocument document:list) {
            result.add(Integer.parseInt(document.get("id").toString()));
        }
        return result;
    }
}
