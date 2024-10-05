package org.tyby.javaapacheexp.core;

import javafx.concurrent.Task;
import org.tyby.javaapacheexp.tools.Tools;

import java.net.MalformedURLException;

/**
 * @author yhy
 * @date 2021/8/21 14:33
 * @github https://github.com/yhy0
 */

public class VulCheckTask extends Task<Void> {
    private String target;
    private String vulName;
    private String result;

    public VulCheckTask(String target, String vulName) {
        this.target = target;
        this.vulName = vulName;
    }

    protected Void call() throws MalformedURLException {
        String result = Tools.getExploit(vulName).checkVul(this.target);
        this.updateMessage(result);
        this.setResult(result);

        return null;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
