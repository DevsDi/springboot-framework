package com.dev.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtils {
	public static File saveUrlAs(String photoUrl, String path,String annex_name) {
		// 此方法只能用户HTTP协议
		try {
	        
	        File file=new File(path);
	        if (!file.exists()) {
				file.mkdirs();
			}
	        
	        String fileName=path+File.separator+annex_name;
			URL url = new URL(photoUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			DataInputStream in = new DataInputStream(connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			return new File(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
