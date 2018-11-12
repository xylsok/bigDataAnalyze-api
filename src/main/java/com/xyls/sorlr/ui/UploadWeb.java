package com.xyls.sorlr.ui;

import com.xyls.sorlr.utils.FileUtil;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("solr")
public class UploadWeb {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)

    public Map upload(@RequestParam("file") MultipartFile file) {
        Map map = new HashMap();
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String filePath = "/usr/local/solr-7.5.0/Import/";
        try {
            File file1 = new File(filePath+"/"+fileName);
            //如果有先清空
            if(file1.exists()){
                file1.delete();
            }

            com.xyls.sorlr.utils.FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            map.put("code", "200");
            map.put("name", fileName);
            map.put("size", FileUtil.getPrintSize(file.getSize()));
            map.put("type", type);
            map.put("result", "上传成功");
            //导入索引库
            importFile();

        } catch (Exception e) {
            map.put("code", "500");
            map.put("name", fileName);
            map.put("size", FileUtil.getPrintSize(file.getSize()));
            map.put("type", type);
            map.put("result", "上传失败了");
            e.printStackTrace();
        }
        return map;
    }

    @Value("${solrId}")
    private String solrId;
    public Map importFile() {

        CloseableHttpClient client = HttpClients.createDefault();

        //新建一个HttpPost请求的对象 --并将uri传给这个对象
        HttpPost post = new HttpPost("http://localhost:8888/solr/core/dataimport?_="+solrId+"&indent=on&wt=json");
        //使用NameValuePair  键值对  对参数进行打包
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("command", "full-import"));
        list.add(new BasicNameValuePair("commit", "true"));
        list.add(new BasicNameValuePair("clean", "true"));
        list.add(new BasicNameValuePair("entity", "file"));
        list.add(new BasicNameValuePair("name", "dataimport"));
        list.add(new BasicNameValuePair("optimize", "true"));
        list.add(new BasicNameValuePair("core", "core"));
        //4.对打包好的参数，使用UrlEncodedFormEntity工具类生成实体的类型数据
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        //5.将含有请求参数的实体对象放到post请求中
        post.setEntity(entity);
        //6.新建一个响应对象来接收客户端执行post的结果
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            //7.从响应对象中提取需要的结果-->实际结果,与预期结果进行对比
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
