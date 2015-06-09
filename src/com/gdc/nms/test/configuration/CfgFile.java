package com.gdc.nms.test.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CfgFile {
    private Pattern _section = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
    private Pattern _keyValue = Pattern.compile("\\s*([^=]*)=(.*)");
    private Pattern _innerValue = Pattern.compile("\\$\\{([A-Za-z0-9\\.]*)\\}");
    private Map<String, Map<String, String>> _entries = new LinkedHashMap<String, Map<String, String>>();

    public CfgFile() {

    }

    public CfgFile(String path) throws IOException {
        load(path);
    }

    public void load(String path) throws IOException {
       
        	BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            String section = null;
            String lastKey = "";
            String lastValue = "";
            while ((line = br.readLine()) != null) {
                Matcher m = _section.matcher(line);
                if (m.matches()) {
                    section = m.group(1).trim();
                    lastKey = section;
                } else if (section != null) {
                	
                    m = _keyValue.matcher(line);
                    if (m.matches()) {
                        String key = m.group(1).trim();
                        String value = m.group(2).trim();

                        lastValue = key;

                        value = replace(value);

                        Map<String, String> kv = _entries.get(section);
                        if (kv == null) {
                            _entries.put(section, kv = new LinkedHashMap<String, String>());
                        }
                        kv.put(key, value);
                    } else {
                        if (!line.isEmpty()) {
                            line = replace(line);
                            String oldValue = _entries.get(lastKey).get(lastValue);
                            _entries.get(lastKey).put(lastValue, oldValue.concat("\n").concat(line));
                        }
                    }
                }
            }
        
    }

    private String replace(String line) {
        Matcher matcher = _innerValue.matcher(line);

        while (matcher.find()) {
            String v = matcher.group(1);
            String[] split = v.split("[.]");

            line = line.replace("${" + v + "}", getString(split[0], split[1]));
        }

        return line;
    }

    public String getString(String section, String key) {
    	String info=getString(section, key, "");
    	if(info.contains("#")){
    		info=info.substring(0, info.indexOf("#"));
    	}
        
    	return info;
    }

    public String getString(String section, String key, String defaultvalue) {
        Map<String, String> kv = _entries.get(section);
        if (kv == null) {
            return defaultvalue;
        }
        return kv.get(key);
    }
    
    public Set<String> getKeySection(){
    	Set<String> keys=_entries.keySet();
    	return keys;
    }
    
    public Set<String> getFieldSection(String sectionName){
    	 Map<String, String> kv = _entries.get(sectionName);
    	return kv.keySet();
    }

    public void add(String section) {
        _entries.put(section, new HashMap<String, String>());
    }

    public void add(String section, String key, String value) {
        Map<String, String> map = _entries.get(section);
        if (map == null) {
            map = new HashMap<String, String>();
            map.put(key, value);
            _entries.put(section, map);
        } else {
            map.put(key, value);
        }
    }

    public void print() {
        for (Entry<String, Map<String, String>> e : _entries.entrySet()) {
            System.out.println("[" + e.getKey() + "]");
            for (Entry<String, String> ee : e.getValue().entrySet()) {
                System.out.println("K:" + ee.getKey());
                System.out.println("V:" + ee.getValue());
            }
        }
    }

//    public void write(String target) {
//    	File file=new File(target);
//    	file.get
//        Path path = Paths.get(target);
//        if (!Files.exists(path.getParent())) {
//            try {
//                Files.createDirectory(path.getParent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (!Files.exists(path)) {
//            try {
//                Files.createFile(path);
//                PrintWriter writer;
//                try {
//                    // onProcess(file);
//                    writer = new PrintWriter(path.toFile());
//                    for (Entry<String, Map<String, String>> e : _entries.entrySet()) {
//                        writer.println("[" + e.getKey() + "]");
//                        for (Entry<String, String> ee : e.getValue().entrySet()) {
//                            writer.println(ee.getKey() + "=" + ee.getValue());
//                        }
//                    }
//                    writer.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

}
