package com.example.map.pa1;

import java.util.HashMap;
import java.util.Map;
public class FirebaseContent {

    public String id;
    public String content;
    public String tag;
    public String imageName;

    public FirebaseContent(){

    }

    public FirebaseContent(String id, String content, String tag,String imageName){
        this.id=id;
        this.content=content;
        this.tag=tag;
        this.imageName=imageName;

    }

    public Map<String, Object> toMap2(){
        HashMap<String, Object> result= new HashMap<>();
        result.put("id",id);
        result.put("content", content);
        result.put("tag", tag);
        result.put("imageName",imageName);
        return result;
    }

}
