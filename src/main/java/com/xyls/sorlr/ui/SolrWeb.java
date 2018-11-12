package com.xyls.sorlr.ui;

import com.xyls.sorlr.model.SolerQueryBean;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("solr")
public class SolrWeb {

    @Autowired
    private SolrClient solrClient;


    @RequestMapping(value="/getindex", method = RequestMethod.POST)
    public Map getIndex(@RequestBody SolerQueryBean solerQueryBean) {
        List<String> keyword = solerQueryBean.getKeyword();
        Map<String,Object> map= new HashMap();
        if(keyword.size()>0){
            keyword.forEach(r->{
                SolrQuery sq = new SolrQuery();
                sq.set("collection","core");
                sq.set("q", "id:"+solerQueryBean.getId());
                sq.set("fl", "freq:termfreq(text,"+r+")");
                try {
                    QueryResponse result = solrClient.query(sq);
                    SolrDocumentList results = result.getResults();
                    for (SolrDocument solrDocument : results) {
                        Object freq = solrDocument.get("freq");
                        map.put(r,freq);
                    }
                } catch (SolrServerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            return map;
        }
        else {
            return null;
        }
    }

    @RequestMapping(value="/getAll", method = RequestMethod.GET)
    public List<Object> getAll() {
        List<Object> list= new ArrayList<>();
        SolrQuery sq = new SolrQuery();
        sq.set("collection","core");
        sq.set("q", "*:*");
        sq.set("fl", "id");
        try {
            QueryResponse result = solrClient.query(sq);
            SolrDocumentList results = result.getResults();
            for (SolrDocument solrDocument : results) {
                Object id = solrDocument.get("id");
                list.add(id);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
