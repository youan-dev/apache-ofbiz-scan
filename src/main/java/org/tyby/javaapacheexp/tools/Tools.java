package org.tyby.javaapacheexp.tools;

/**
 * @author yhy
 * @date 2021/3/25 11:20
 * @github https://github.com/yhy0
 */

// http 请求对象，取自 shack2 的Java反序列化漏洞利用工具V1.7

import javafx.scene.control.Alert;
import javafx.stage.Window;
import org.apache.log4j.Logger;
import org.tyby.javaapacheexp.controller.MainController;
import org.tyby.javaapacheexp.core.ExploitInterface;
import org.tyby.javaapacheexp.exp.CVE_2024_45507_EXP;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public static Logger logger = Logger.getLogger(MainController.class);
    public Tools() {
    }


    public static void alert(String header_text, String content_text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        // 点 x 退出
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });

        alert.setHeaderText(header_text);
        alert.setContentText(content_text);
        alert.show();
    }

    // 因为使用echo 写 shell ，这里需要对 < > 转义
    public static String get_escape_shell(String str, String platform) {
        String key1 = "<";
        String key2 = ">";

        if (platform.equals("Linux")) {

            return "'" + str + "'";
        } else {
            return escape(key2, escape(key1, str, "^"), "^");
        }

    }

    public static String escape(String key, String str, String escape_str) {
        StringBuffer stringBuilder1 = new StringBuffer(str);
        int a = str.indexOf(key);
        int i = 0;
        while (a != -1) {
            stringBuilder1.insert(a + i, escape_str);
            a = str.indexOf(key, a + 1);
            i++;
        }

        return stringBuilder1.toString();
    }

    public static String checkTheDomain(String weburl) {
        if ("".equals(weburl.trim())) {
            return "";
        } else {
            if (!weburl.startsWith("http")) {
                weburl = "http://" + weburl;
            }

            if (!weburl.endsWith("/")) {
                weburl = weburl + "/";
            }

            return weburl;
        }
    }

    public static String urlParse(String url) {
        if (!url.contains("http")) {
            url = "http://" + url;
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }


    public static boolean checkTheURL(String weburl) {
        if ("".equals(weburl.trim())) {
            return false;
        } else {
            return weburl.startsWith("http");
        }
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String getDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String reverse(String data) {
        StringBuilder sb = new StringBuilder(data);
        return sb.reverse().toString();
    }

    public static HashSet<String> read(String path, String encode, Boolean domain) {
        HashSet list = new HashSet();

        try {
            FileInputStream fs = new FileInputStream(new File(path));
            InputStreamReader isr = null;
            if (encode.equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encode);
            }

            BufferedReader br = new BufferedReader(isr);
            String tem = null;

            while ((tem = br.readLine()) != null) {
                if (domain) {
                    tem = checkTheDomain(tem);
                }
                if (!list.contains(tem)) {
                    list.add(tem);
                }
            }

            br.close();
            isr.close();
        } catch (Exception var7) {
        }

        return list;
    }

    public static boolean write(String path, String value) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            out.write(value);
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    // 根据选择对应的漏洞检测
    public static ExploitInterface getExploit(String vulName) {
        ExploitInterface ei = null;
        if(vulName.contains("CVE-2024-45507")) {
            // 这里创建你的cve漏洞检测，注意要实现 ExploitInterface 接口
            ei = new CVE_2024_45507_EXP();
        }

        return (ExploitInterface) ei;
    }


    public static String str2Hex(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();

        for (int i = 0; i < bs.length; ++i) {
            int bit = (bs[i] & 240) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 15;
            sb.append(chars[bit]);
        }

        return sb.toString().trim();
    }

    public static String hex2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];

        for (int i = 0; i < bytes.length; ++i) {
            int n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 255);
        }

        return new String(bytes);
    }

    public static String loadExp(String path) {
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream(path);
            pro.load(in);
            String exp = (String) pro.get("exp");
            return exp;
        } catch (IOException var4) {
            logger.debug(var4);
            return "";
        }
    }

    // 去除html
    public static String regReplace(String content) {
        String pattern = "<.*html.*>[\\s\\S]*</html>";
        String newString = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        String result = m.replaceAll(newString);
        return result;
    }

    // 随机字符
    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return sb.toString();
    }



    // base64编码
    public static String Base64Encode(String txt) {
        try {
            return Base64.getEncoder().encodeToString(txt.getBytes("UTF-8"));
        } catch (Exception var2) {
            logger.debug(var2);
            return "";
        }
    }

    // 获取weblogic 的exp文本
    public static String getExp(String path) {
        InputStream in = Tools.class.getClassLoader().getResourceAsStream(path);

        Scanner s = (new Scanner(in)).useDelimiter("\\A");
        String str = s.hasNext() ? s.next() : "";

        return str;
    }

    public static URL getUrl(String strURL) throws MalformedURLException {
        // 创建URL对象
        URL url = new URL(strURL);
        return url;
    }

}
