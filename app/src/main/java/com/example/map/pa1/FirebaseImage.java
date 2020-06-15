package com.example.map.pa1;

import java.util.HashMap;
import java.util.Map;

public class FirebaseImage {
    public String username;
    public String imageName;
    public FirebaseImage(){

    }
    public FirebaseImage(String username, String imageName){
        this.username=username;
        this.imageName=imageName;
    }

    public Map<String, Object> toMap3(){
        HashMap<String, Object> result= new HashMap<>();
        result.put("username",username);
        result.put("imageName", imageName);
        return result;
    }
}
